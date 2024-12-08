package com.switchmaker.smpay.dto;

import com.switchmaker.smpay.entities.Payment;
import com.switchmaker.smpay.entities.Platform;

import java.util.Date;
import java.util.List;

public class PlatformTransactions {
	private Platform platform;
	private List<Payment> transaction;
	private Date date;
	public Platform getPlatform() {
		return platform;
	}
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
	public List<Payment> getTransaction() {
		return transaction;
	}
	public void setTransaction(List<Payment> transaction) {
		this.transaction = transaction;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public PlatformTransactions() {
		super();
	}

}
