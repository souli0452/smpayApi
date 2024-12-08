package com.switchmaker.smpay.repository;

import com.switchmaker.smpay.entities.CostomerRate;
import com.switchmaker.smpay.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CostomerRateRepository extends JpaRepository<CostomerRate, UUID> {
	@Query(value = "SELECT * FROM costomer_rate WHERE costomer_id = ?1 AND means_payment_id = ?2", nativeQuery = true)
	CostomerRate getRateCostomerAndPaymentService(UUID costomerId, UUID paymentServiceId);
	List<CostomerRate> findByCostomer(Customer costomer);
}
