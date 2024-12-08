package com.switchmaker.smpay.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.switchmaker.smpay.abstractClass.Identifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment extends Identifier {
	@Column(unique = true, name="payment_code")
	@JsonProperty("payment_code")
	private String paymentCode;
	@Column(name="account_number")
	@JsonProperty("account_number")
	private String accountNumber;
	@Column(name="payment_origin")
	@JsonProperty("payment_origin")
	private String paymentOrigin;
	@Column(name="payment_type")
	@JsonProperty("payment_type")
	private String paymentType;
	@Column(name="service_code")
	@JsonProperty("service_code")
	private String serviceCode;
	@com.sun.istack.NotNull
	@Column(name="payment_amount")
	@JsonProperty("payment_amount")
	private int paymentAmount = 0;
	@com.sun.istack.NotNull
	@Column(name="payment_date")
	@JsonProperty("payment_date")
	private LocalDateTime paymentDate;
	@Column(name="commission_amount")
	@JsonProperty("commission_amount")
	private int commissionAmount = 0;
	@Column(name="rate_applied")
	@JsonProperty("rate_applied")
	private float rateApplied;
	@Column(name="real_amount")
	@JsonProperty("real_amount")
	private int realAmount;
	private String currency;
	private String status;
	@OneToOne
	private Platform platform;
}

