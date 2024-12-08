package com.switchmaker.smpay.services;

import com.switchmaker.smpay.entities.CostomerRate;

import java.util.List;
import java.util.UUID;

public interface CostomerRateService {
	public CostomerRate saveCostomerRate(UUID costomerId, UUID paymentServicesId, float rate);
	public List<CostomerRate> getCostomerRatesById(UUID costomerId);
	List<CostomerRate> getAllCustomersRates();

	CostomerRate updateCostumerRate(UUID id, CostomerRate costomerRate);

	void deleCostumerRate(UUID id);
	public CostomerRate getCostomerRateByCostomerAndMeansPayment(UUID costomerId, UUID paymentServiceId);
}
