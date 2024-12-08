package com.switchmaker.smpay.dto;

import com.switchmaker.smpay.entities.Platform;
import com.switchmaker.smpay.payout.PayoutInformation;
import lombok.Data;

@Data
public class ValidPaymentInformations {
	private PayoutInformation payoutInformation;
	private Platform platform;
	private String paymentType;
	private float rateApplied;

}
