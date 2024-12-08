package com.switchmaker.smpay.services;

import com.switchmaker.smpay.dto.ValidPaymentInformations;
import com.switchmaker.smpay.entities.PaymentRequest;
import com.switchmaker.smpay.entities.Platform;
import com.switchmaker.smpay.entities.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PlatformService {
	public Platform savePlatform(Platform platform);
	public Platform getByUserAndPlatformName(User user, String platformName);
	public List<Platform> getAllPlatforms();
	public Platform updatePlatform(UUID platformId, Platform platform);
	public Platform getPlatformById(UUID platformId);
	public Platform getPlatformByCode(String platformCode);
	public void deletePlatform(UUID platformId);
	public List<Platform> getCostomerPlatforms(UUID costomerId);
	public Platform getPlatformByCostomerCodeAndPlatformCode(String costomerCode, String platformCode);
	public Platform balanceUpdate(ValidPaymentInformations validPaymentInformations);
	public int countPlatforms();



	public int getTotalNumberOfTransactionsAppp(UUID compteApplicatifId);

	public int getTotalTransactionsByStatusApp(UUID compteApplicatifId,String status);

	public double getTransactionsVolumeApp(UUID compteApplicatifId);

	public double getTransactionsVolumeByStatusApp(UUID compteApplicatifId, String status);

	public double getCommissionsVolumeApp(UUID compteApplicatifId);

	public double getPaymentsVolumeApp(UUID compteApplicatifId);

	public Map<String, Map<String, Object>> numberOfTransactionsByPaymentMethodApp(UUID compteApplicatifId);

	public Map<String, Map<String, Object>> volumeOfTransactionsByPaymentMethodApp(UUID compteApplicatifId);

	public Map<String,Long> getTransactionsByPaymentMethodCategoryApp(UUID compteApplicatifId);

	public Map<String, Map<String, Long>> getMonthlyTransactionsByStatusApp(UUID compteApplicatifId);

	public Map<String, Map<String, Double>> getMonthlyTransactionVolumeByStatusApp(UUID compteApplicatifId);

	public Map<String, Long> getMonthlyDepositCountApp(UUID compteApplicatifId);

	public Map<String, Double> getMonthlyDepositVolumeApp(UUID compteApplicatifId);

	public Map<String, Long> getVolumeOfTransactionsByPaymentMethodCategoryApp(UUID compteApplicatifId);

	public double getAccountBalanceApp(UUID compteApplicatifId);

	public Map<String, Double> getMonthlyBalanceApp(UUID compteApplicatifId);

	public Map<String, Map<String, Number>> getBalanceAndPaymentPerMonthApp(UUID compteApplicatifId);

	public List <Platform> getClientPlatformApp(UUID compteApplicatifId);


	User getAccountApp(UUID compteApplicatifId);

	List <PaymentRequest> getClientPlatformAppPaymentRequest(UUID compteApplicatifId);

	List <PaymentRequest> getClientPlatformAppPaymentRequestSuccess(UUID compteApplicatifId);
}
