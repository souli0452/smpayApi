package com.switchmaker.smpay.test;

import com.switchmaker.smpay.mtn_ci.MtnRequestToPay;
import com.switchmaker.smpay.mtn_ci.MtnServices;
import com.switchmaker.smpay.payout.PayoutInformation;
import com.switchmaker.smpay.utilsClass.UtilsClass;
import com.switchmaker.smpay.wave_ci.WaveCheckoutSession;
import com.switchmaker.smpay.wave_ci.WaveCheckoutSessionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.switchmaker.smpay.constant.urls.RootUrl.*;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.ADMIN;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.DEVELOPER;
import static com.switchmaker.smpay.constant.values.MeansOfPaymentConstantValues.*;


@CrossOrigin("*")
@RestController
public class PaymentTest {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	MtnServices mtnServices;

	@RolesAllowed(DEVELOPER)
	@PostMapping("/check/orange/payout")
	public ResponseEntity<?> orangePayout(
			@RequestBody PayoutInformation transactionInfos) {
		String xmlString = orangeMoneyXml(
				  transactionInfos.getAccountNumber(),
				  ""+transactionInfos.getAmountPayment(),
				  transactionInfos.getOtpCode());

		  ResponseEntity<String> response = postRequest(xmlString, BF_ORANGE_MONEY_API_URL);
		  return ResponseEntity.ok(response);
	}



	//@RolesAllowed(ADMIN)
	@PostMapping("/check/coris/get/otp")
	public ResponseEntity<?> corisGetOtp(
			@RequestBody PayoutInformation transactionInfos) {
		final String uri = BF_CORIS_MONEY_API_URL+ "send-code-otp?codePays=226&telephone=70971901";
		  HttpHeaders headers = new HttpHeaders();
		  headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		  headers.set("hashParam", UtilsClass.hashString("22670971901$2a$10$9TEcA9tNAFflUtfNytOGGe6L/zFtvxJm1Hy3NRhw.yrIdkUJBvlVm"));
		  headers.set("clientId", "SWITCHMAKER");
		  HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		  ResponseEntity<String> response = restTemplate.postForEntity(uri, requestEntity, String.class);
		return ResponseEntity.ok(response);
	}



	@RolesAllowed(ADMIN)
	@PostMapping("/check/coris/payout")
	public ResponseEntity<?> corisPayout(
			@RequestBody PayoutInformation transactionInfos) {
		final String uri = BF_CORIS_MONEY_API_URL+ "operations/paiement-bien?codePays=226&telephone=="+transactionInfos.getAccountNumber()+"&codePv=" + BF_CORIS_CODE_PV + "&montant=" + transactionInfos.getAmountPayment()+"&codeOTP="+transactionInfos.getOtpCode();
		  HttpHeaders headers = new HttpHeaders();
		  headers.setContentType(MediaType.APPLICATION_JSON);
		  headers.set("hashParam", UtilsClass.hashString("226") + UtilsClass.hashString(transactionInfos.getAccountNumber()) + UtilsClass.hashString(BF_CORIS_CODE_PV) + UtilsClass.hashString(""+transactionInfos.getAmountPayment()) + UtilsClass.hashString(transactionInfos.getOtpCode()) + UtilsClass.hashString(BF_CORIS_CLIENT_SECRET));
		  headers.set("clientId", BF_CORIS_CLIENT_ID);
		  HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		  ResponseEntity<String> response = restTemplate.postForEntity(uri, requestEntity, String.class);
		return ResponseEntity.ok(response.getBody());
	}



	@RolesAllowed(DEVELOPER)
	@PostMapping("/check/wave/payout")
	public ResponseEntity<?> wavePayout(
			@RequestBody WaveCheckoutSession transactionInfos) {
		final String uri = CI_WAVE_API_URL+ "/v1/checkout/sessions";
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(CI_WAVE_API_KEY);
			HttpEntity<WaveCheckoutSession> entity = new HttpEntity<>(transactionInfos, headers);
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
			return ResponseEntity.ok(response.getBody());
		}catch(HttpClientErrorException e){
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}



	@RolesAllowed(DEVELOPER)
	@PostMapping("/check/wave/session")
	public ResponseEntity<?> waveSession(@RequestBody WaveCheckoutSessionId sessionId) {
		final String uri = CI_WAVE_API_URL+ "/v1/checkout/sessions/"+sessionId.getSessionId();

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(CI_WAVE_API_KEY);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		return ResponseEntity.ok(response.getBody());
	}



	@RolesAllowed(DEVELOPER)
	@PostMapping("/check/mtn/payout")
	public ResponseEntity<?> mtnPayout(@RequestBody MtnRequestToPay request) {
		return ResponseEntity.ok(request);
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

	public String hash256(String input) {
		final String uri = "https://testbed.corismoney.com/external/v1/api/hash256?originalString=" + input;
		 String responseBody = restTemplate.getForObject(uri, String.class);
		return responseBody;
	}
}
