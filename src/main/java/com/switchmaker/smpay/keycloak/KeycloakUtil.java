package com.switchmaker.smpay.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import static com.switchmaker.smpay.keycloak.KeycloakUrls.*;

public class KeycloakUtil {

  public static Keycloak getKeycloakInstance(String token) {
    return KeycloakBuilder.builder()
      .serverUrl(KEYCLOAK_CLIEENT_ADMIN_URL)
      .realm(KEYCLOAK_CLIENT_REALM)
      .clientId(KEYCLOAK_CLIENT_ID)
      .username(KEYCLOAK_ADMIN_USERNAME)
      .authorization(token)
      .build();
  }


  public static Keycloak getKeycloakInstanceToAuth() {
    return KeycloakBuilder.builder()
        .serverUrl(KEYCLOAK_CLIEENT_ADMIN_URL)
        .realm("admin-cli")
        .clientId("smpay")
      .grantType("client_credentials")
        .clientSecret(KEYCLOAK_CLIENT_SECRET)
        .build();
  }
}
