package com.switchmaker.smpay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentRequestDtoTransfert {

    private String transferDate;
    private String transferTime;
    private String transactionNumber;
    private String code;
}
