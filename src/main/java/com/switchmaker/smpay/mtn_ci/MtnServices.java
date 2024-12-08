package com.switchmaker.smpay.mtn_ci;

import com.switchmaker.smpay.dto.ValidPaymentInformations;
import org.springframework.http.ResponseEntity;

public interface MtnServices {
	public String getToken();
	public ResponseEntity<?> clientPayment(ValidPaymentInformations validPaymentInformations);
	public String paymentStatus(String transactionId);
}
