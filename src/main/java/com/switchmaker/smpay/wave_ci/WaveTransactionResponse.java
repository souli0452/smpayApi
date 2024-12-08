package com.switchmaker.smpay.wave_ci;

import java.time.LocalDateTime;

public class WaveTransactionResponse {
	private String code;
	private String transactionCode;
	private String clientPhoneNumber;
	private int transactionAmount;
	private String transactionType;
	private String transactionOrigin;
	private LocalDateTime transactionDate;
	private String status;
	private String errorUrl;
	private String succesUrl;
	private String waveLaunchUrl;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getClientPhoneNumber() {
		return clientPhoneNumber;
	}
	public void setClientPhoneNumber(String clientPhoneNumber) {
		this.clientPhoneNumber = clientPhoneNumber;
	}
	public int getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(int transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getTransactionOrigin() {
		return transactionOrigin;
	}
	public void setTransactionOrigin(String transactionOrigin) {
		this.transactionOrigin = transactionOrigin;
	}
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorUrl() {
		return errorUrl;
	}
	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}
	public String getSuccesUrl() {
		return succesUrl;
	}
	public void setSuccesUrl(String succesUrl) {
		this.succesUrl = succesUrl;
	}
	public String getWaveLaunchUrl() {
		return waveLaunchUrl;
	}
	public void setWaveLaunchUrl(String waveLaunchUrl) {
		this.waveLaunchUrl = waveLaunchUrl;
	}
	public WaveTransactionResponse(String code, String transactionCode, String clientPhoneNumber, int transactionAmount,
			String transactionType, String transactionOrigin, LocalDateTime transactionDate, String status, String errorUrl, String succesUrl,
			String waveLaunchUrl) {
		this.code = code;
		this.transactionCode = transactionCode;
		this.clientPhoneNumber = clientPhoneNumber;
		this.transactionAmount = transactionAmount;
		this.transactionType = transactionType;
		this.transactionOrigin = transactionOrigin;
		this.transactionDate = transactionDate;
		this.status = status;
		this.errorUrl = errorUrl;
		this.succesUrl = succesUrl;
		this.waveLaunchUrl = waveLaunchUrl;
	}
}
