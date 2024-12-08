package com.switchmaker.smpay.orange_bf;

import com.switchmaker.smpay.dto.ValidPaymentInformations;
import org.springframework.http.ResponseEntity;

public interface OrangeServices {
	public ResponseEntity<?> payment(ValidPaymentInformations validPaymentInformation);
}
