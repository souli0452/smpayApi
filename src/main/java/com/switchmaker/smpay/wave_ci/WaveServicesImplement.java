package com.switchmaker.smpay.wave_ci;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.switchmaker.smpay.dto.PaymentResponse;
import com.switchmaker.smpay.dto.ResponseMessage;
import com.switchmaker.smpay.dto.ValidPaymentInformations;
import com.switchmaker.smpay.services.PaymentService;
import com.switchmaker.smpay.services.PlatformService;
import com.switchmaker.smpay.utilsClass.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.switchmaker.smpay.constant.urls.RootUrl.CI_WAVE_API_URL;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;
import static com.switchmaker.smpay.constant.values.MeansOfPaymentConstantValues.CI_WAVE_API_KEY;

@Service
public class WaveServicesImplement implements WaveServices {

	@Autowired
	PlatformService platformService;

	@Autowired
	PaymentService paymentService;

	@Override
	public ResponseEntity<?> clientPayment(ValidPaymentInformations validPaymentInformations) {
		final String uri = CI_WAVE_API_URL+ "/v1/checkout/sessions";

		WaveCheckoutSession checkoutSession = new WaveCheckoutSession();
		checkoutSession.setAmount(""+validPaymentInformations.getPayoutInformation().getAmountPayment());
		checkoutSession.setCurrency("XOF");
		checkoutSession.setErrorUrl(validPaymentInformations.getPayoutInformation().getErrorUrl());
		checkoutSession.setSuccessUrl(validPaymentInformations.getPayoutInformation().getSuccessUrl());

		try {
					RestTemplate restTemplate = new RestTemplate();
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					headers.setBearerAuth(CI_WAVE_API_KEY);

					HttpEntity<WaveCheckoutSession> entity = new HttpEntity<>(checkoutSession, headers);
					ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

					if (response.getStatusCodeValue() == 200) {
						  ObjectMapper mapper = new ObjectMapper();
						  JsonNode jsonObject = mapper.readTree(response.getBody());
						  //payoutInfos.setRequestId(jsonObject.get("id").asText());
						  validPaymentInformations.getPayoutInformation().setRequestId(jsonObject.get("id").asText());
						  //Enregistrement du paiement
					  		ResponseMessage responseMessage = paymentService.savePayment(validPaymentInformations, INITIATE);
					  		if(responseMessage!=null) {
					  			PaymentResponse paymentResponse = (PaymentResponse) responseMessage.getData();
					  			WaveTransactionResponse waveResponse = new WaveTransactionResponse(
					  					SUCCESS_CODE,
					  					paymentResponse.getPaymentCode(),
					  					paymentResponse.getAccountNumber(),
					  					paymentResponse.getPaymentAmount(),
					  					paymentResponse.getPaymentType(),
					  					paymentResponse.getPaymentOrigin(),
					  					paymentResponse.getPaymentDate(),
					  					INITIATE,
					  					jsonObject.get("error_url").asText(),
					  					jsonObject.get("success_url").asText(),
					  					jsonObject.get("wave_launch_url").asText()
					  					);
					  				return new ResponseEntity<WaveTransactionResponse>(waveResponse,HttpStatus.OK);
					  			}
					}
				return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Une erreur est survenue"),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Une erreur est survenue"),HttpStatus.OK);
		}
	}


	  /*--------------------------------------------------------------/
     /           WAVE SERVICE GET TRANSACTION  STATUS                /
    /--------------------------------------------------------------*/
	@Override
	public String paymentStatus(String transactionId) {
		final String uri = CI_WAVE_API_URL+ "/v1/checkout/sessions/"+transactionId;
		try {
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setBearerAuth(CI_WAVE_API_KEY);
				HttpEntity<?> entity = new HttpEntity<>(headers);
				ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
				if (response.getStatusCodeValue() == 200) {
					  ObjectMapper mapper = new ObjectMapper();
					  JsonNode jsonObject = mapper.readTree(response.getBody());
					  String message = jsonObject.get("payment_status").asText();
					  return message;
				  }
				return "error";
			} catch (Exception e) {return "error";}
	}

}
