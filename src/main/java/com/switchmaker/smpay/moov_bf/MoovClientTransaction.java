package com.switchmaker.smpay.moov_bf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class MoovClientTransaction {
	@JsonProperty("request-id")
	private String requestId;
	private String destination;
	private int amount;
	private String remarks;
	private String message;
	@JsonProperty("extended-data")
	private MoovExtendedData data;
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public MoovExtendedData getData() {
		return data;
	}
	public void setData(MoovExtendedData data) {
		this.data = data;
	}
	
}
