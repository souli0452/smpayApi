package com.switchmaker.smpay.dto;

import com.switchmaker.smpay.enumeration.AccountType;
import com.switchmaker.smpay.enumeration.ClientType;
import io.micrometer.core.lang.Nullable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.*;

@Getter
@Setter
public class CustomerDto {
  private String name;
  private String firstName;
  private String countryOfResidence;
  private String cnibOrPassport;
  private String establishmentDate;
  private ClientType clientType;
  private String establishmentPlace;
  private String denomination;
  private String expirationDate;
  @Column(columnDefinition = "TEXT")
  @Nullable
  private String identityDocumentbase;
  private String companyName;
  private String rccm;
  private String ifu;
  private String socialReason;
  private String companyContact;
  private String companyAddress;
  private String companyDomiciledCountry;
  private String domiciliaryAccountStructure;
  @Column(columnDefinition = "TEXT")
  private String companyLogobase;
  private String customerAccountNumberForPayment;
  private AccountType customerAccountTypeForPayment;
  private String address;
  private String town;
  private String email;
  private String phone;
  public List<Map<String, String>> validateFields() {
    List<Map<String, String>> errors = new ArrayList<>();
    for (Field field : this.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      try {
        if (field.get(this) == null) {
          Map<String, String> error = new HashMap<>();
          error.put("message", "Le champ " + field.getName() + " est obligatoire.");
          errors.add(error);
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return errors;
  }

  public List<Map<String, String>> validateSelectedFields() {
    List<Map<String, String>> errors = new ArrayList<>();
    List<String> fieldsToCheck = Arrays.asList("name", "firstName", "countryOfResidence", "cnibOrPassport", "establishmentDate", "establishmentPlace", "expirationDate", "identityDocumentbase", "address", "town", "email", "phone");

    for (String fieldName : fieldsToCheck) {
      try {
        Field field = this.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        if (field.get(this) == null) {
          Map<String, String> error = new HashMap<>();
          error.put("message", "Le champ " + fieldName + " est obligatoire.");
          errors.add(error);
        }
      } catch (NoSuchFieldException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return errors;
  }
}
