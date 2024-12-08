package com.switchmaker.smpay.keycloak;

import java.util.ArrayList;
import java.util.List;

public class KeycloakUser {
	private int createdTimestamp = 1;
	private String username;
	private boolean enabled = true;
	private boolean totp;
	private boolean emailVerified;
	private String firstName;
	private String lastName;
	private String email;
	private List<?> disableableCredentialTypes = new ArrayList<>();
	private List<?> requiredActions = new ArrayList<>();
	private int notBefore = 0;
	private KeycloackAccess access = new KeycloackAccess();
	private List<KeycloakPasswordReset> credentials;
	private List<String> realmRoles;
	public int getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(int createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isTotp() {
		return totp;
	}
	public void setTotp(boolean totp) {
		this.totp = totp;
	}
	public boolean isEmailVerified() {
		return emailVerified;
	}
	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<?> getDisableableCredentialTypes() {
		return disableableCredentialTypes;
	}
	public void setDisableableCredentialTypes(List<?> disableableCredentialTypes) {
		this.disableableCredentialTypes = disableableCredentialTypes;
	}
	public List<?> getRequiredActions() {
		return requiredActions;
	}
	public void setRequiredActions(List<?> requiredActions) {
		this.requiredActions = requiredActions;
	}
	public int getNotBefore() {
		return notBefore;
	}
	public void setNotBefore(int notBefore) {
		this.notBefore = notBefore;
	}
	public KeycloackAccess getAccess() {
		return access;
	}
	public void setAccess(KeycloackAccess access) {
		this.access = access;
	}
	public List<KeycloakPasswordReset> getCredentials() {
		return credentials;
	}
	public void setCredentials(List<KeycloakPasswordReset> credentials) {
		this.credentials = credentials;
	}
	public List<String> getRealmRoles() {
		return realmRoles;
	}
	public void setRealmRoles(List<String> realmRoles) {
		this.realmRoles = realmRoles;
	}
}
