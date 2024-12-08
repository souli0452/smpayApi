package com.switchmaker.smpay.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {
	private String platformKey;
	private String paymentCode;
	private String accountNumber;
	private String paymentOrigin;
	private String paymentType;
	private String serviceCode;
	private int paymentAmount;
	private LocalDateTime paymentDate;
	private int commissionAmount;
	private float rateApplied;
	private int realAmount;
	private String currency;
	private String paymentStatus;

}
