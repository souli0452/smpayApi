package com.switchmaker.smpay.dto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SupervisorContainer {
  private int totalNumberOfPlatform;
  private int totalNumberOfCApplicationAccount;
  private int totalNumberOfInitiateOrProcessingPaymentRequest;
  private int totalNumberOfSuccesPaymentRequest;
}
