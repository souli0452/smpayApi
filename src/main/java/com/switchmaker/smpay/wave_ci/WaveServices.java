package com.switchmaker.smpay.wave_ci;

import com.switchmaker.smpay.dto.ValidPaymentInformations;
import org.springframework.http.ResponseEntity;

public interface WaveServices {
	public ResponseEntity<?> clientPayment(ValidPaymentInformations validPaymentInformations);
	public String paymentStatus(String paymentId);
}
