package com.switchmaker.smpay.mtn_ci;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.switchmaker.smpay.dto.ResponseMessage;
import com.switchmaker.smpay.dto.ValidPaymentInformations;
import com.switchmaker.smpay.services.PaymentService;
import com.switchmaker.smpay.services.PlatformService;
import com.switchmaker.smpay.services.UserService;
import com.switchmaker.smpay.utilsClass.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static com.switchmaker.smpay.constant.urls.RootUrl.CI_MTN_API_URL;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.FAILURE_CODE;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.INITIATE;
import static com.switchmaker.smpay.constant.values.MeansOfPaymentConstantValues.*;

@Service
public class MtnServicesImplement implements MtnServices {

	@Autowired
	PlatformService platformService;
	@Autowired
	UserService userService;
	@Autowired
	PaymentService paymentService;


	    /*------------------------------------------------------------/
	   /           MTN SERVICE INITIATE CLIENT TRANSACTION           /
	  /------------------------------------------------------------*/
		@Override
		public ResponseEntity<?> clientPayment(ValidPaymentInformations validPaymentInformations) {
			final String url = CI_MTN_API_URL+ "collection/v1_0/requesttopay";
			String externalId = UUID.randomUUID().toString();
			MtnPayer payer = new MtnPayer();
			payer.setPartyIdType("MSISDN");
			payer.setPartyId(validPaymentInformations.getPayoutInformation().getAccountNumber());
			MtnRequestToPay mtnRequestTopay = new MtnRequestToPay();

			mtnRequestTopay.setAmount(""+validPaymentInformations.getPayoutInformation().getAmountPayment());
			mtnRequestTopay.setCurrency("EUR");//XOF en production
			mtnRequestTopay.setExternalId(externalId);
			mtnRequestTopay.setPayer(payer);
			mtnRequestTopay.setPayerMessage("Paiement en ligne");
			mtnRequestTopay.setPayeeNote(validPaymentInformations.getPayoutInformation().getAccountNumber()+" Paiement en ligne");

			try {
					RestTemplate restTemplate = new RestTemplate();
					HttpHeaders headers = new HttpHeaders();
					headers.set("X-Reference-Id", externalId);
					headers.set("X-Target-Environment", CI_MTN_TARGET_ENVIRONMENT);
					headers.set("Content-Type", "application/json");
					headers.set("Ocp-Apim-Subscription-Key", CI_MTN_COLLECTION_SERVICE_SUBSCRIPTION_KEY);
					headers.setBearerAuth(getToken());

					HttpEntity<MtnRequestToPay> entity = new HttpEntity<>(mtnRequestTopay, headers);
					ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

					if (response.getStatusCodeValue() == 202) {
						//Enregistrement du paiement
				  		ResponseMessage responseMessage = paymentService.savePayment(validPaymentInformations, INITIATE);
				  		return new ResponseEntity<ResponseMessage>(responseMessage,HttpStatus.OK);
					}
				return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Une erreur est survenue1"),HttpStatus.OK);
			}catch (Exception e) {
				return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Une erreur est survenue2"),HttpStatus.OK);
			}
		}


		  /*--------------------------------------------------------------/
	     /             MTN SERVICE GET TRANSACTION  STATUS               /
	    /--------------------------------------------------------------*/
		@Override
		public String paymentStatus(String transactionId) {
			final String url = CI_MTN_API_URL+ "collection/v1_0/requesttopay/"+transactionId;
			try {
					RestTemplate restTemplate = new RestTemplate();
					HttpHeaders headers = new HttpHeaders();
					headers.set("X-Target-Environment", CI_MTN_TARGET_ENVIRONMENT);
					headers.set("Ocp-Apim-Subscription-Key", CI_MTN_COLLECTION_SERVICE_SUBSCRIPTION_KEY);
					headers.setBearerAuth(getToken());
					HttpEntity<?> entity = new HttpEntity<>(headers);
					ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
					if (response.getStatusCodeValue() == 200) {
						  ObjectMapper mapper = new ObjectMapper();
						  JsonNode jsonObject = mapper.readTree(response.getBody());
						  String message = jsonObject.get("status").asText();
						  return message.toLowerCase();
					  }
					return "error";
				} catch (Exception e) {return "error";}
		}



		@Override
		public String getToken() {
			try {
				  RestTemplate restTemplate = new RestTemplate();

				  String url = CI_MTN_API_URL + "collection/token/";

				  HttpHeaders headers = new HttpHeaders();
				  //headers.setContentType(MediaType.APPLICATION_JSON);
				  headers.set("Ocp-Apim-Subscription-Key", CI_MTN_COLLECTION_SERVICE_SUBSCRIPTION_KEY);
				  headers.setBasicAuth(CI_MTN_USERNAME_UUID_GENERATED, CI_MTN_API_KEY);
				  HttpEntity<?> entity = new HttpEntity<>(headers);
				  ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
				  if (response.getStatusCodeValue() == 200) {
					  ObjectMapper mapper = new ObjectMapper();
					  JsonNode actualObj = mapper.readTree(response.getBody());
					  String token =  actualObj.get("access_token").asText();
					  return token;
				  }
				  return "error";
			}catch (Exception e) {
				return "error";
			}
		}
}
