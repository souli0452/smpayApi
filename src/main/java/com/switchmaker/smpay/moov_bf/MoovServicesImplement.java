package com.switchmaker.smpay.moov_bf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.switchmaker.smpay.dto.ResponseMessage;
import com.switchmaker.smpay.dto.ValidPaymentInformations;
import com.switchmaker.smpay.payout.PayoutInformation;
import com.switchmaker.smpay.services.PaymentService;
import com.switchmaker.smpay.services.PlatformService;
import com.switchmaker.smpay.services.UserService;
import com.switchmaker.smpay.utilsClass.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static com.switchmaker.smpay.constant.urls.RootUrl.BF_MOOV_MONEY_API_URL;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.FAILURE_CODE;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.INITIATE;
import static com.switchmaker.smpay.constant.values.MeansOfPaymentConstantValues.*;

@Service
public class MoovServicesImplement implements MoovServices {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	PlatformService platformService;
	@Autowired
	UserService userService;
	@Autowired
	PaymentService paymentService;



	    /*--------------------------------------------------------------------/
	   /           MOOV MONEY SERVICE INITIATE CLIENT TRANSACTION            /
	  /--------------------------------------------------------------------*/
		@Override
		public ResponseEntity<?> clientPayment(ValidPaymentInformations validPaymentInformation){

			MoovExtendedData data = new MoovExtendedData();
			MoovClientTransaction clientTransaction = new MoovClientTransaction();

			String requestId = UtilsClass.codeGenerate();
			clientTransaction.setRequestId(requestId);
			clientTransaction.setDestination(validPaymentInformation.getPayoutInformation().getAccountNumber());
			clientTransaction.setAmount(validPaymentInformation.getPayoutInformation().getAmountPayment());
			clientTransaction.setRemarks("Transaction");
			clientTransaction.setMessage("Vous êtes sur le point d'effectuer un paiement de "+validPaymentInformation.getPayoutInformation().getAmountPayment()+"f cfa. Veuillez confirmer la transaction.");
			clientTransaction.setData(data);
			try {
					  HttpHeaders headers = new HttpHeaders();
					  headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
					  headers.setContentType(MediaType.APPLICATION_JSON);
					  headers.set("command-id", BF_MOOV_MONEY_CLIENT_TRANSFERT_ID);
					  headers.setBasicAuth(BF_MOOV_MONEY_USERNAME, BF_MOOV_MONEY_PASSWORD);
					  HttpEntity<MoovClientTransaction> entity = new HttpEntity<>(clientTransaction, headers);
					  restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
					  ResponseEntity<String> response = restTemplate.exchange(BF_MOOV_MONEY_API_URL, HttpMethod.POST, entity, String.class);
					  if (response.getStatusCodeValue() == 200) {
						  ObjectMapper mapper = new ObjectMapper();
						  JsonNode jsonObject = mapper.readTree(response.getBody());
						  String code = jsonObject.get("status").asText();
						  String message = jsonObject.get("message").asText();
						  switch (code) {
						  	case "0":
						  		//Enregistrement du paiement
						  		ResponseMessage responseMessage = paymentService.savePayment(validPaymentInformation, INITIATE);
						  		return new ResponseEntity<ResponseMessage>(responseMessage,HttpStatus.OK);

						  	case "12":
								  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"La transaction a échoué"),HttpStatus.OK);

						  	case "15":
								  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,message),HttpStatus.OK);

							  default:
								  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,message),HttpStatus.OK);
							  	}

					  }
				  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Erreur inconnue"),HttpStatus.OK);

			} catch (Exception e) {
				return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Une erreur est survenue"),HttpStatus.OK);
			}
		}

	@Override
	public ResponseEntity<?> merchandPayment(PayoutInformation transactionInfos) {
		// TODO Auto-generated method stub
		return null;
	}

		/*--------------------------------------------------------------/
	   /           MOOV MONEY SERVICE GET TRANSACTION  STATUS          /
	  /--------------------------------------------------------------*/
		@Override
		public String paymentStatus(String transactionId){

			MoovTransactionStatus transaction = new MoovTransactionStatus();
			transaction.setRequestId(transactionId);
			try {
				  HttpHeaders headers = new HttpHeaders();
				  headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				  headers.setContentType(MediaType.APPLICATION_JSON);
				  headers.set("command-id", BF_MOOV_MONEY_TRANSACTION_STATUS);
				  headers.setBasicAuth(BF_MOOV_MONEY_USERNAME, BF_MOOV_MONEY_PASSWORD);
				  HttpEntity<MoovTransactionStatus> entity = new HttpEntity<>(transaction, headers);
				  restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
				  ResponseEntity<String> response = restTemplate.exchange(BF_MOOV_MONEY_API_URL, HttpMethod.POST, entity, String.class);
				  if (response.getStatusCodeValue() == 200) {
					  ObjectMapper mapper = new ObjectMapper();
					  JsonNode jsonObject = mapper.readTree(response.getBody());
					  String message = jsonObject.get("message").asText();
					  return message.toLowerCase();
				  }
				  return "error";
			} catch (Exception e) {return "error";}

		}

}
