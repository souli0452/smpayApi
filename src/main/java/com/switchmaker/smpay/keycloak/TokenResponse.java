package com.switchmaker.smpay.keycloak;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.switchmaker.smpay.dto.Token;
import com.switchmaker.smpay.entities.User;

import java.time.LocalDateTime;

import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.SUCCESS_CODE;

public class TokenResponse {

	public static Token accessToken(String apiTokenResponse, User user) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
        KeycloakTokenResponse keycloakTokenResponse = objectMapper.readValue(apiTokenResponse, KeycloakTokenResponse.class);
		Token token = new Token();
		token.setCode(SUCCESS_CODE);
    token.setUser(user);
		token.setAccessToken(keycloakTokenResponse.getAccessToken());
		token.setTokenExpired(keycloakTokenResponse.getExpiresIn());
		token.setTokenType(keycloakTokenResponse.getTokenType());
		token.setGenerationDate(LocalDateTime.now());
		return token;
	}

}
