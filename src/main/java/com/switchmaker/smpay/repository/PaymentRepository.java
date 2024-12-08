package com.switchmaker.smpay.repository;


import com.switchmaker.smpay.entities.Payment;
import com.switchmaker.smpay.entities.Platform;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin("*")
@RepositoryRestResource
public interface PaymentRepository extends JpaRepository<Payment, UUID>{
	List<Payment> findByPlatform(Platform platform);
	List<Payment> findByPlatform(Platform platform, Sort sort);
	List<Payment> findByStatus(String status);
	Payment findByPaymentCode(String code);

	@Query(value = "SELECT * FROM payment WHERE status = ?1 ORDER BY payment_date DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
	List<Payment> findByStatus(String statut, Integer limit, Integer offset);

	@Query(value = "SELECT * FROM payment t, platform p, costomer c WHERE c.id = ?1 AND p.costomer_id = c.id AND t.platform_id = p.id  ORDER BY t.payment_date DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
	List<Payment> findCostomerTransactions(UUID costomer, Integer limit, Integer offset);

	@Query(value = "SELECT * FROM payment t, platform p, costomer c WHERE c.id = ?1 AND p.costomer_id = c.id AND t.platform_id = p.id  ORDER BY t.payment_date DESC", nativeQuery = true)
	List<Payment> findCostomerTransactions(UUID costomer);

	@Query(value = "SELECT * FROM payment t, platform p WHERE p.id = ?1 AND t.platform_id = p.id  ORDER BY t.payment_date DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
	List<Payment> findPlatformTransactions(UUID platform, Integer limit, Integer offset);

	@Query(value = "SELECT * FROM payment t, platform p WHERE p.id = ?1 AND t.platform_id = p.id  ORDER BY t.payment_date DESC", nativeQuery = true)
	List<Payment> findPlatformTransactions(UUID platform);

	@Query(value = "SELECT * FROM payment t, platform p, costomer c WHERE c.id = ?1 AND p.costomer_id = c.id AND t.platform_id = p.id AND date(t.payment_date) between ?2 AND ?3 ORDER BY t.payment_date DESC", nativeQuery = true)
	List<Payment> costomerTransactionsBetweenToDates(UUID costomer, LocalDate debut, LocalDate fin);

	@Query(value = "SELECT * FROM payment t, platform p WHERE p.id = ?1 AND t.platform_id = p.id AND date(t.payment_date) between ?2 AND ?3 ORDER BY t.payment_date DESC", nativeQuery = true)
	List<Payment> platformTransactionsBetweenToDates(UUID platform, LocalDate debut, LocalDate fin);

	@Query(value = "SELECT * FROM payment WHERE status = ?1 AND date(payment_date) between ?2 AND ?3 ORDER BY payment_date DESC", nativeQuery = true)
	List<Payment> transactionsByStatusBetweenToDates(String status, LocalDate debut, LocalDate fin);

	@Query(value = "SELECT COUNT (*) FROM transaction t, platform p, costomer c WHERE c.id = ?1 AND p.costomer_id = c.id AND t.platform_id = p.id AND t.status = ?2", nativeQuery = true)
	Integer countCostomerTransactionsByStatus(UUID costomer, String status);

	@Query(value = "SELECT COUNT (*) as nombre, extract (month from t.payment_date) as mois FROM payment t, platform p WHERE p.costomer_id = ?1 AND t.platform_id = p.id AND t.status = ?2 AND extract (year from t.payment_date) = extract (year from current_date) - ?3  group by extract (month from t.payment_date)", nativeQuery = true)
	List<Object[]> countCostomerTransactionsByStatusPerMonth(UUID costomer, String status, Integer value);

	@Query(value = "SELECT COUNT (*) as nombre, extract (month from payment_date) as mois FROM payment WHERE status = ?1 AND extract (year from payment_date) = extract (year from current_date) - ?2  group by extract (month from payment_date)", nativeQuery = true)
	List<Object[]> countTransactionsByStatusPerMonth(String status, Integer value);

	@Query(value = "SELECT COUNT (*) as nombre, extract (month from t.payment_date) as mois FROM payment t, platform p WHERE p.id = ?1 AND t.platform_id = p.id AND t.status = ?2 AND extract (year from t.payment_date) = extract (year from current_date) - ?3  group by extract (month from t.payment_date)", nativeQuery = true)
	List<Object[]> countPlatformTransactionsByStatusPerMonth(UUID platform, String status, Integer value);

	@Query(value = "SELECT COUNT (*) FROM payment t, platform p WHERE p.id = ?1 AND t.platform_id = p.id AND t.status = ?2", nativeQuery = true)
	Integer countPlatformTransactionsByStatus(UUID platform, String status);

	@Query(value = "SELECT COUNT (*) FROM payment", nativeQuery = true)
	Integer findCountTransaction();

	@Query(value = "SELECT SUM (solde) FROM platform", nativeQuery = true)
	Integer findSolde();

	@Query(value = "SELECT SUM (solde) FROM platform WHERE costomer_id = ?1", nativeQuery = true)
	String findCostomerBalance(UUID id);

	@Query(value = "SELECT COUNT (*) FROM payment WHERE status = ?1", nativeQuery = true)
	Integer countTransanctionByStatus(String status);

	@Query(value = "select count (*) from payment t, costomer_rate cr, payment_service ps, means_payment mp where t.costomer_rate_id = cr.id and cr.payment_services_id = ps.id and ps.mean_payment_id=mp.id and mp.wording = ?1", nativeQuery = true)
	Integer countTransanctionByType(String type);

	@Query(value = "select count (*) from transaction t, costomer_rate cr, payment_service ps, means_payment mp where t.costomer_rate_id = cr.id and cr.payment_services_id = ps.id and cr.costomer_id = ?1 and ps.mean_payment_id=mp.id and mp.wording = ?2", nativeQuery = true)
	Integer countCostomerTransanctionByType(UUID costomer,String type);

	@Query(value = "SELECT COUNT (*) FROM transaction t, platform p WHERE p.id = ?1 AND t.platform_id = p.id AND t.transaction_type = ?2", nativeQuery = true)
	Integer countPlatformTransanctionByType(UUID platform,String type);

	@Query(value = "select sum(payment_amount) from payment where status = ?1", nativeQuery = true)
	Integer amountTransactionByStatus(String status);

	@Query(value = "select sum(payment_amount) from transaction where status = ?1 and extract (year from payment_date) = extract (year from current_date) - ?2", nativeQuery = true)
	String yearAmountTransaction(String status, Integer value);

	//@Query(value = "select sum(t.transaction_amount - t.transaction_amount * c.rate / 100) from transaction t, platform p, client c where c.id = ?1 AND t.platform_id = p.id AND p.client_id = c.id AND t.status = ?2", nativeQuery = true)
	@Query(value = "select sum(t.payment_amount) from transaction t, platform p where p.costomer_id = ?1 AND t.platform_id = p.id AND t.status = ?2", nativeQuery = true)
	String amountCostomerTransaction(UUID costomer, String status);

	//@Query(value = "select sum(t.transaction_amount - t.transaction_amount * c.rate / 100) from transaction t, platform p, client c where c.id = ?1 AND t.platform_id = p.id AND p.client_id = c.id AND t.status = ?2 AND extract (year from t.transaction_date) = extract (year from current_date) - ?3", nativeQuery = true)
	@Query(value = "select sum(t.transaction_amount) from transaction t, platform p where p.costomer_id = ?1 AND t.platform_id = p.id AND t.status = ?2 AND extract (year from t.payment_date) = extract (year from current_date) - ?3", nativeQuery = true)
	String yearAmountCostomerTransaction(UUID costomer, String status, Integer value);

	//@Query(value = "select sum(t.transaction_amount - t.transaction_amount * c.rate / 100) from transaction t, platform p, client c where c.id = ?1 AND p.id = ?2 AND t.platform_id = p.id AND p.client_id = c.id AND t.status = ?3", nativeQuery = true)
	@Query(value = "select sum(t.payment_amount) from payment t where t.platform_id = ?1 AND t.status = ?2", nativeQuery = true)
	String amountPlatformTransaction(UUID platform, String status);

	//@Query(value = "select sum(t.transaction_amount - t.transaction_amount * c.rate / 100) from transaction t, platform p, client c where c.id = ?1 AND p.id = ?2 AND t.platform_id = p.id AND p.client_id = c.id AND t.status = ?3 AND extract (year from t.transaction_date) = extract (year from current_date) - ?4", nativeQuery = true)
	@Query(value = "select sum(t.payment_amount) from payment t where t.platform_id = ?1 AND t.status = ?2 AND extract (year from t.payment_date) = extract (year from current_date) - ?3", nativeQuery = true)
	String yearAmountPlatformTransaction(UUID platform, String status, Integer value);

	@Query(value = "select sum(t.payment_amount) from transaction t, platform p, costomer c where c.id = ?1 AND t.platform_id = p.id AND p.costomer_id = c.id AND t.status = ?2", nativeQuery = true)
	String amountCostomerTransactionWithoutCommission(UUID costomer, String status);

	@Query(value = "select sum(t.payment_amount) from payment t, platform p where p.id = ?1 AND t.platform_id = p.id AND t.status = ?2", nativeQuery = true)
	String amountPlatformTransactionWithoutCommission(UUID platform, String status);

	//@Query(value = "select sum(t.transaction_amount - t.transaction_amount * c.rate / 100) as montant,extract (month from t.transaction_date) as mois from transaction t, platform p, client c where c.id = ?1 AND t.platform_id = p.id AND p.client_id = c.id AND t.status='success' AND extract (year from t.transaction_date) = extract (year from current_date) - ?2  group by extract (month from t.transaction_date)", nativeQuery = true)
	@Query(value = "select sum(t.payment_amount) as montant,extract (month from t.payment_date) as mois from payment t, platform p where p.costomer_id = ?1 AND t.platform_id = p.id AND t.status='success' AND extract (year from t.payment_date) = extract (year from current_date) - ?2  group by extract (month from t.payment_date)", nativeQuery = true)
	List<Object[]> costomerTransactionAmountPerMonth(UUID costomer, Integer value);

	/*@Query(value = "select sum(t.transaction_amount - t.transaction_amount * c.rate / 100) as montant,extract (month from t.transaction_date) as mois from transaction t, platform p, client c where t.platform_id = p.id AND p.client_id = c.id AND t.status= ?1 AND extract (year from t.transaction_date) = extract (year from current_date) - ?2  group by extract (month from t.transaction_date)", nativeQuery = true)
	List<Object[]> transactionAmountPerMonth(String status, Integer value);*/

	@Query(value = "select sum(payment_amount) as montant,extract (month from payment_date) as mois from payment where status= ?1 AND extract (year from payment_date) = extract (year from current_date) - ?2  group by extract (month from payment_date)", nativeQuery = true)
	List<Object[]> transactionAmountPerMonth(String status, Integer value);

	@Query(value = "select sum(a.balance) as montant, extract (month from a.balance_modification_date) as mois from account a, platform p where a.platform_id = p.id and p.costomer_id =?1 and a.balance_modification_date in (select max(a.balance_modification_date) from account a, platform p where a.platform_id = p.id and p.costomer_id = ?1 and extract (year from a.balance_modification_date) = extract (year from current_date) - ?2 group by a.platform_id, extract (month from a.balance_modification_date)) group by extract (month from a.balance_modification_date)", nativeQuery = true)
	List<Object[]> costomerBalancePerMonth(UUID costomer, Integer value);

	@Query(value = "select sum(balance) from (select a.balance, a.platform_id from account a, platform p where a.platform_id = p.id and p.costomer_id = ?1 and a.balance_modification_date in (select max(a.balance_modification_date) from account a, platform p where a.platform_id = p.id and p.costomer_id = ?1 and extract (year from a.balance_modification_date) = extract (year from current_date) - ?2 group by platform_id) group by a.balance, a.platform_id) as tab", nativeQuery = true)
	Double costomerYearBalance(UUID costomer, Integer value);

	@Query(value = "select balance as montant, extract (month from balance_modification_date) as mois from account where platform_id = ?1 and balance_modification_date in (select max(balance_modification_date) from account where platform_id = ?1 and extract (year from balance_modification_date) = extract (year from current_date) - ?2 group by extract (month from balance_modification_date))", nativeQuery = true)
	List<Object[]> platformBalancePerMonth(UUID platform, Integer value);

	@Query(value = "select balance from account where platform_id = ?1 and balance_modification_date = (select max(balance_modification_date) from account where platform_id = ?1 and extract (year from balance_modification_date) = extract (year from current_date) - ?2)", nativeQuery = true)
	Double platformYearBalance(UUID platform, Integer value);

	//@Query(value = "select sum(t.transaction_amount - t.transaction_amount * c.rate / 100) as montant,extract (month from t.transaction_date) as mois from transaction t, platform p, client c where c.id = ?1 AND p.id = ?2 AND t.platform_id = p.id AND p.client_id = c.id AND t.status='success' AND extract (year from t.transaction_date) = extract (year from current_date) - ?3  group by extract (month from t.transaction_date)", nativeQuery = true)
	@Query(value = "select sum(t.payment_amount) as montant,extract (month from t.payment_date) as mois from payment t where t.platform_id = ?1 AND t.status='success' AND extract (year from t.payment_date) = extract (year from current_date) - ?2  group by extract (month from t.payment_date)", nativeQuery = true)
	List<Object[]> platformTransactionAmountPerMonth(UUID platform, Integer value);

	@Query(value = "select sum(payment_amount) as montant,extract (month from payment_date) as mois from payment where status = 'success' and extract (year from payment_date) = extract (year from current_date)  group by extract (month from payment_date)", nativeQuery = true)
	List<Object[]> transactionAmountPerMonth();

	@Query(value = "select count(*) as nombre,extract (month from payment_date) as mois from payment where extract (year from payment_date) = extract (year from current_date)  group by extract (month from payment_date)", nativeQuery = true)
	List<Object[]> countTransactionPerMonth();

	@Query(value = "select extract (month from current_date)", nativeQuery = true)
	Double currentMonth();

	@Query(value = "select sum(payment_amount) from payment where payment_type = ?1 and status= ?2", nativeQuery = true)
	String findAmountTransactionByTypeAndStatus(String type, String status);

	//@Query(value = "SELECT SUM(t.transaction_amount - t.transaction_amount * c.rate / 100) FROM transaction t, platform p, client c WHERE c.id = ?1 AND p.client_id = c.id AND t.platform_id = p.id AND t.transaction_type = ?2 AND t.status = ?3", nativeQuery = true)
	@Query(value = "SELECT SUM(t.payment_amount) FROM payment t, platform p WHERE p.costomer_id = ?1 AND t.platform_id = p.id AND t.payment_type = ?2 AND t.status = ?3", nativeQuery = true)
	String findAmountCostomerTransactionByTypeAndStatus(UUID costomer,String type, String status);

	//@Query(value = "SELECT SUM (transaction_amount * rate / 100) FROM transaction WHERE status = ?1", nativeQuery = true)
	@Query(value = "SELECT SUM (commission_amount) FROM payment WHERE status = ?1", nativeQuery = true)
	String amountCommission(String status);

	//@Query(value = "SELECT SUM (t.transaction_amount * t.rate / 100) FROM transaction t, platform p WHERE t.platform_id=p.id AND p.client_id=?1 AND status = ?2", nativeQuery = true)
	@Query(value = "SELECT SUM (t.commission_amount) FROM payment t, platform p WHERE t.platform_id=p.id AND p.costomer_id=?1 AND status = ?2", nativeQuery = true)
	String amountCostomerCommission(UUID costomer, String status);

	@Query(value = "SELECT COUNT (*) FROM transaction t, platform p, costomer c WHERE c.id = ?1 AND p.costomer_id = c.id AND t.platform_id = p.id AND extract (year from t.payment_date) = extract (year from current_date) - ?2", nativeQuery = true)
	Integer countCostomerTransaction(UUID costomer,Integer value);

	@Query(value = "SELECT COUNT (*) FROM payment WHERE platform_id = ?1 AND extract (year from payment_date) = extract (year from current_date) - ?2", nativeQuery = true)
	Integer countPlatformTransaction(UUID platform,Integer value);

	@Query(value = "SELECT COUNT (*) FROM transaction WHERE extract (year from payment_date) = extract (year from current_date) - ?1", nativeQuery = true)
	Integer countTransaction(Integer value);
	@Query(value = "SELECT SUM(commission_amount) FROM payment", nativeQuery = true)
	Double commissionVolume();
	@Query(value = "SELECT SUM(amount) FROM payment_request", nativeQuery = true)
	Double paymentVolume();
	@Query(value = "SELECT status, COUNT(*) from payment group by status", nativeQuery = true)
	List<Object[]> totalTransactionByAllStatus();
	@Query(value = "select status, sum(payment_amount) from payment group by status", nativeQuery = true)
	List<Object[]> transactionsVolumeByStatus();
	@Query(value =
					"SELECT " +
					"    to_char(payment_date, 'Month'), " +
					"    status, " +
					"     COALESCE(count(*), 0) " +
					"FROM " +
					"    payment " +
					"WHERE " +
					"    EXTRACT(YEAR FROM payment_date) = EXTRACT(YEAR FROM CURRENT_DATE) " +
					"GROUP BY " +
					"    EXTRACT(MONTH FROM payment_date), " +
					"    status," +
					"    to_char(payment_date, 'Month') " +
					"ORDER BY " +
					"    EXTRACT(MONTH FROM payment_date),status ", nativeQuery = true)
	List<Object> monthlyTransactionsCountByStatus();
	@Query(value = "select to_char(sending_date, 'Month') as month, sum(amount) as montant from payment_request " +
					"where " +
					"extract(year from sending_date)=extract(year from current_date) " +
					"and " +
					"extract(month from sending_date)=extract(month from current_date) group by sending_date ", nativeQuery = true)
	Map<String, Double> monthlyPaymentsVolume();
	@Query(value = "select to_char(sending_date, 'Month')as month, count(*) as total from payment_request " +
				   "where " +
			       "extract(year from sending_date)=extract(year from current_date) " +
				   "and " +
				   "extract(month from sending_date)=extract(month from current_date) group by sending_date ", nativeQuery = true)
	Map<String, Long> monthlyPaymentsCount();
	@Query(value =
			"SET lc_time TO 'fr_FR';" +
			"SELECT" +
			"    to_char(payment_date, 'Month')," +
			"    status," +
			"    SUM(payment_amount)" +
			"FROM" +
			"    payment" +
			"WHERE" +
			"    EXTRACT(YEAR FROM payment_date) = EXTRACT(YEAR FROM CURRENT_DATE)" +
			"GROUP BY" +
			"    EXTRACT(MONTH FROM payment_date)," +
			"    status," +
			"    to_char(payment_date, 'Month')" +
			"ORDER BY" +
			"    EXTRACT(MONTH FROM payment_date)," +
			"    status", nativeQuery = true)
	Map<String, Map<String, Double>> monthlyPaymentsVolumeByStatus();
	@Query(value =
			"SET lc_time TO 'fr_FR';" +
			"SELECT" +
			"    to_char(payment_date, 'Month')," +
			"    status," +
			"    count(*) " +
			"FROM" +
			"    payment" +
			"WHERE" +
			"    EXTRACT(YEAR FROM payment_date) = EXTRACT(YEAR FROM CURRENT_DATE)" +
			"GROUP BY" +
			"    EXTRACT(MONTH FROM payment_date)," +
			"    status," +
			"    to_char(payment_date, 'Month')" +
			"ORDER BY" +
			"    EXTRACT(MONTH FROM payment_date),status", nativeQuery = true)
	Map<String, Map<String, Long>> monthlyPaymentsCountByStatus();

	@Query(value = "select sum(payment_amount) from payment", nativeQuery = true)
	Map<String, Long> volumeTransactionsOfPaymentMethodCategory();
	@Query(value = "select sum(payment_amount) from payment", nativeQuery = true)
	int amountTransaction();
}
