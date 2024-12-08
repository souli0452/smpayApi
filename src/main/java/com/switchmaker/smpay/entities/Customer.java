package com.switchmaker.smpay.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.switchmaker.smpay.abstractClass.Identifier;
import com.switchmaker.smpay.enumeration.AccountType;
import com.switchmaker.smpay.enumeration.ClientType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Entity
@Table(name="costomer")
public class Customer extends Identifier{
  private ClientType clientType;
  private String name;
  private String firstName;
  private String countryOfResidence;
  private String cnibOrPassport;
  private String establishmentDate;
  private String establishmentPlace;
  private String expirationDate;
  private String identityDocument;
  private String companyName;
  private String socialReason;
  private String rccm;
  private String ifu;
  private String companyContact;
  private String companyAddress;
  private String companyDomiciledCountry;
  private String companyLogo;
  private String customerAccountNumberForPayment;
  private AccountType customerAccountTypeForPayment;
  private String domiciliaryAccountStructure;
	private String denomination;
	private String address;
	private String town;
	@Column(unique = true)
	private String email;
	private String phone;
	@Column(name="creation_date")
	@JsonProperty("creation_date")
	private LocalDateTime creationDate;
	private boolean status = true;
	private boolean passwordChange;

	@OneToMany(mappedBy="costomer")
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private Collection<Notification> notification;
	@OneToMany(mappedBy="costomer")
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private Collection<CostomerRate> costomerRate;

}

