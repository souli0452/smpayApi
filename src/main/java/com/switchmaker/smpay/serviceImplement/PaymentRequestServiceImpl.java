package com.switchmaker.smpay.serviceImplement;

import com.switchmaker.smpay.dto.PaymentRequestDtoTransfert;
import com.switchmaker.smpay.entities.PaymentRequest;
import com.switchmaker.smpay.entities.Platform;
import com.switchmaker.smpay.entities.ValidateCode;
import com.switchmaker.smpay.repository.CodeRepository;
import com.switchmaker.smpay.repository.PaymentRequestRepository;
import com.switchmaker.smpay.repository.PlatformRepository;
import com.switchmaker.smpay.services.PaymentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;

@Service
public class PaymentRequestServiceImpl implements PaymentRequestService {

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    CodeRepository codeRepository;

  /*  @Override
    public PaymentRequest sendPaymentRequest(Request request) {
        return null;
    }*/
/*
    @Override
    public PaymentRequest validatePaymentRequest(UUID id) {
        return null;
    }

    @Override
    public PaymentRequest rejectPaymentRequest(UUID id) {
        return null;
    }

    @Override
    public List<PaymentRequest> getCostomerPaymentRequestByCostomerId(UUID costomerID, int limit, int offset) {
        return null;
    }

    @Override
    public List<PaymentRequest> getCostomerPaymentRequestByCostomerIdAndStatus(UUID costomerID, String status, int limit, int offset) {
        return null;
    }

    @Override
    public List<PaymentRequest> getAllPaymentRequestByStatus(String status, int limit, int offset) {
        return null;
    }

    @Override
    public List<PaymentRequest> getAllPaymentRequestByPeriodAndStatus(String status, String startDate, String endDate) {
        return null;
    }*/

    @Override
    public PaymentRequest validateChangeStatutPaymentRequest(UUID requestId, PaymentRequestDtoTransfert paymentRequestDtoTransfert) {

                Optional<PaymentRequest> optionalRequest = paymentRequestRepository.findById(requestId);
                PaymentRequest paymentRequest1=new PaymentRequest();
            ValidateCode validateCode = codeRepository.findByToken(paymentRequestDtoTransfert.getCode());
            if (validateCode != null) {
                Calendar cal = Calendar.getInstance();
                if ((validateCode.getExpiryDate().getTime() - cal.getTime().getTime()) >= 0) {
                    codeRepository.delete(validateCode);


                    if (optionalRequest.isPresent()) {
                        PaymentRequest paymentRequest = optionalRequest.get();

                        if (paymentRequest.getStatus().equals(INITIATE)) {
                            paymentRequest.setStatus(PROCESSING);
                            paymentRequest.setProcessingDate(LocalDateTime.now());

                        } else if (paymentRequest.getStatus().equals(PROCESSING)) {
                            paymentRequest.setStatus(SUCCESS);
                            paymentRequest.setTransactionNumber(paymentRequestDtoTransfert.getTransactionNumber());
                            paymentRequest.setTransferDate(paymentRequestDtoTransfert.getTransferDate());
                            paymentRequest.setTransferTime(paymentRequestDtoTransfert.getTransferTime());
                            paymentRequest.setSuccessDate(LocalDateTime.now());

                        } else {
                            throw new IllegalStateException("\"La demande est déjà traitéé\"");
                        }

                        paymentRequest1= paymentRequestRepository.save(paymentRequest);
                    } else {
                        throw new NoSuchElementException("Demande non trouvée");
                    }

                }
            }


        return paymentRequest1;
    }

    @Override
    public ResponseEntity<?> cancelPaymentRequest(UUID requestId) {
        Optional<PaymentRequest> optionalRequest = paymentRequestRepository.findById(requestId);

        if (optionalRequest.isPresent()) {
            PaymentRequest paymentRequest = optionalRequest.get();

            if (paymentRequest.getStatus().equals(INITIATE)) {
                // Si la demande est à l'état "Initiate", vous pouvez l'annuler
                paymentRequest.setStatus(CANCELED);
                paymentRequestRepository.save(paymentRequest);
                return ResponseEntity.status(HttpStatus.OK).body("La demande a été annulée.");
            } else {
                // Si la demande n'est pas à l'état "Initiate", retournez une réponse appropriée
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La demande ne peut pas être annulée car elle n'est pas à l'état INITIATE.");
            }
        } else {
            // Si la demande n'est pas trouvée, retournez une réponse appropriée
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande non trouvée");
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> deleteInitiedPaymentRequest(UUID requestId) {
        Optional<PaymentRequest> optionalRequest = paymentRequestRepository.findById(requestId);

        if (optionalRequest.isPresent()) {
            PaymentRequest paymentRequest = optionalRequest.get();

            if (paymentRequest.getStatus().equals(INITIATE)) {
                Platform platform = paymentRequest.getPlatform();
                platform.setBalance(platform.getBalance() + paymentRequest.getAmount());
                platformRepository.save(platform);
                // Si la demande est à l'état "annulée", vous pouvez la supprimer
                paymentRequestRepository.delete(paymentRequest);

                return ResponseEntity.status(HttpStatus.OK).body("La demande été supprimée.");
            } else {
                // Si la demande n'est pas à l'état "CANCELED", retournez une réponse appropriée
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La demande ne peut pas être supprimée car elle n'est pas à l'état initié.");
            }
        } else {
            // Si la demande n'est pas trouvée, retournez une réponse appropriée
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande non trouvée");
        }
    }

    @Override
    public ResponseEntity<?> getPaymentRequestById(UUID requestId) {
        Optional<PaymentRequest> optionalRequest = paymentRequestRepository.findById(requestId);

        if (optionalRequest.isPresent()) {
            PaymentRequest paymentRequest = optionalRequest.get();
            return ResponseEntity.status(HttpStatus.OK).body(paymentRequest);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande non trouvée");
        }
    }

    @Override
    public List<PaymentRequest> getAllPaymentRequests() {
        return paymentRequestRepository.findAll();
    }

    @Override
    public List<PaymentRequest> getAllPaymentRequestsInitieProcessing() {
        return getAllPaymentRequests().stream()
                .filter(p -> p.getStatus().equalsIgnoreCase(INITIATE) || p.getStatus().equalsIgnoreCase(PROCESSING))
                .sorted((p1, p2) -> p2.getSendingDate().compareTo(p1.getSendingDate())) // Tri par date de création décroissante
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentRequest> getAllCanceledPaymentRequests() {
        return paymentRequestRepository.findByStatus(CANCELED);
    }

    @Override
    public List<PaymentRequest> getAllValidatedPaymentRequests() {
        return paymentRequestRepository.findByStatus(SUCCESS);
    }

    @Override
    public List<PaymentRequest> getPaymentRequestsByUser(UUID userId) {
        return paymentRequestRepository.userId(userId);
    }

    @Override
        public List<PaymentRequest> getPaymentRequestsByStatus(String status) {
            return paymentRequestRepository.findByStatus(status);
        }




}
