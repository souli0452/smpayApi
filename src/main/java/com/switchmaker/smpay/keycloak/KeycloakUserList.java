package com.switchmaker.smpay.keycloak;

import java.util.List;
import java.util.UUID;

public class KeycloakUserList {
	private UUID id;
	private Long createdTimestamp;
	private String username;
	private boolean enabled;
	private boolean totp;
	private boolean emailVerified;
	private String firstName;
	private String lastName;
	private String email;
	private List<?> disableableCredentialTypes;
	private List<?> requiredActions;
	private int notBefore;
	private KeycloackAccess access;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public Long getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(Long createdTimestamp) {
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
}
