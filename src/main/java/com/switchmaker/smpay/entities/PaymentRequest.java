package com.switchmaker.smpay.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.switchmaker.smpay.abstractClass.Identifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="payment_request")
public class PaymentRequest extends Identifier{
	private int amount;
	@Column(name="payment_type")
	@JsonProperty("payment_type")
	private String paymentType;
	@Column(name="payment_account")
	@JsonProperty("payment_account")
	private String paymentAccount;
	@Column(name="sending_date")
	@JsonProperty("sending_date")
	private LocalDateTime sendingDate;
	@Column(name="processing_date")
	@JsonProperty("processing_date")
	private LocalDateTime processingDate;

	@Column(name="transfer_date")
	@JsonProperty("transfer_date")
	private String transferDate;

	@Column(name="Transfer_time")
	@JsonProperty("Transfer_time")
	private String transferTime;

	@JsonProperty("success_date")
	@Column(name = "success_date")
	private LocalDateTime successDate;

	@Column(name="transaction_number")
	@JsonProperty("transaction_number")
	private String transactionNumber;
	private String status;
	private String reason;

	@JsonProperty("home_structure")
	@Column(name = "home_structure")
	private String homeStructure;
//	private boolean isUserDeleted;
//	private boolean isDeleted;
	@OneToOne
	private Platform platform;
	@OneToOne
	private User user;
}
