package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.dto.AdminContainer;
import com.switchmaker.smpay.dto.ApplicatifContainer;
import com.switchmaker.smpay.dto.Statistics;
import com.switchmaker.smpay.dto.SupervisorContainer;
import com.switchmaker.smpay.repository.ContactRepository;
import com.switchmaker.smpay.repository.NotificationRepository;
import com.switchmaker.smpay.repository.PaymentRequestRepository;
import com.switchmaker.smpay.services.CostomerService;
import com.switchmaker.smpay.services.PaymentService;
import com.switchmaker.smpay.services.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.UUID;

import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;

@CrossOrigin("*")
@RestController
@RequestMapping(ROOT_API)
public class StatisticsController {

	@Autowired
	CostomerService costomerService;
	@Autowired
	PlatformService platformService;
	@Autowired
	ContactRepository contactRepo;
	@Autowired
	PaymentService paymentService;
	@Autowired
	PaymentRequestRepository demandeRepo;
	@Autowired
	NotificationRepository notificationRepo;

	/*-----------------------------------------------------/
	/*          Statistiques pour le compte superviseur   /
	/*--------------------------------------------------*/

	@RolesAllowed({ADMIN,MANAGER})
	@GetMapping("/statistics/admin")
	public ResponseEntity<?> getAdminStatistics() {
		Statistics statistics = new Statistics();
		//total number of clients
		statistics.setTotalCustomer(costomerService.countCostomer());
		//total number of platforms
		statistics.setTotalPlatform(platformService.countPlatforms());
		//total number of transactions
		statistics.setTotalNumberOfTransactions(paymentService.countTransactions());
		//transactions amount
		statistics.setTransactionsVolume(paymentService.amountTransaction());
		// count the total transactions by status
		statistics.setTotalFailedTransactions(paymentService.countTransactionByStatus(FAILURE));
		statistics.setTotalSuccessfulTransactions(paymentService.countTransactionByStatus(SUCCESS));
		statistics.setTotalInitiatedTransactions(paymentService.countTransactionByStatus(INITIATE));
		//the amount of transaction by status
		statistics.setFailedTransactionsVolume(paymentService.amountTransactionByStatus(FAILURE));
		statistics.setSuccessfulTransactionsVolume(paymentService.amountTransactionByStatus(SUCCESS));
		statistics.setInitiatedTransactionsVolume(paymentService.amountTransactionByStatus(INITIATE));
		//amount of commissions
		statistics.setCommissionsVolume(paymentService.commissionVolume());
		//payment request amount

		//amount payment request
		statistics.setPaymentsVolume(paymentService.paymentVolume());
		statistics.setTransactionsCountByStatus(paymentService.totalTransactionByAllStatus());
		statistics.setTransactionsVolumeByStatus(paymentService.transactionsVolumeByStatus());
		//nombre total des transactions de tous les du moyen de payment
		statistics.setNumberOfTransactionsByPaymentMethod(paymentService.totalTransactionsByPaymentMethod());
		statistics.setVolumeOfTransactionsByPaymentMethod(paymentService.volumeTransactionByPaymentMethod());
		statistics.setTransactionsByPaymentMethodCategory(paymentService.totalTransactionOfPaymentMethodCategory());
		statistics.setVolumeOfTransactionsByPaymentMethodCategory(paymentService.volumeTransactionsByPaymentMethodCategory());
		statistics.setMonthlyTransactionsCountByStatus(paymentService.monthlyTransactionsCountByStatus());
		statistics.setMonthlyTransactionsVolumeByStatus(paymentService.monthlyTransactionsVolumeByStatus());
		statistics.setMonthlyPaymentsCount(paymentService.monthlyPaymentsCount());
		statistics.setMonthlyPaymentsVolume(paymentService.monthlyPaymentsVolume());
		return new ResponseEntity<Statistics>(statistics, HttpStatus.OK);

	}
	/*-----------------------------------------------------/
	/*          Statistiques pour le compte superviseur   /
	/*--------------------------------------------------*/

  @RolesAllowed({SUPERVISOR})
  @GetMapping("/statistics")
  public ResponseEntity<?> getSupervisorStatistics(@RequestParam UUID supervisor){
    Statistics statistics = new Statistics();
    statistics.setFailedTransactionsVolume(paymentService.getTotalTransactionsByStatus(supervisor,FAILURE));
    statistics.setTotalSuccessfulTransactions(paymentService.getTotalTransactionsByStatus(supervisor,SUCCESS));
    statistics.setTotalFailedTransactions(paymentService.getTotalTransactionsByStatus(supervisor,FAILURE));
    statistics.setTotalInitiatedTransactions(paymentService.getTotalTransactionsByStatus(supervisor,INITIATE));
    statistics.setFailedTransactionsVolume(paymentService.getTransactionsVolumeBystatus(supervisor,FAILURE));
    statistics.setSuccessfulTransactionsVolume(paymentService.getTransactionsVolumeBystatus(supervisor,SUCCESS));
    statistics.setInitiatedTransactionsVolume(paymentService.getTransactionsVolumeBystatus(supervisor,INITIATE));
    statistics.setMonthlyPaymentsCount(paymentService.getMonthlyDepositCount(supervisor));
    statistics.setCommissionsVolume(paymentService.getCommissionsVolume(supervisor));
    statistics.getTransactionsCountByStatus().put(SUCCESS,paymentService.getTotalTransactionsByStatus(supervisor,SUCCESS));
    statistics.getTransactionsCountByStatus().put(FAILURE,paymentService.getTotalTransactionsByStatus(supervisor,FAILURE));
    statistics.getTransactionsCountByStatus().put(INITIATE,paymentService.getTotalTransactionsByStatus(supervisor,INITIATE));
    statistics.getTransactionsVolumeByStatus().put(SUCCESS,paymentService.getTransactionsVolumeBystatus(supervisor,SUCCESS));
    statistics.getTransactionsVolumeByStatus().put(FAILURE,paymentService.getTransactionsVolumeBystatus(supervisor,FAILURE));
    statistics.getTransactionsVolumeByStatus().put(INITIATE,paymentService.getTransactionsVolumeBystatus(supervisor,INITIATE));
    statistics.setTransactionsVolume(paymentService.getTransactionsVolume(supervisor));
    statistics.setMonthlyTransactionsCountByStatus(paymentService.getMonthlyTransactionsByStatus(supervisor));
    statistics.setTransactionsByPaymentMethodCategory(paymentService.getTransactionsByPaymentMethodCategory(supervisor));
    statistics.setVolumeOfTransactionsByPaymentMethod(paymentService.volumeOfTransactionsByPaymentMethod(supervisor));
    statistics.setVolumeOfTransactionsByPaymentMethodCategory(paymentService.getVolumeOfTransactionsByPaymentMethodCategory(supervisor));
    statistics.setTotalNumberOfTransactions(paymentService.getTotalNumberOfTransactions(supervisor));
    statistics.setPaymentsVolume(paymentService.getPaymentsVolume(supervisor));
    statistics.setNumberOfTransactionsByPaymentMethod(paymentService.numberOfTransactionsByPaymentMethod(supervisor));
    statistics.setMonthlyPaymentsVolume(paymentService.getMonthlyDepositVolume(supervisor));
    statistics.setMonthlyTransactionsVolumeByStatus(paymentService.getMonthlyTransactionVolumeByStatus(supervisor));
    statistics.setAccountBalance(paymentService.getAccountBalance(supervisor));
    statistics.setBalancePerMonth(paymentService.getMonthlyBalance(supervisor));
    statistics.setBalancePaymentPerMonth(paymentService.getBalanceAndPaymentPerMonth(supervisor));
    return new ResponseEntity<Statistics>(statistics, HttpStatus.OK);
  }


  /*-----------------------------------------------------------------------------------------/
 /*        Statistiques pour le compte administraeur dans la section container principal    /
/*----------------------------------------------------------------------------------------*/
  @RolesAllowed({ADMIN,MANAGER})
  @GetMapping("/admin-container-stat")
  public ResponseEntity<AdminContainer> getAdminContainerStat(){
    return ResponseEntity.status(HttpStatus.OK).body(paymentService.getAdminContainerStat());
  }


  	/*-----------------------------------------------------------------------------------------/
	 /*        Statistiques pour le compte superviseur dans la section container principal     /
	/*----------------------------------------------------------------------------------------*/

  @RolesAllowed({SUPERVISOR})
  @GetMapping("/supervisor-container-stat")
  public ResponseEntity<SupervisorContainer> getSupervisorContainerStat(@RequestParam UUID supervisor){
    return ResponseEntity.status(HttpStatus.OK).body(paymentService.getSupervisorStat(supervisor));
  }

      /*----------------------------------------------------------------------------------------------/
	 /*        Statistiques pour le compte applicatif, le nombre de paiement succèes et en cours     /
	/*---------------------------------------------------------------------------------------------*/


	@RolesAllowed({SUPERVISOR,ADMIN, APPLICATION})
	@GetMapping("/application-container-stat")
	public ResponseEntity<ApplicatifContainer> getApplicationContainerStat(@RequestParam UUID applicationId){
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.getContainerStatApplicatifState(applicationId));
	}

	/*-----------------------------------------------------/
	/*         Statistiques d'un compte applicatif        /
	/*--------------------------------------------------*/
		@RolesAllowed({ADMIN, USER, SUPERVISOR, APPLICATION})
		@GetMapping("/statistics/app/{compteApplicatifId}")
		public ResponseEntity<?> getStatisticsApp (@PathVariable UUID compteApplicatifId){
			Statistics statistics = new Statistics();
			statistics.setFailedTransactionsVolume(platformService.getTotalTransactionsByStatusApp(compteApplicatifId, FAILURE));
			statistics.setTotalFailedTransactions(platformService.getTotalTransactionsByStatusApp(compteApplicatifId, FAILURE));
			statistics.setTotalSuccessfulTransactions(platformService.getTotalTransactionsByStatusApp(compteApplicatifId, SUCCESS));
			statistics.setTotalInitiatedTransactions(platformService.getTotalTransactionsByStatusApp(compteApplicatifId, INITIATE));
			statistics.setTotalFailedTransactions(platformService.getTotalTransactionsByStatusApp(compteApplicatifId, FAILURE));
			statistics.setFailedTransactionsVolume(platformService.getTransactionsVolumeByStatusApp(compteApplicatifId, FAILURE));
			statistics.setSuccessfulTransactionsVolume(platformService.getTransactionsVolumeByStatusApp(compteApplicatifId, SUCCESS));
			statistics.setInitiatedTransactionsVolume(platformService.getTransactionsVolumeByStatusApp(compteApplicatifId, INITIATE));
			statistics.setMonthlyPaymentsCount(platformService.getMonthlyDepositCountApp(compteApplicatifId));
			statistics.setCommissionsVolume(platformService.getCommissionsVolumeApp(compteApplicatifId));
			statistics.getTransactionsCountByStatus().put(SUCCESS, platformService.getTotalTransactionsByStatusApp(compteApplicatifId, SUCCESS));
			statistics.getTransactionsCountByStatus().put(FAILURE, platformService.getTotalTransactionsByStatusApp(compteApplicatifId, FAILURE));
			statistics.getTransactionsCountByStatus().put(INITIATE, platformService.getTotalTransactionsByStatusApp(compteApplicatifId, INITIATE));
			statistics.getTransactionsVolumeByStatus().put(SUCCESS, platformService.getTransactionsVolumeByStatusApp(compteApplicatifId, SUCCESS));
			statistics.getTransactionsVolumeByStatus().put(FAILURE, platformService.getTransactionsVolumeByStatusApp(compteApplicatifId, FAILURE));
			statistics.getTransactionsVolumeByStatus().put(INITIATE, platformService.getTransactionsVolumeByStatusApp(compteApplicatifId, INITIATE));
			statistics.setTransactionsVolume(platformService.getTransactionsVolumeApp(compteApplicatifId));
			statistics.setMonthlyPaymentsCount(platformService.getMonthlyDepositCountApp(compteApplicatifId));
			statistics.setMonthlyTransactionsCountByStatus(platformService.getMonthlyTransactionsByStatusApp(compteApplicatifId));
			statistics.setTransactionsByPaymentMethodCategory(platformService.getTransactionsByPaymentMethodCategoryApp(compteApplicatifId));
			statistics.setVolumeOfTransactionsByPaymentMethod(platformService.volumeOfTransactionsByPaymentMethodApp(compteApplicatifId));
			statistics.setVolumeOfTransactionsByPaymentMethodCategory(platformService.getVolumeOfTransactionsByPaymentMethodCategoryApp(compteApplicatifId));
			statistics.setTotalNumberOfTransactions(platformService.getTotalNumberOfTransactionsAppp(compteApplicatifId));
			statistics.setPaymentsVolume(platformService.getPaymentsVolumeApp(compteApplicatifId));
			statistics.setNumberOfTransactionsByPaymentMethod(platformService.numberOfTransactionsByPaymentMethodApp(compteApplicatifId));
			statistics.setMonthlyPaymentsVolume(platformService.getMonthlyDepositVolumeApp(compteApplicatifId));
			statistics.setMonthlyTransactionsVolumeByStatus(platformService.getMonthlyTransactionVolumeByStatusApp(compteApplicatifId));
			statistics.setAccountBalance(platformService.getAccountBalanceApp(compteApplicatifId));
			statistics.setBalancePerMonth(platformService.getMonthlyBalanceApp(compteApplicatifId));
			statistics.setBalancePaymentPerMonth(platformService.getBalanceAndPaymentPerMonthApp(compteApplicatifId));
			return new ResponseEntity<Statistics>(statistics, HttpStatus.OK);
		}


}
