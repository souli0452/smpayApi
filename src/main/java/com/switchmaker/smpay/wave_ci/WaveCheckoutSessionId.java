package com.switchmaker.smpay.wave_ci;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WaveCheckoutSessionId {
	@JsonProperty("session_id")
	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
