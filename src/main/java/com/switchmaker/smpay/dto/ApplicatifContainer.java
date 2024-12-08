package com.switchmaker.smpay.dto;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class ApplicatifContainer {

    private int numberOfcurrentRequestsAndInitiateOfApplicationAccount;

    private int numberOfSuccessfulRequestsApplicationAccount;

}
