package com.switchmaker.smpay.constant.urls;
import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;

public class AuthorizationUrls {
	public static final String DEVELOPER_ACCESS_TOKEN = ROOT_API+"/access/token";
	public static final String USER_ACCESS_TOKEN = ROOT_API+"/user/access/token";
  public static final String ADMIN_ACCESS_TOKEN  = ROOT_API+"/admin/access/token";
	public static final String USER_RESET_PASSWORD = ROOT_API+"/user/{id}/reset/password";
	public static final String USER_CHANGE_PASSWORD = ROOT_API+"/user/{id}/change/password";
	public static final String FORGOT_PASSWORD = ROOT_API+"/forgot/password/email/{email}";
	public static final String SEND_CODE = ROOT_API+"/code/{code}";
}
