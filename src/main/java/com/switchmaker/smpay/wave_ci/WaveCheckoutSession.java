package com.switchmaker.smpay.wave_ci;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WaveCheckoutSession {
	private String amount;
	private String currency;
	@JsonProperty("error_url")
	private String errorUrl;
	@JsonProperty("success_url")
	private String successUrl;
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getErrorUrl() {
		return errorUrl;
	}
	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}
	public String getSuccessUrl() {
		return successUrl;
	}
	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}
}
