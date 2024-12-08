package com.switchmaker.smpay.dto;

import java.util.List;

public class TransactionVolume {
	private List<Integer> volume;
	private List<Integer> transaction;
	public List<Integer> getVolume() {
		return volume;
	}
	public void setVolume(List<Integer> volume) {
		this.volume = volume;
	}
	public List<Integer> getTransaction() {
		return transaction;
	}
	public void setTransaction(List<Integer> transaction) {
		this.transaction = transaction;
	}
}
