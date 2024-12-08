package com.switchmaker.smpay.services;

import com.switchmaker.smpay.dto.CostomerApps;
import com.switchmaker.smpay.dto.CustomerDto;
import com.switchmaker.smpay.entities.Customer;
import com.switchmaker.smpay.enumeration.ClientType;
import com.switchmaker.smpay.enumeration.UserAccountType;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface CostomerService {
	public Customer saveCostomer(CustomerDto costomer) throws IOException;
	public boolean existsByCostomerCode(String costomerCode);
	public boolean existsByUsername(String username);
	public boolean existsByUserCode(String userCode);
	public boolean existsByEmail(String email);
	public Customer getCostomerById(UUID id);
	public Customer updateCostomer(UUID id, CustomerDto costomer) throws IOException;
	public List<Customer> getAllCostomers();

  List<Customer> getAllCostomersByCustomertype(ClientType type);
  public Customer disableOrEnableCustomerDeveloperOrSupervisorAccount(UUID customerId, UserAccountType type);
  public List<CostomerApps> getAllCostomersWithCostomerPlatforms();
  public CostomerApps getCostomerWithCostomerPlatforms(UUID customerId);
	public void deleteCostomerById(UUID id);
	public int countCostomer();
}
