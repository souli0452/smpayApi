package com.switchmaker.smpay.repository;

import com.switchmaker.smpay.entities.Customer;
import com.switchmaker.smpay.entities.Notification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
	List<Notification> findByCostomerAndStatus(Customer costomer, boolean status, Sort sort);
	List<Notification> findByCostomer(Customer costomer, Sort sort);

	@Query(value = "SELECT COUNT (*) FROM notification WHERE costomer_id = ?1", nativeQuery = true)
	Integer countCostomerMessage(UUID costomer);

	@Query(value = "SELECT COUNT (*) FROM notification WHERE costomer_id = ?1 and status = false", nativeQuery = true)
	Integer countCostomerNotification(UUID costomer);
}
