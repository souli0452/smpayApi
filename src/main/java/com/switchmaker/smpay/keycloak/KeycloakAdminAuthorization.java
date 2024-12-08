package com.switchmaker.smpay.keycloak;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.ADMIN_ID;
import static com.switchmaker.smpay.keycloak.KeycloakConstantValues.*;
import static com.switchmaker.smpay.keycloak.KeycloakUrls.KEYCLOAK_ADMIN_AUTHORIZATION;

public class KeycloakAdminAuthorization {

	/*------------------------------------/
	/*        Admin access token         /
	/*---------------------------------*/

	public static String getAdminToken(){
		try {
			RestTemplate restTemplate = new RestTemplate();
		   	HttpHeaders headers = new HttpHeaders();
		   	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			  map.add("client_id", ADMIN_ID);
			  map.add("username", ADMIN_LOGIN);
			  map.add("password", ADMIN_PASSWORD);
			  map.add("grant_type", GRANT_TYPE);
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map,headers);
			  ResponseEntity<String> response = restTemplate.exchange(KEYCLOAK_ADMIN_AUTHORIZATION,HttpMethod.POST, entity, String.class);
			  if (response.getStatusCodeValue() == 200) {
				  ObjectMapper mapper = new ObjectMapper();
				  JsonNode actualObj = mapper.readTree(response.getBody());
				  String token =  actualObj.get("access_token").asText();
				  return token;
			  }

		}catch (Exception e) {
			return "error";
		}
		return "error";
	}

}
