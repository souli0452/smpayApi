package com.switchmaker.smpay.services;
import com.switchmaker.smpay.dto.*;
import com.switchmaker.smpay.entities.Payment;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PaymentService {

  Map<String, Map<String, Object>> numberOfTransactionsByPaymentMethod(UUID supervisor);

  Map<String, Map<String, Object>>volumeOfTransactionsByPaymentMethod(UUID supervisor);

  Map<String, Double> getMonthlyDepositVolume(UUID supervisor);

  Map<String, Long> getMonthlyDepositCount(UUID supervisor);

  double getAccountBalance(UUID supervisor);

  Map<String,Long> getTransactionsByPaymentMethodCategory(UUID supervisor);

  Map<String,Long> getVolumeOfTransactionsByPaymentMethodCategory(UUID supervisor);

  Map<String, Map<String, Long>> getMonthlyTransactionsByStatus(UUID supervisor);

  Map<String, Map<String, Double>> getMonthlyTransactionVolumeByStatus(UUID supervisor);

  Map<String, Double> getMonthlyBalance(UUID supervisor);

  Map<String, Map<String, Number>> getBalanceAndPaymentPerMonth(UUID supervisor);

  public ResponseMessage savePayment(ValidPaymentInformations validPaymentInformations, String paymentStatus);
	public Payment getPaymentByPaymentCode(String paymentCode);
	public ResponseMessage getPayment(Payment payment);

    public ResponseMessage updateBFMoovPayment(Payment payment);
	public ResponseMessage updateCIWavePayment(Payment payment);
	public ResponseMessage updateCIMTNPayment(Payment payment);
	public int countTransactions();
	public int countTransactionByStatus(String status);
	public int amountTransactionByStatus(String status);
	public double amountTransaction();

  public int getTotalNumberOfTransactions(UUID supervisor);
  public int getTotalTransactionsByStatus(UUID supervisor,String status);
 public double getTransactionsVolume(UUID supervisor);
  public double getTransactionsVolumeBystatus(UUID supervisor,String status);
  public double getCommissionsVolume(UUID supervisor);
  public double getPaymentsVolume(UUID supervisor);
    Double commissionVolume();

    Double paymentVolume();

   Map<String, Integer>totalTransactionByAllStatus();

    Map<String , Double> transactionsVolumeByStatus();

    Map<String, Map<String, Object>> totalTransactionsByPaymentMethod();

    Map<String, Map<String, Object>> volumeTransactionByPaymentMethod();

    Map<String,Long> totalTransactionOfPaymentMethodCategory();

    Map<String, Long> volumeTransactionsByPaymentMethodCategory();

   Map<String, Map<String, Long>> monthlyPaymentsCountByStatus();

    Map<String, Map<String, Double>> monthlyPaymentsVolumeByStatus();

    Map<String, Long> monthlyPaymentsCount();

    Map<String, Double> monthlyPaymentsVolume();

    Map<String, Map<String, Long>> monthlyTransactionsCountByStatus();

    Map<String, Map<String, Double>> monthlyTransactionsVolumeByStatus();
public AdminContainer getAdminContainerStat();

    ApplicatifContainer getContainerStatApplicatifState(UUID compteApplicatifId);

    public SupervisorContainer getSupervisorStat(UUID supervisor);

}
