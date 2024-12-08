package com.switchmaker.smpay.keycloak;
import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;

public class KeycloakUrls {
	public static final String GET_CLIENTS_FROM_KEYCLOAK = ROOT_API+"/keycloak/clients/get";
	public static final String KEYCLOAK_ADMIN_AUTHORIZATION =  "http://localhost:8080/auth/realms/master/protocol/openid-connect/token";//local
	//public static final String KEYCLOAK_ADMIN_AUTHORIZATION =  "https://authsmpay.switch-maker.com/auth/realms/master/protocol/openid-connect/token";
	public static final String KEYCLOAK_CLIENT_AUTHORIZATION = "http://localhost:8080/auth/realms/switch-maker/protocol/openid-connect/token";//local
	//public static final String KEYCLOAK_CLIENT_AUTHORIZATION = "https://authsmpay.switch-maker.com/auth/realms/switch-maker/protocol/openid-connect/token";
	public static final String KEYCLOAK_RESET_PASSWORD = "http://localhost:8080/auth/admin/realms/switch-maker/users/"; //local
	//public static final String KEYCLOAK_RESET_PASSWORD = "https://authsmpay.switch-maker.com/auth/admin/realms/switch-maker/users/";
	public static final String KEYCLOAK_USERS_URL = "http://localhost:8080/auth/admin/realms/switch-maker/users"; //local
	//public static final String KEYCLOAK_CREATE_USER = "https://authsmpay.switch-maker.com/auth/admin/realms/switch-maker/users";
  public static final String KEYCLOAK_CLIEENT_ADMIN_URL = "http://localhost:8080/auth";
  public static final String KEYCLOAK_CLIENT_REALM = "switch-maker";
  public static final String KEYCLOAK_CLIENT_ID = "smpay";

  public static final String KEYCLOAK_ADMIN_USERNAME = "admin";
  public static final String KEYCLOAK_CLIENT_SECRET="9d3dbffb-071a-48bd-a254-e4227c1ddc09";
}
