package com.switchmaker.smpay.services;

import com.switchmaker.smpay.dto.ResponseMessage;
import com.switchmaker.smpay.payout.PayoutInformation;

public interface CheckingPaymentInformationService {
	public ResponseMessage checkingPaymentInformation(PayoutInformation payoutInfos);
}
