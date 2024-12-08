package com.switchmaker.smpay.services;

import com.switchmaker.smpay.dto.PaymentRequestDtoTransfert;
import com.switchmaker.smpay.entities.PaymentRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PaymentRequestService {
	//public PaymentRequest sendPaymentRequest(Request request);
	//public PaymentRequest validatePaymentRequest(UUID id);
	//public PaymentRequest rejectPaymentRequest(UUID id);
	//public List<PaymentRequest> getCostomerPaymentRequestByCostomerId(UUID costomerID, int limit, int offset);
	//public List<PaymentRequest> getCostomerPaymentRequestByCostomerIdAndStatus(UUID costomerID, String status, int limit, int offset);
	//public List<PaymentRequest> getAllPaymentRequestByStatus(String status, int limit, int offset);
	//public List<PaymentRequest> getAllPaymentRequestByPeriodAndStatus(String status, String startDate, String endDate);

	public PaymentRequest validateChangeStatutPaymentRequest(UUID requestId, PaymentRequestDtoTransfert PaymentRequestDtoTransfert);

	public ResponseEntity cancelPaymentRequest(UUID requestId);

	public ResponseEntity deleteInitiedPaymentRequest(UUID requestId);

	public ResponseEntity getPaymentRequestById(UUID requestId);

	public List<PaymentRequest> getAllPaymentRequests();

	List<PaymentRequest> getAllPaymentRequestsInitieProcessing();

	public List<PaymentRequest> getAllCanceledPaymentRequests();

	public List<PaymentRequest> getAllValidatedPaymentRequests();

	public List<PaymentRequest> getPaymentRequestsByUser(UUID userId);

	List<PaymentRequest> getPaymentRequestsByStatus(String status);

}
