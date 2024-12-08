package com.switchmaker.smpay.coris_bf;

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

import static com.switchmaker.smpay.constant.urls.RootUrl.BF_CORIS_MONEY_API_URL;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;
import static com.switchmaker.smpay.constant.values.MeansOfPaymentConstantValues.*;

@Service
public class CorisServicesImplement implements CorisServices {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	PlatformService platformService;
	@Autowired
	UserService userService;
	@Autowired
	PaymentService paymentService;

	@Override
	public ResponseEntity<?> paymentOtp(ValidPaymentInformations validPaymentInformations) {
		// TODO Auto-generated method stub
		try {
			  final String uri = BF_CORIS_MONEY_API_URL+ "send-code-otp?codePays=226&telephone="+validPaymentInformations.getPayoutInformation().getAccountNumber();
			  HttpHeaders headers = new HttpHeaders();
			  headers.setContentType(MediaType.APPLICATION_JSON);
			  headers.set("hashParam", UtilsClass.hashString("226") + UtilsClass.hashString(validPaymentInformations.getPayoutInformation().getAccountNumber()) + UtilsClass.hashString(BF_CORIS_CLIENT_SECRET));
			  headers.set("clientId", BF_CORIS_CLIENT_ID);
			  HttpEntity<?> requestEntity = new HttpEntity<>(headers);
			  ResponseEntity<String> response = restTemplate.postForEntity(uri, requestEntity, String.class);
			  if (response.getStatusCodeValue() == 200) {
				  ObjectMapper mapper = new ObjectMapper();
				  JsonNode jsonObject = mapper.readTree(response.getBody());
				  String code = jsonObject.get("code").asText();
				  String message = jsonObject.get("message").asText();
				  switch (code) {

				  	case "0":
						  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(SUCCESS_CODE,message),HttpStatus.OK);

				  	case "-1":
						  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,message),HttpStatus.OK);

					  default:
						  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"erreur inconnue"),HttpStatus.OK);
					}
			  	}
		  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Erreur inconnue"),HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Une erreur est survenue"),HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> paymentAccountDebited(ValidPaymentInformations validPaymentInformations) {
		// TODO Auto-generated method stub
		try {
				  final String uri = BF_CORIS_MONEY_API_URL+ "operations/paiement-bien?codePays=226&telephone=="+validPaymentInformations.getPayoutInformation().getAccountNumber()+"&codePv=" + BF_CORIS_CODE_PV + "&montant=" + validPaymentInformations.getPayoutInformation().getAmountPayment()+"&codeOTP="+validPaymentInformations.getPayoutInformation().getOtpCode();
				  HttpHeaders headers = new HttpHeaders();
				  headers.setContentType(MediaType.APPLICATION_JSON);
				  headers.set("hashParam", UtilsClass.hashString("226") + UtilsClass.hashString(validPaymentInformations.getPayoutInformation().getAccountNumber()) + UtilsClass.hashString(BF_CORIS_CODE_PV) + UtilsClass.hashString(""+validPaymentInformations.getPayoutInformation().getAmountPayment()) + UtilsClass.hashString(validPaymentInformations.getPayoutInformation().getOtpCode()) + UtilsClass.hashString(BF_CORIS_CLIENT_SECRET));
				  headers.set("clientId", BF_CORIS_CLIENT_ID);
				  HttpEntity<?> requestEntity = new HttpEntity<>(headers);
				  ResponseEntity<String> response = restTemplate.postForEntity(uri, requestEntity, String.class);
				  if (response.getStatusCodeValue() == 200) {
					  ObjectMapper mapper = new ObjectMapper();
					  JsonNode jsonObject = mapper.readTree(response.getBody());
					  String code = jsonObject.get("code").asText();
					  String message = jsonObject.get("message").asText();
					  switch (code) {
					  	case "0":
					  		//Enregistrement du paiement
					  		ResponseMessage responseMessage = paymentService.savePayment(validPaymentInformations, SUCCESS);
					  		return new ResponseEntity<ResponseMessage>(responseMessage,HttpStatus.OK);
					  	case "-1":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,message),HttpStatus.OK);

						  default:
							  break;
				  }
			  }
			  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Erreur inconnue"),HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Une erreur est survenue"),HttpStatus.OK);
		}
	}
}

