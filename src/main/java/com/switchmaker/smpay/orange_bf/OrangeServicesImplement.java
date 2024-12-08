package com.switchmaker.smpay.orange_bf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.switchmaker.smpay.dto.ResponseMessage;
import com.switchmaker.smpay.dto.ValidPaymentInformations;
import com.switchmaker.smpay.services.PaymentService;
import com.switchmaker.smpay.services.PlatformService;
import com.switchmaker.smpay.services.UserService;
import com.switchmaker.smpay.utilsClass.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.switchmaker.smpay.constant.urls.RootUrl.BF_ORANGE_MONEY_API_URL;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.FAILURE_CODE;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.SUCCESS;

@Service
public class OrangeServicesImplement implements OrangeServices {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	PlatformService platformService;
	@Autowired
	UserService userService;
	@Autowired
	PaymentService paymentService;

	@Override
	public ResponseEntity<?> payment(ValidPaymentInformations validPaymentInformation) {
		try {
			  String xmlString = orangeMoneyXml(
					  validPaymentInformation.getPayoutInformation().getAccountNumber(),
					  ""+validPaymentInformation.getPayoutInformation().getAmountPayment(),
					  validPaymentInformation.getPayoutInformation().getOtpCode());

				  ResponseEntity<String> response = postRequest(xmlString, BF_ORANGE_MONEY_API_URL);
			System.out.println("========================");
				  System.out.println(response);
				  if(!response.getBody().equals("error")) {
					  XmlMapper xmlMapper = new XmlMapper();
					  JsonNode node = xmlMapper.readTree(response.getBody());
					  String code = node.toString();
					  switch (code) {
					  	case "\"200\"":
					  		//Enregistrement du paiement
					  		ResponseMessage responseMessage = paymentService.savePayment(validPaymentInformation, SUCCESS);
					  		return new ResponseEntity<ResponseMessage>(responseMessage,HttpStatus.OK);

						  case "\"990418\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"OTP deja utilise"), HttpStatus.OK);

						  case "\"990417\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"OTP n'existe pas"),HttpStatus.OK);

						  case "\"OTPINV\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"OTP invalide"), HttpStatus.OK);

						  case "\"00409\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le montant de la transaction est inférieur à la valeur minimale définie"), HttpStatus.OK);

						  case "\"409\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le montant de la transaction est inférieur à la valeur minimale définie"),HttpStatus.OK);

						  case "\"410\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le montant de la transaction est supérieur à la valeur autorisée"), HttpStatus.OK);

						  case "\"00410\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le montant de la transaction est supérieur à la valeur autorisée"),HttpStatus.OK);

						  case "\"02117\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le compte utilisateur est verrouillé, il peut être déverrouillé par CCE"),HttpStatus.OK);

						  case "\"09988\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Un problème est survenu lors de la transaction avec le marchand"),HttpStatus.OK);

						  case "\"11007\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Un problème est survenu lors de la transaction avec le marchand"),HttpStatus.OK);

						  case "\"60011\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le client a atteint le nombre maximal de transactions pour la journée"),HttpStatus.OK);

						  case "\"60012\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le client a atteint le nombre maximal de transactions pour la semaine"),HttpStatus.OK);

						  case "\"60013\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le client a atteint le nombre maximal de transactions pour le mois"),HttpStatus.OK);

						  case "\"60014\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le client a atteint le montant maximal de transactions pour la journée"),HttpStatus.OK);

						  case "\"60015\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le client a atteint le montant maximal de transactions pour la semaine"),HttpStatus.OK);

						  case "\"60016\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le client a atteint le montant maximal de transactions pour le mois"),HttpStatus.OK);

						  case "\"60019\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le client a atteint la limite de solde minimum"),HttpStatus.OK);

						  case "\"90001\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Transaction non autorisée"),HttpStatus.OK);

						  case "\"99046\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le montant de la transaction est inférieur au seuil minimal"),HttpStatus.OK);

						  case "\"99047\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le montant de la transaction est supérieur au seuil minimal"),HttpStatus.OK);

						  case "\"99987\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le service n'est pas accessible"),HttpStatus.OK);

						  case "\"99990\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"L'utilisateur essaie de faire la transaction plus que le solde disponible"),HttpStatus.OK);

						  case "\"99996\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le compte de l'utilisateur est suspendu, l'utilisateur ne peut pas effectuer la transaction"),HttpStatus.OK);

						  case "\"100004\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le montant que vous avez entré est supérieur au montant maximum autorisé pour ce service"),HttpStatus.OK);

						  case "\"0100004\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le montant que vous avez entré est supérieur au montant maximum autorisé pour ce service"),HttpStatus.OK);

						  case "\"0100005\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le montant que vous avez entré est supérieur au montant maximum autorisé pour le destinataire"),HttpStatus.OK);

						  case "\"08\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Montant invalide"),HttpStatus.OK);

						  case "\"00042\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le montant demandé n'est pas un multiple de la valeur configurée"),HttpStatus.OK);

						  case "\"00043\"":
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Le montant par lequel la transaction est initiée n'est pas configuré dans le système"),HttpStatus.OK);

						  default:
							  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Erreur inconue"),HttpStatus.OK);
						  	}
				  }
			 } catch (IOException e1) {

		  }
	  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Une erreur est survenue"),HttpStatus.OK);
}




	/*-------------------------------------------/
	/*      la chaine XML de Orange Money       /
	/*----------------------------------------*/

	public String orangeMoneyXml(String clientPhoneNumber, String amount, String otp) {

		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
    			"<COMMAND>\r\n" +
    			" <TYPE>OMPREQ</TYPE>\r\n" +
    			" <customer_msisdn>"+clientPhoneNumber+"</customer_msisdn>\r\n" +
    			" <merchant_msisdn>65566062</merchant_msisdn>\r\n" +
    			" <api_username>SWITCHMAKER</api_username>\r\n" +
    			" <api_password>MAKER@43</api_password>\r\n" +
    			" <amount>"+amount+"</amount>\r\n" +
    			" <PROVIDER>101</PROVIDER>\r\n" +
    			" <PROVIDER2>101</PROVIDER2>\r\n" +
    			" <PAYID>12</PAYID>\r\n" +
    			" <PAYID2>12</PAYID2>\r\n" +
    			" <otp>"+otp+"</otp>\r\n" +
    			" <reference_number>789233</reference_number>\r\n" +
    			" <ext_txn_id>201500068544</ext_txn_id>\r\n" +
    			"</COMMAND>";
			return xmlString;
	}



	/*--------------------------------------------------------------------------/
	/*        Envoie des informations de paiement Orange Money post XML        /
	/*-----------------------------------------------------------------------*/

	public ResponseEntity<String> postRequest(String body, String apiUrl){
		try {
			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			messageConverters.add(new StringHttpMessageConverter());
			restTemplate.setMessageConverters(messageConverters);
			HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_XML);
	        HttpEntity<String> request = new HttpEntity<String>(body, headers);
	        ResponseEntity<String> response = restTemplate.exchange(apiUrl,HttpMethod.POST,request, String.class);
			return response;

		} catch (Exception e) {
			return new ResponseEntity<String>("error", HttpStatus.OK);
		}

	}

}
