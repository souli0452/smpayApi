package com.switchmaker.smpay.wave_ci;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "event")
public class EventEntity {
	@Id
	private String id;
    private String amount;
    private String checkout_status;
    private String client_reference;
    private String currency;
    private String error_url;
    private String payment_status;
    private String success_url;
    private String wave_launch_url;
    private String when_completed;
    private String when_created;
    private String when_expires;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCheckout_status() {
		return checkout_status;
	}
	public void setCheckout_status(String checkout_status) {
		this.checkout_status = checkout_status;
	}
	public String getClient_reference() {
		return client_reference;
	}
	public void setClient_reference(String client_reference) {
		this.client_reference = client_reference;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getError_url() {
		return error_url;
	}
	public void setError_url(String error_url) {
		this.error_url = error_url;
	}
	public String getPayment_status() {
		return payment_status;
	}
	public void setPayment_status(String payment_status) {
		this.payment_status = payment_status;
	}
	public String getSuccess_url() {
		return success_url;
	}
	public void setSuccess_url(String success_url) {
		this.success_url = success_url;
	}
	public String getWave_launch_url() {
		return wave_launch_url;
	}
	public void setWave_launch_url(String wave_launch_url) {
		this.wave_launch_url = wave_launch_url;
	}
	public String getWhen_completed() {
		return when_completed;
	}
	public void setWhen_completed(String when_completed) {
		this.when_completed = when_completed;
	}
	public String getWhen_created() {
		return when_created;
	}
	public void setWhen_created(String when_created) {
		this.when_created = when_created;
	}
	public String getWhen_expires() {
		return when_expires;
	}
	public void setWhen_expires(String when_expires) {
		this.when_expires = when_expires;
	}
	
}
