package com.switchmaker.smpay.dto;

import com.switchmaker.smpay.entities.PaymentRequest;

public class Request {
	private String code;
	private PaymentRequest request;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public PaymentRequest getRequest() {
		return request;
	}
	public void setRequest(PaymentRequest request) {
		this.request = request;
	}
}
