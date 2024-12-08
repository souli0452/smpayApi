package com.switchmaker.smpay.payout;
import lombok.Data;

@Data
public class PayoutInformation {
	private String platformKey;
	private String accountNumber;
	private String otpCode;
	private int amountPayment;
	private String countryCode;
	private String serviceCode;
	private String currency;
	private String errorUrl;
	private String successUrl;
	private String requestId;
}
