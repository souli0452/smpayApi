package com.switchmaker.smpay.repository;

import com.switchmaker.smpay.entities.PaymentRequest;
import com.switchmaker.smpay.entities.Platform;
import com.switchmaker.smpay.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, UUID> {
	List<PaymentRequest> findByPlatform(Platform platform);

	List<PaymentRequest> findByPlatformAndStatus(Platform platform, String status);

	List<PaymentRequest> findByStatus(String status);

	List<PaymentRequest> userId(UUID userId);



	@Query(value = "SELECT * FROM payment_request pr,platform p, costomer c WHERE c.id = ?1 AND c.id = p.costomer_id AND p.id = pr.platform_id ORDER BY pr.sending_date DESC LIMIT ?2", nativeQuery = true)
	List<PaymentRequest> findCostomerRequest(UUID costomer, Integer limit);

	@Query(value = "SELECT * FROM payment_request pr, platform p, costomer c WHERE c.id = ?1 AND d.status = ?2 AND c.id = p.costomer_id AND p.id = pr.platform_id ORDER BY pr.sending_date DESC LIMIT ?3", nativeQuery = true)
	List<PaymentRequest> findCostomerRequestStatus(UUID costomer, String status, Integer limit);

	List<PaymentRequest> findByStatus(String status, Sort sort);

	@Query(value = "SELECT * FROM payment_request WHERE status = ?1 ORDER BY sending_date DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
	List<PaymentRequest> findByStatus(String status, Integer limit, Integer offset);

	@Query(value = "SELECT * FROM payment_request pr,platform p, costomer c WHERE c.id = ?1 AND pr.status IN ('success', 'failure') AND c.id = p.costomer_id AND p.id = pr.platform_id ORDER BY pr.sending_date DESC", nativeQuery = true)
	List<PaymentRequest> findCostomerRequestStatusNotAttempt(UUID client);

	@Query(value = "SELECT * FROM payment_request WHERE status = ?1 ORDER BY sending_date DESC", nativeQuery = true)
	List<PaymentRequest> demandeByStatus(String status);

	@Query(value = "SELECT * FROM payment_request WHERE status = ?1 AND sending_date between ?2 AND ?3 ORDER BY sending_date DESC", nativeQuery = true)
	List<PaymentRequest> periodDemandeByStatus(String status, LocalDate debut, LocalDate fin);

	@Query(value = "SELECT COUNT (*) FROM payment_request WHERE status = ?1", nativeQuery = true)
	Integer countDemandByStatus(String status);

	@Query(value = "SELECT COUNT (*) FROM payment_request pr, platform p, costomer c WHERE pr.platform_id = p.id AND p.costomer_id = c.id AND c.id = ?1 AND pr.status = ?2", nativeQuery = true)
	Integer countClientDemandByStatus(UUID id, String status);

	@Query(value = "SELECT COUNT (*) FROM payment_request WHERE platform_id = ?1 AND status = ?2", nativeQuery = true)
	Integer countPlatformDemandByStatus(UUID platform, String status);

	@Query(value = "SELECT SUM (amount) FROM payment_request WHERE status = ?1", nativeQuery = true)
	String sumRequestAmount(String status);

	@Query(value = "SELECT SUM (pr.amount) FROM payment_request pr, platform p, costomer c WHERE pr.platform_id = p.id AND p.costomer_id = c.id AND c.id = ?1 AND pr.status = ?2", nativeQuery = true)
	String sumCostomerRquestAmount(UUID id, String status);

	@Query(value = "SELECT SUM (pr.amount) FROM payment_request pr, platform p, costomer c WHERE pr.platform_id = p.id AND p.costomer_id = c.id AND c.id = ?1 AND pr.status = ?2 AND extract (year from pr.processing_date) = extract (year from current_date) - ?3", nativeQuery = true)
	String sumCostomerYearRequestAmount(UUID id, String status, Integer value);

	@Query(value = "SELECT SUM (amount) FROM payment_request WHERE platform_id = ?1 AND status = ?2", nativeQuery = true)
	String sumPlatformRequestAmount(UUID platform, String status);

	@Query(value = "SELECT SUM (amount) FROM payment_request WHERE platform_id = ?1 AND status = ?2 AND extract (year from processing_date) = extract (year from current_date) - ?3", nativeQuery = true)
	String sumPlatformYearRequestAmount(UUID platform, String status, Integer value);

	@Query(value = "SELECT SUM (pr.amount) as montant,extract (month from pr.processing_date) as mois FROM payment_request pr, platform p, costomer c WHERE pr.platform_id = p.id AND p.costomer_id = c.id AND c.id = ?1 AND d.status = ?2 AND extract (year from pr.processing_date) = extract (year from current_date) - ?3  group by extract (month from pr.processing_date)", nativeQuery = true)
	List<Object[]> costomerPayment(UUID id, String status, Integer value);

	@Query(value = "SELECT SUM (amount) as montant,extract (month from processing_date) as mois FROM payment_request WHERE platform_id = ?1 AND status = ?2 AND extract (year from processing_date) = extract (year from current_date) - ?3  group by extract (month from processing_date)", nativeQuery = true)
	List<Object[]> platformPayment(UUID platform, String status, Integer value);

  List<PaymentRequest> findByUser(User user);
}
