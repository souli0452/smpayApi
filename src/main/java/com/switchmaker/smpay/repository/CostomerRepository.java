package com.switchmaker.smpay.repository;

import com.switchmaker.smpay.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CostomerRepository extends JpaRepository<Customer, UUID> {
	Boolean existsByEmail(String email);
	//Boolean existsByUserCode(String userCode);
	//Boolean existsByCostomerCode(String costomerCode);
	//Boolean existsByUsername(String username);
	//Customer findByUsername(String username);
	Customer findByEmail(String email);
	@Query(value = "SELECT COUNT (*) FROM costomer", nativeQuery = true)
	Integer findCountCostomer();



}
