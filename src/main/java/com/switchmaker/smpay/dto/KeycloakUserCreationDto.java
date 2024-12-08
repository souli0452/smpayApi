package com.switchmaker.smpay.dto;

import com.switchmaker.smpay.enumeration.UserAccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@AllArgsConstructor
@Getter
@Setter
public class KeycloakUserCreationDto {
  private String customerId;
  private String firstName;
  private String lastName;
  private String email;
  private UserAccountType accountType;
  private UUID plateformeId;
}
