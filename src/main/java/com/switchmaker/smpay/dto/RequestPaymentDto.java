package com.switchmaker.smpay.dto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RequestPaymentDto {

    private String code;
    private int amount;
    private String paymentType;
    private String paymentAccount;
    private LocalDateTime sendingDate;
    private LocalDateTime processingDate;
    private String transactionNumber;
    private String status;
    private String reason;
    //	private boolean isUserDeleted;
    //	private boolean isDeleted;
    private UUID platformId;
    private UUID userId;
}
