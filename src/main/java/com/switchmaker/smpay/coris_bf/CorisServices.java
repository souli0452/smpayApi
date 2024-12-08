package com.switchmaker.smpay.coris_bf;

import com.switchmaker.smpay.dto.ValidPaymentInformations;
import org.springframework.http.ResponseEntity;

public interface CorisServices {
	public ResponseEntity<?> paymentOtp(ValidPaymentInformations validPaymentInformations);
	public ResponseEntity<?> paymentAccountDebited(ValidPaymentInformations validPaymentInformations);
}
