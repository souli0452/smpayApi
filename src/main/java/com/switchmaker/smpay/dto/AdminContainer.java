package com.switchmaker.smpay.dto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AdminContainer {
  private int totalNumberOfPlatform;
  private int totalNumberOfCustomer;
  private int totalNumberOfNewPaymentRequest;
  private int totalNumberOfMessage;
}
