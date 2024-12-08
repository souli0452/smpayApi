package com.switchmaker.smpay.dto;

import java.util.List;

public class ChartData {
	private List<Double> amountTransaction;
	private List<Double> solde;
	private List<Double> versement;
	private List<Integer> initiateTransaction;
	private List<Integer> failureTransaction;
	private List<Integer> successTransaction;
	private String amountYearTransaction = "0";
	private String amountYearSolde = "0";
	public String getAmountYearSolde() {
		return amountYearSolde;
	}
	public void setAmountYearSolde(String amountYearSolde) {
		this.amountYearSolde = amountYearSolde;
	}
	private String amountYearVersement = "0";
	private int transactionCount = 0;
	
	public List<Double> getAmountTransaction() {
		return amountTransaction;
	}
	public void setAmountTransaction(List<Double> amountTransaction) {
		this.amountTransaction = amountTransaction;
	}
	public List<Double> getSolde() {
		return solde;
	}
	public void setSolde(List<Double> solde) {
		this.solde = solde;
	}
	public List<Double> getVersement() {
		return versement;
	}
	public void setVersement(List<Double> versement) {
		this.versement = versement;
	}
	public List<Integer> getInitiateTransaction() {
		return initiateTransaction;
	}
	public void setInitiateTransaction(List<Integer> initiateTransaction) {
		this.initiateTransaction = initiateTransaction;
	}
	public List<Integer> getFailureTransaction() {
		return failureTransaction;
	}
	public void setFailureTransaction(List<Integer> failureTransaction) {
		this.failureTransaction = failureTransaction;
	}
	public List<Integer> getSuccessTransaction() {
		return successTransaction;
	}
	public void setSuccessTransaction(List<Integer> successTransaction) {
		this.successTransaction = successTransaction;
	}
	public String getAmountYearTransaction() {
		return amountYearTransaction;
	}
	public void setAmountYearTransaction(String amountYearTransaction) {
		this.amountYearTransaction = amountYearTransaction;
	}
	public String getAmountYearVersement() {
		return amountYearVersement;
	}
	public void setAmountYearVersement(String amountYearVersement) {
		this.amountYearVersement = amountYearVersement;
	}
	public int getTransactionCount() {
		return transactionCount;
	}
	public void setTransactionCount(int transactionCount) {
		this.transactionCount = transactionCount;
	}
}
