package com.switchmaker.smpay.dto;

import com.switchmaker.smpay.entities.Payment;

import java.util.List;

public class TransactionPageable {
	private int totalPage;
	private int numberOfElements;
	private int totalRecords;
	private List<Payment> transaction;
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getNumberOfElements() {
		return numberOfElements;
	}
	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public List<Payment> getTransaction() {
		return transaction;
	}
	public void setTransaction(List<Payment> transaction) {
		this.transaction = transaction;
	}
}
