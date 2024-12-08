package com.switchmaker.smpay.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.switchmaker.smpay.abstractClass.Identifier;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Entity
@Table(name = "platform")
public class Platform extends Identifier{
	@Column(name="platform_code", unique = true)
	@JsonProperty("platform_code")
	private String platformCode;
	@Column(name="platform_name")
	@JsonProperty("platform_name")
	private String platformName;
//	@Column(unique = true)
//	private String email;
	@Column(name="payment_account")
	@JsonProperty("payment_account")
	private String paymentAccount;
	private int balance = 0;
	@Column(name="balance_modification_date")
//	@JsonProperty("balance_modification_date")
//	private LocalDateTime balanceModificationDate;
//	@Column(name="creation_date")
	@JsonProperty("creation_date")
	private LocalDateTime creationDate;
//	@Column(name="account_creation_date")
//	@JsonProperty("account_creation_date")
//	private LocalDateTime accountCreationDate;
	private boolean authorize;
	@JsonProperty("home_structure")
	@Column(name = "home_structure")
	private String homeStructure;

	@JsonProperty("payment_account_type")
	@Column(name = "payment_account_type")
	private String paymentAccountType;

	@ManyToOne
	private User user;
	@OneToMany(mappedBy="platform")
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private Collection<Payment> payment;
	@ManyToMany()
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private Collection<User> users;
	@OneToMany(mappedBy="platform")
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private Collection<PaymentRequest> paymentRequest;


}
