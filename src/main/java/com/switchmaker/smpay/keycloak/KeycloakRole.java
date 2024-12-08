package com.switchmaker.smpay.keycloak;

public class KeycloakRole {
	private String id = "04e1ca7c-1747-4fe2-b6bb-3995fec164fc";
	private String name = "app-application";
	private boolean composite;
	private boolean clientRole;
	private String containerId = "switch-maker";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isComposite() {
		return composite;
	}
	public void setComposite(boolean composite) {
		this.composite = composite;
	}
	public boolean isClientRole() {
		return clientRole;
	}
	public void setClientRole(boolean clientRole) {
		this.clientRole = clientRole;
	}
	public String getContainerId() {
		return containerId;
	}
	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}
}
