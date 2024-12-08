package com.switchmaker.smpay.moov_bf;

import com.switchmaker.smpay.dto.ValidPaymentInformations;
import com.switchmaker.smpay.payout.PayoutInformation;
import org.springframework.http.ResponseEntity;

public interface MoovServices {
	public ResponseEntity<?> clientPayment(ValidPaymentInformations validPaymentInformation);
	public ResponseEntity<?> merchandPayment(PayoutInformation transactionInfos);
	public String paymentStatus(String paymentId);
}
