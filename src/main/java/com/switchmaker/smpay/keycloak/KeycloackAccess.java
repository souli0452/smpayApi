package com.switchmaker.smpay.keycloak;

import lombok.Data;

@Data
public class KeycloackAccess {
	private boolean manageGroupMembership = true;
	private boolean view = true;
	private boolean mapRoles = true;
	private boolean impersonate = true;
	private boolean manage = true;
}
