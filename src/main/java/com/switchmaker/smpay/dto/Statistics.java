package com.switchmaker.smpay.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Statistics {
  private int totalCustomer;
  private int totalPlatform;
  private int totalNumberOfTransactions;
  private int totalSuccessfulTransactions;
  private int totalFailedTransactions;
  private int totalInitiatedTransactions;
  private double transactionsVolume;
  private double initiatedTransactionsVolume;
  private double successfulTransactionsVolume;
  private double failedTransactionsVolume;
  private double commissionsVolume;
  private double paymentsVolume;
  private Map<String, Integer> transactionsCountByStatus = new HashMap<>();
  private Map<String, Double> transactionsVolumeByStatus = new HashMap<>();
  private Map<String,Long> transactionsByPaymentMethodCategory = new HashMap<>();
  private Map<String, Long> volumeOfTransactionsByPaymentMethodCategory = new HashMap<>();
  private Map<String, Map<String, Object>> numberOfTransactionsByPaymentMethod = new HashMap<>();
  private Map<String, Map<String, Object>> volumeOfTransactionsByPaymentMethod = new HashMap<>();
  private Map<String, Map<String, Long>> monthlyTransactionsCountByStatus;
  private Map<String, Map<String, Double>> monthlyTransactionsVolumeByStatus;
  private Map<String, Double> monthlyPaymentsVolume = new HashMap<>();
  private Map<String, Long> monthlyPaymentsCount = new HashMap<>();
  private double accountBalance;
  private Map<String, Double> balancePerMonth = new HashMap<>();
  private Map<String, Map<String, Number>> balancePaymentPerMonth = new HashMap<>();

}



//package com.switchmaker.smpay.dto;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class Statistics {
//
//
// private int totalNumberOfTransactions;
//  private int totalSuccessfulTransactions;
// private int totalFailedTransactions;
//  private int totalInitiatedTransactions;
//
// private  double transactionsVolume;
// private double successfulTransactionsVolume;
// private double failedTransactionsVolume;
// private double commissionsVolume;
// private double paymentsVolume;
// Map<String,Integer> transactionsCountByStatus=new HashMap<>();
// Map<String,Double> transactionsVolumeByStatus=new HashMap<>();
//
//Map<String, Double> transactionsByPaymentMethodCategory=new HashMap<>();
//Map<String, Double> volumeOfTransactionsByPaymentMethodCategory;
//
//Map<String, Integer> numberOfTransactionsByPaymentMethod;
//Map<String, Double> volumeOfTransactionsByPaymentMethod;
//
// List<MonthlyTransactionsDTO> monthlyTransactionsCountByStatus=new ArrayList<>();
//  List<MonthlyTransactionsVolume> monthlyTransactionsVolumeByStatus;
//
//  Map<String,Double> monthlyPaymentsVolume;
//  Map<String,Integer> monthlyPaymentsCount;
//
//  double accountBalance;
//  Map<String,Double> balancePerMonth;
//	private int transactionCount;
//	private int initTransactionCount;
//	private int failTransactionCount;
//	private int successTransactionCount;
//	private int initTransactionAmount;
//	private int failTransactionAmount;
//	private int successTransactionAmount;
//	private int clientCount;
//	private int platformCount;
//	private int contactCount;
//	private int newDemandCount;
//	private int rejectDemandCount;
//	private int successDemandCount;
//	private int notificationCount;
//	private int messageCount;
//	private int transactionAmount;
//	private String versementAmount = "0";
//	private String commissionAmount = "0";
//	private int orangeTransactionCount;
//	private int moovTransactionCount;
//	private int corisTransactionCount;
//	private int mtnTransactionCount;
//	private int waveTransactionCount;
//	private String moovTransactionAmount = "0";
//	private String orangeTransactionAmount = "0";
//	private String corisTransactionAmount = "0";
//	private String mtnTransactionAmount = "0";
//	private String waveTransactionAmount = "0";
//	private String clientSolde = "0";
//	public int getTransactionCount() {
//		return transactionCount;
//	}
//	public void setTransactionCount(int transactionCount) {
//		this.transactionCount = transactionCount;
//	}
//	public int getInitTransactionCount() {
//		return initTransactionCount;
//	}
//	public void setInitTransactionCount(int initTransactionCount) {
//		this.initTransactionCount = initTransactionCount;
//	}
//	public int getFailTransactionCount() {
//		return failTransactionCount;
//	}
//	public void setFailTransactionCount(int failTransactionCount) {
//		this.failTransactionCount = failTransactionCount;
//	}
//	public int getSuccessTransactionCount() {
//		return successTransactionCount;
//	}
//	public void setSuccessTransactionCount(int successTransactionCount) {
//		this.successTransactionCount = successTransactionCount;
//	}
//
//	public int getInitTransactionAmount() {
//		return initTransactionAmount;
//	}
//	public void setInitTransactionAmount(int initTransactionAmount) {
//		this.initTransactionAmount = initTransactionAmount;
//	}
//	public int getFailTransactionAmount() {
//		return failTransactionAmount;
//	}
//	public void setFailTransactionAmount(int failTransactionAmount) {
//		this.failTransactionAmount = failTransactionAmount;
//	}
//	public int getSuccessTransactionAmount() {
//		return successTransactionAmount;
//	}
//	public void setSuccessTransactionAmount(int successTransactionAmount) {
//		this.successTransactionAmount = successTransactionAmount;
//	}
//	public int getClientCount() {
//		return clientCount;
//	}
//	public void setClientCount(int clientCount) {
//		this.clientCount = clientCount;
//	}
//	public int getPlatformCount() {
//		return platformCount;
//	}
//	public void setPlatformCount(int platformCount) {
//		this.platformCount = platformCount;
//	}
//	public int getContactCount() {
//		return contactCount;
//	}
//	public void setContactCount(int contactCount) {
//		this.contactCount = contactCount;
//	}
//	public int getNewDemandCount() {
//		return newDemandCount;
//	}
//	public void setNewDemandCount(int newDemandCount) {
//		this.newDemandCount = newDemandCount;
//	}
//	public int getRejectDemandCount() {
//		return rejectDemandCount;
//	}
//	public void setRejectDemandCount(int rejectDemandCount) {
//		this.rejectDemandCount = rejectDemandCount;
//	}
//	public int getSuccessDemandCount() {
//		return successDemandCount;
//	}
//	public void setSuccessDemandCount(int successDemandCount) {
//		this.successDemandCount = successDemandCount;
//	}
//	public int getNotificationCount() {
//		return notificationCount;
//	}
//	public void setNotificationCount(int notificationCount) {
//		this.notificationCount = notificationCount;
//	}
//	public int getMessageCount() {
//		return messageCount;
//	}
//	public void setMessageCount(int messageCount) {
//		this.messageCount = messageCount;
//	}
//	public int getTransactionAmount() {
//		return transactionAmount;
//	}
//	public void setTransactionAmount(int transactionAmount) {
//		this.transactionAmount = transactionAmount;
//	}
//	public String getVersementAmount() {
//		return versementAmount;
//	}
//	public void setVersementAmount(String versementAmount) {
//		this.versementAmount = versementAmount;
//	}
//	public String getCommissionAmount() {
//		return commissionAmount;
//	}
//	public void setCommissionAmount(String commissionAmount) {
//		this.commissionAmount = commissionAmount;
//	}
//	public int getOrangeTransactionCount() {
//		return orangeTransactionCount;
//	}
//	public void setOrangeTransactionCount(int orangeTransactionCount) {
//		this.orangeTransactionCount = orangeTransactionCount;
//	}
//	public int getMoovTransactionCount() {
//		return moovTransactionCount;
//	}
//	public void setMoovTransactionCount(int moovTransactionCount) {
//		this.moovTransactionCount = moovTransactionCount;
//	}
//	public int getCorisTransactionCount() {
//		return corisTransactionCount;
//	}
//	public void setCorisTransactionCount(int corisTransactionCount) {
//		this.corisTransactionCount = corisTransactionCount;
//	}
//	public int getMtnTransactionCount() {
//		return mtnTransactionCount;
//	}
//	public void setMtnTransactionCount(int mtnTransactionCount) {
//		this.mtnTransactionCount = mtnTransactionCount;
//	}
//	public int getWaveTransactionCount() {
//		return waveTransactionCount;
//	}
//	public void setWaveTransactionCount(int waveTransactionCount) {
//		this.waveTransactionCount = waveTransactionCount;
//	}
//	public String getMoovTransactionAmount() {
//		return moovTransactionAmount;
//	}
//	public void setMoovTransactionAmount(String moovTransactionAmount) {
//		this.moovTransactionAmount = moovTransactionAmount;
//	}
//	public String getOrangeTransactionAmount() {
//		return orangeTransactionAmount;
//	}
//	public void setOrangeTransactionAmount(String orangeTransactionAmount) {
//		this.orangeTransactionAmount = orangeTransactionAmount;
//	}
//	public String getCorisTransactionAmount() {
//		return corisTransactionAmount;
//	}
//	public void setCorisTransactionAmount(String corisTransactionAmount) {
//		this.corisTransactionAmount = corisTransactionAmount;
//	}
//	public String getMtnTransactionAmount() {
//		return mtnTransactionAmount;
//	}
//	public void setMtnTransactionAmount(String mtnTransactionAmount) {
//		this.mtnTransactionAmount = mtnTransactionAmount;
//	}
//	public String getWaveTransactionAmount() {
//		return waveTransactionAmount;
//	}
//	public void setWaveTransactionAmount(String waveTransactionAmount) {
//		this.waveTransactionAmount = waveTransactionAmount;
//	}
//	public String getClientSolde() {
//		return clientSolde;
//	}
//	public void setClientSolde(String clientSolde) {
//		this.clientSolde = clientSolde;
//	}
//}
