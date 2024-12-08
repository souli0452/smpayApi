package com.switchmaker.smpay.serviceImplement;


import com.switchmaker.smpay.dto.ValidPaymentInformations;
import com.switchmaker.smpay.entities.*;
import com.switchmaker.smpay.repository.*;
import com.switchmaker.smpay.services.BalanceEvolutionService;
import com.switchmaker.smpay.services.CostomerRateService;
import com.switchmaker.smpay.services.CostomerService;
import com.switchmaker.smpay.services.PlatformService;
import com.switchmaker.smpay.utilsClass.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;

@Service
public class PlatformServiceImp implements PlatformService {

	@Autowired
	CostomerService costomerService;
	@Autowired
	CostomerRateService costomerRateService;
	@Autowired
	PlatformRepository platformRepo;

	@Autowired
	MeansPaymentRepository meansPaymentRepository;

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	PaymentRequestRepository paymentRequestRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	BalanceEvolutionService balanceEvolutionService;

	@Autowired
	BalanceEvolutionRepository balanceEvolutionRepository;
	@Override
	public Platform savePlatform(Platform platform) {
		platform.setPlatformCode(UtilsClass.codeGenerate());
		LocalDateTime date = LocalDateTime.now();
		platform.setCreationDate(date);
	//	platform.setBalanceModificationDate(date);
		return platformRepo.save(platform);
	}
	@Override
	public Platform getByUserAndPlatformName(User user, String platformName) {
		// TODO Auto-generated method stub
		return platformRepo.findByUserAndPlatformName(user, platformName);
	}
	@Override
	public List<Platform> getAllPlatforms() {
		// TODO Auto-generated method stub
		return platformRepo.findAllPlatforms();
	}
	@Override
	public Platform updatePlatform(UUID platformId, Platform platform) {
		// TODO Auto-generated method stub
		Platform currentPlatform = platformRepo.findById(platformId).get();
		if (platform.getPlatformName() != null){
			currentPlatform.setPlatformName(platform.getPlatformName());
		}

		if (platform.getPaymentAccount() != null){
			currentPlatform.setPaymentAccount(platform.getPaymentAccount());
		}

	/*	// Check for duplicate platform name (excluding the current platform)
		if (platform.getPlatformName() != null &&
				platformRepo.existsByPlatformNameAndIdNot(platform.getPlatformName(), platformId)) {
			throw new RuntimeException("\"Plateforme existe déjà !\"");
		}

		// Update platform properties
		currentPlatform.setPlatformName(platform.getPlatformName());
		currentPlatform.setPaymentAccount(platform.getPaymentAccount());*/

		return platformRepo.save(currentPlatform);
	}

	@Override
	public Platform getPlatformById(UUID platformId) {
		// TODO Auto-generated method stub
		return platformRepo.findById(platformId).get();
	}
	@Override
	public void deletePlatform(UUID platformId) {
		// TODO Auto-generated method stub
		platformRepo.deleteById(platformId);
	}
	@Override
	public List<Platform> getCostomerPlatforms(UUID costomerId) {
		// TODO Auto-generated method stub
		return platformRepo.findCostomerPlatforms(costomerId);
	}

	@Override
	public Platform getPlatformByCostomerCodeAndPlatformCode(String costomerCode, String platformCode) {
		// TODO Auto-generated method stub
		return platformRepo.findPlatformByCostomerCodeAndPlatformCode(costomerCode, platformCode);
	}


	@Override
	public Platform balanceUpdate(ValidPaymentInformations validPaymentInformations) {
		// TODO Auto-generated method stub
		validPaymentInformations.getPlatform().setBalance(UtilsClass.newBalanceOfPlatform(validPaymentInformations));
  		LocalDateTime date = LocalDateTime.now();
  	//	validPaymentInformations.getPlatform().setBalanceModificationDate(date);
		return platformRepo.save(validPaymentInformations.getPlatform());
	}
	@Override
	public int countPlatforms() {
		// TODO Auto-generated method stub
		return platformRepo.findCountPlatform();
	}
	@Override
	public Platform getPlatformByCode(String platformCode) {
		// TODO Auto-generated method stub
		return platformRepo.findByPlatformCode(platformCode);
	}


	/*----------------------------------------------------------------------------------------------------------------/
	/ 										Statistique 														   	/
    /				cette méthode compte le nombre total de transactions associées à un compte applicatif    	   /
	/		en agrégeant les paiements de toutes les plateformes associées à ce compte applicatif 				  /
	/		dans une liste globale, puis en renvoyant la taille de cette liste						            /
	/*--------------------------------------------------------------------------------------------------------*/

	@Override
	public int getTotalNumberOfTransactionsAppp(UUID applicatifId) {
		Platform platform=platformRepo.findById(applicatifId).get();
		List<Payment> paymentList;
		paymentList = paymentRepository.findByPlatform(platform);

		return paymentList.size();
	}


	/*------------------------------------------------------------------------------------------------------------------------------/
	/*  							            		 Statistique 										               			/
	/		méthode pour recuperer le nombre total des transactions d'un compte applicatif par statut(reussir, échec, en cours)   /
	/*---------------------------------------------------------------------------------------------------------------------- ---*/

	@Override
	public int getTotalTransactionsByStatusApp(UUID applicatifId,String status) {
		Platform platform=platformRepo.findById(applicatifId).get();
		List<Payment> paymentList;
		paymentList = paymentRepository.findByPlatform(platform).stream().filter(payment -> payment.getStatus().compareToIgnoreCase(status)==0).collect(Collectors.toList());
		return paymentList.size();
	}


	/*-----------------------------------------------------------------------------------------------------------------------/
	/*  							            		 Statistique 										          		/
	/		méthode pour recuperer le volume total des transactions d'un compte applicatif                                 /
	/*--------------------------------------------------------------------------------------------------------------- ---*/


	@Override
	public double getTransactionsVolumeApp(UUID applicatifId) {
		Platform platform=platformRepo.findById(applicatifId).get();
		AtomicReference<Double> volume = new AtomicReference<>((double) 0);
			List<Payment> paymentList = paymentRepository.findByPlatform(platform);
			paymentList.forEach(payment -> {
				volume.updateAndGet(currentVolume -> currentVolume + payment.getPaymentAmount());
			});


		return volume.get();
	}


	/*------------------------------------------------------------------------------------------------------------------------------/
	/*  							            		 Statistique 										               			/
	/		méthode pour recuperer le volume total des transactions d'un compte applicatif par statut(reussir, échec, en cours)   /
	/*---------------------------------------------------------------------------------------------------------------------- ---*/

	@Override
	public double getTransactionsVolumeByStatusApp(UUID applicatifId, String status) {
		Platform platform=platformRepo.findById(applicatifId).get();
		AtomicReference<Double> volume = new AtomicReference<>((double) 0);

			List<Payment> paymentList = paymentRepository.findByPlatform(platform);
			paymentList.stream()
					.filter(payment -> payment.getStatus().equalsIgnoreCase(status))
					.forEach(payment -> volume.updateAndGet(currentVolume -> currentVolume + payment.getPaymentAmount()));

		return volume.get();
	}

	/*-------------------------------------------------------------------------------------/
	/*  					    	Statistique 							       	   	/
	/		  méthode pour recuperer le volume des commissions d'un compte applicatif    /
	/*----------------------------------------------------------------------------- ---*/

	@Override
	public double getCommissionsVolumeApp(UUID applicatifId) {
		Platform platform=platformRepo.findById(applicatifId).get();
		AtomicReference<Double> volume = new AtomicReference<>((double) 0);

			List<Payment> paymentList = paymentRepository.findByPlatform(platform);
			paymentList.forEach(payment -> {
				volume.updateAndGet(currentVolume -> currentVolume + payment.getCommissionAmount());
			});

		return volume.get();
	}


	/*-------------------------------------------------------------------------------------/
	/*  					    	Statistique 							       	   	/
	/		  méthode pour recuperer le volume des versements d'un compte applicatif    /
	/*----------------------------------------------------------------------------- ---*/

	@Override
	public double getPaymentsVolumeApp(UUID applicatifId) {
		Platform platform=platformRepo.findById(applicatifId).get();
		AtomicReference<Double> volume = new AtomicReference<>((double) 0);

			paymentRequestRepository.findByPlatform(platform).stream().filter(paymentRequest -> paymentRequest.getStatus().compareToIgnoreCase(SUCCESS)==0).collect(Collectors.toList()).forEach(r->{
				volume.updateAndGet(currentVolume -> currentVolume + r.getAmount());
			});

		return volume.get();

	}

	/*-----------------------------------------------------------------------------------------------/
	/*  					    	Statistique 							       	            	/
	/        méthode pour recuperer le nombre de transactions par moyens                           /
    /    				 de paiement d'un compte applicatif         							   /
	/*--------------------------------------------------------------------------------------- ---*/

	@Override
	public Map<String,Map<String, Object>> numberOfTransactionsByPaymentMethodApp(UUID compteApplicatifId) {
		List<Payment> payments = getAllSupervisorTransactionsApp(compteApplicatifId);

		Map<String, Map<String, Object>> result = new HashMap<>();

		for (Payment payment : payments) {
			String paymentType = payment.getPaymentType();
			Map<String, Object> paymentInfo = result.getOrDefault(paymentType, new HashMap<>());

			Long count = (Long) paymentInfo.getOrDefault("count", 0L);
			paymentInfo.put("count", count + 1);
			paymentInfo.put("color", meansPaymentRepository.getMeansPaymentByCode(payment.getServiceCode()).getColorCode());

			result.put(paymentType, paymentInfo);
		}

		return result;
	}


	private List<Payment> getAllSupervisorTransactionsApp(UUID applicatifId) {
		Platform platform=platformRepo.findById(applicatifId).get();
		List<Payment> paymentList=new ArrayList<>();

			paymentList.addAll(paymentRepository.findByPlatform(platform));
		return paymentList;
	}

	/*-----------------------------------------------------------------------------------------------/
	/*  					    	Statistique 							       	            	/
	/           méthode pour recuperer le volume de transactions par moyens                        /
    /   				 de paiement d'un compte applicatif         							   /
	/*--------------------------------------------------------------------------------------- ---*/


	@Override
	public Map<String, Map<String, Object>>volumeOfTransactionsByPaymentMethodApp(UUID compteApplicatifId) {
		List<Payment> payments = getAllSupervisorTransactionsApp(compteApplicatifId);

		Map<String, Map<String, Object>> result = new HashMap<>();

		for (Payment payment : payments) {
			String paymentType = payment.getPaymentType();
			Map<String, Object> paymentInfo = result.getOrDefault(paymentType, new HashMap<>());

			Integer volume = (Integer) paymentInfo.getOrDefault("volume", 0);
			paymentInfo.put("volume", volume + payment.getPaymentAmount());
			paymentInfo.put("color", meansPaymentRepository.getMeansPaymentByCode(payment.getServiceCode()).getColorCode());

			result.put(paymentType, paymentInfo);
		}

		return result;

	}



	/*-----------------------------------------------------------------------------------------------/
	/*  					    	Statistique 							       	            	/
	/            méthode pour recuperer le nombre des transactions par catégorie					/
    /    				 de moyens de paiement d'un compte applicatif        					   /
	/*--------------------------------------------------------------------------------------- ---*/

	private List<MeansPayment> getTransactionMeansPaymentApp(List<Payment> paymentList){
		List<MeansPayment> meansPayments=new ArrayList<>();
		paymentList.forEach(p->{
			MeansPayment meansPayment=meansPaymentRepository.getMeansPaymentByCode(p.getServiceCode());
			meansPayments.add(meansPayment);
		});
		return meansPayments;
	}
	private Set<Category> getMeansPaymentCategorieApp(List<MeansPayment> meansPayments){
		Set<Category> categories = new HashSet<>();
		meansPayments.forEach(m->{
			Category category=categoryRepository.findById(m.getCategory().getId()).get();
			categories.add(category);
		});
		return categories;
	}

	@Override
	public Map<String,Long> getTransactionsByPaymentMethodCategoryApp(UUID compteApplicatifId){
		List<Payment> paymentList=getAllSupervisorTransactionsApp(compteApplicatifId);
		List<MeansPayment> meansPayments=getTransactionMeansPaymentApp(paymentList);
		Set<Category> categories=getMeansPaymentCategorieApp(meansPayments);
		return categories.stream().collect(Collectors.groupingBy(Category::getWording, Collectors.counting()));
	}

	/*-----------------------------------------------------------------------------------------------/
	/*  					    	Statistique 							       	            	/
	/             méthode pour recuperer le nombre des transactions mensuelles par					/
    /   							statut d'un compte applicatif    	    					   /
	/*--------------------------------------------------------------------------------------- ---*/

	@Override
	public Map<String, Map<String, Long>> getMonthlyTransactionsByStatusApp(UUID compteApplicatifId) {
		// Récupérer toutes les transactions
		List<Payment> paymentList = getAllSupervisorTransactionsApp(compteApplicatifId);

		// Obtenir tous les statuts uniques
/*
		Set<String> allStatuses = paymentList.stream()
				.map(Payment::getStatus)
				.collect(Collectors.toSet());
*/

		Set<String> allStatuses =  new HashSet<>();
		allStatuses.add(SUCCESS);
		allStatuses.add(INITIATE);
		allStatuses.add(FAILURE);

		// Initialiser la Map avec tous les mois de l'année et tous les statuts à 0
		Map<String, Map<String, Long>> monthlyTransactions = new LinkedHashMap<>();
		for (Month month : Month.values()) {
			Map<String, Long> statusMap = new HashMap<>();
			for (String status : allStatuses) {
				statusMap.put(status, 0L);
			}
			monthlyTransactions.put(month.getDisplayName(TextStyle.FULL, Locale.FRENCH), statusMap);
		}

		// Grouper par mois et par statut, puis compter le nombre de transactions pour chaque groupe
		Map<YearMonth, Map<String, Long>> transactionsByMonthAndStatus = paymentList.stream()
				.collect(Collectors.groupingBy(
						payment -> YearMonth.from(payment.getPaymentDate()), // Grouper par mois
						Collectors.groupingBy(
								Payment::getStatus, // Grouper par statut
								Collectors.counting() // Compter le nombre de transactions
						)
				));

		// Ajouter les transactions aux mois correspondants
		for (Map.Entry<YearMonth, Map<String, Long>> entry : transactionsByMonthAndStatus.entrySet()) {
			String month = entry.getKey().getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
			Map<String, Long> existingValues = monthlyTransactions.get(month);
			Map<String, Long> newValues = entry.getValue();

			// Fusionner les valeurs existantes avec les nouvelles valeurs
			for (Map.Entry<String, Long> statusEntry : newValues.entrySet()) {
				existingValues.merge(statusEntry.getKey(), statusEntry.getValue(), Long::sum);
			}
		}

		return monthlyTransactions;
	}

	/*-----------------------------------------------------------------------------------------------/
	/*  					    	Statistique 							       	            	/
	/             méthode pour recuperer le volume des transactions mensuelles par					/
    /   							statut d'un compte applicatif    	    					   /
	/*--------------------------------------------------------------------------------------- ---*/


	@Override
	public Map<String, Map<String, Double>> getMonthlyTransactionVolumeByStatusApp(UUID compteApplicatifId) {
		// Récupérer toutes les transactions
		List<Payment> paymentList = getAllSupervisorTransactionsApp(compteApplicatifId);

	/*	// Obtenir tous les statuts uniques
		Set<String> allStatuses = paymentList.stream()
				.map(Payment::getStatus)
				.collect(Collectors.toSet());*/

		Set<String> allStatuses =  new HashSet<>();
		allStatuses.add(SUCCESS);
		allStatuses.add(INITIATE);
		allStatuses.add(FAILURE);

		// Initialiser la Map avec tous les mois de l'année et tous les statuts à 0
		Map<String, Map<String, Double>> monthlyTransactions = new LinkedHashMap<>();
		for (Month month : Month.values()) {
			Map<String, Double> statusMap = new HashMap<>();
			for (String status : allStatuses) {
				statusMap.put(status, 0.0);
			}
			monthlyTransactions.put(month.getDisplayName(TextStyle.FULL, Locale.FRENCH), statusMap);
		}

		// Grouper par mois et par statut, puis sommer le montant des paiements pour chaque groupe
		Map<YearMonth, Map<String, Double>> transactionsByMonthAndStatus = paymentList.stream()
				.collect(Collectors.groupingBy(
						payment -> YearMonth.from(payment.getPaymentDate()), // Grouper par mois
						Collectors.groupingBy(
								Payment::getStatus, // Grouper par statut
								Collectors.summingDouble(Payment::getPaymentAmount) // Somme des montants des paiements
						)
				));

		// Ajouter les paiements aux mois et statuts correspondants
		for (Map.Entry<YearMonth, Map<String, Double>> entry : transactionsByMonthAndStatus.entrySet()) {
			String month = entry.getKey().getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
			Map<String, Double> existingValues = monthlyTransactions.get(month);
			Map<String, Double> newValues = entry.getValue();

			// Fusionner les valeurs existantes avec les nouvelles valeurs
			for (Map.Entry<String, Double> statusEntry : newValues.entrySet()) {
				existingValues.merge(statusEntry.getKey(), statusEntry.getValue(), Double::sum);
			}
		}

		return monthlyTransactions;
	}


		/*-----------------------------------------------------------------------------------------------/
		/*  					    	Statistique 							       	            	/
		/            méthode pour recuperer le nombre des versements mensuels d'un compte applicatif   /
		/*--------------------------------------------------------------------------------------- ---*/

	private List<PaymentRequest> getAllSupervisorDepositsApp(UUID applicatifId) {
		Platform platform=platformRepo.findById(applicatifId).get();
		List<PaymentRequest> depositList = new ArrayList<>();

			depositList.addAll(paymentRequestRepository.findByPlatform(platform));

		return depositList;
	}

	@Override
	public Map<String, Long> getMonthlyDepositCountApp(UUID compteApplicatifId) {
		// Récupérer tous les versements d'un superviseur spécifique
		List<PaymentRequest> depositList = getAllSupervisorDepositsApp(compteApplicatifId);

		// Initialiser la Map avec tous les mois de l'année à 0
		Map<String, Long> monthlyDeposits = new LinkedHashMap<>();
		for (Month month : Month.values()) {
			monthlyDeposits.put(month.getDisplayName(TextStyle.FULL, Locale.FRENCH), 0L);
		}

		// Grouper par mois, puis compter le nombre de versements pour chaque groupe
		Map<String, Long> depositsByMonth = depositList.stream()
				.collect(Collectors.groupingBy(
						deposit -> YearMonth.from(deposit.getSendingDate()).getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH), // Grouper par mois
						Collectors.counting() // Compter le nombre de versements
				));

		// Ajouter les versements aux mois correspondants
		for (Map.Entry<String, Long> entry : depositsByMonth.entrySet()) {
			monthlyDeposits.put(entry.getKey(), entry.getValue());
		}

		return monthlyDeposits;
	}


	/*-----------------------------------------------------------------------------------------------/
	/*  					    	Statistique 							       	             	/
	/         méthode pour recuperer le volume des versements mensuels d'un compte applicatif      /
	/*--------------------------------------------------------------------------------------- ---*/

	@Override
	public Map<String, Double> getMonthlyDepositVolumeApp(UUID compteApplicatifId) {
		// Récupérer toutes les demandes de versement
		List<PaymentRequest> paymentRequests = getAllSupervisorDepositsApp(compteApplicatifId);

		// Initialiser la Map avec tous les mois de l'année à 0
		Map<String, Double> monthlyDeposits = new LinkedHashMap<>();
		for (Month month : Month.values()) {
			monthlyDeposits.put(month.getDisplayName(TextStyle.FULL, Locale.FRENCH), 0.0);
		}

		// Grouper par mois, puis sommer le montant des versements pour chaque groupe
		Map<YearMonth, Double> depositsByMonth = paymentRequests.stream()
				.collect(Collectors.groupingBy(
						paymentRequest -> YearMonth.from(paymentRequest.getSendingDate()), // Grouper par mois
						Collectors.summingDouble(PaymentRequest::getAmount) // Somme des montants des versements
				));

		// Ajouter les versements aux mois correspondants
		for (Map.Entry<YearMonth, Double> entry : depositsByMonth.entrySet()) {
			String month = entry.getKey().getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
			monthlyDeposits.put(month, entry.getValue());
		}

		return monthlyDeposits;
	}


	/*-----------------------------------------------------------------------------------------------/
	/*  					    	Statistique 							       	            	/
	/            méthode pour recuperer le volume total des transactions par catégorie	    		/
    /    				 de moyens de paiement d'un compte applicatif        					   /
	/*--------------------------------------------------------------------------------------- ---*/


	@Override
	public Map<String, Long> getVolumeOfTransactionsByPaymentMethodCategoryApp(UUID compteApplicatifId) {
		List<Payment> paymentList = getAllSupervisorTransactionsApp(compteApplicatifId);
		List<MeansPayment> meansPayments = getTransactionMeansPaymentApp(paymentList);
		Set<Category> categories = getMeansPaymentCategorieApp(meansPayments);
		Map<String, Long> resultat = new HashMap<>();
		categories.forEach(c -> {
			double volume = 0;
			for (Payment p : paymentList) {
				if (meansPaymentRepository.findByCode(p.getServiceCode()).getCategory().getWording().equalsIgnoreCase(c.getWording())) {
					volume += p.getPaymentAmount();
				}
			}
			resultat.put(c.getWording(), (long) volume);
		});

		return resultat;
	}


	/*-----------------------------------------------------------------------------------------------/
	/*  					    	Statistique 							       	            	/
	/           méthode pour recuperer le solde du compte d'un compte applicatif    	    	   /
	/*--------------------------------------------------------------------------------------- ---*/

	@Override
	public double getAccountBalanceApp(UUID applicatifId){
		Platform platform=platformRepo.findById(applicatifId).get();

		return  platform.getBalance();
	}


	/*-----------------------------------------------------------------------------------------------/
	/*  					    	Statistique 							       	             	/
	/                         méthode pour recuperer le solde par mois d'un compte applicatif      /
	/*--------------------------------------------------------------------------------------- ---*/


	private List<BalanceEvolution> getBalanceEvolutionApp(Platform platform){
		return balanceEvolutionService.getPlatformeBalanceEvolutionSolde(platform);
	}
	@Override
	public Map<String, Double> getMonthlyBalanceApp(UUID applicatifId) {
		List<Platform> platforms = new ArrayList<>();
		platforms.add(platformRepo.findById(applicatifId).get());

		// Initialiser la Map avec tous les mois de l'année à 0
		Map<String, Double> monthlyBalance = new LinkedHashMap<>();
		for (Month month : Month.values()) {
			monthlyBalance.put(month.getDisplayName(TextStyle.FULL, Locale.FRENCH), 0.0);
		}

		for (Platform platform : platforms) {
			List<BalanceEvolution> balanceEvolutions = getBalanceEvolutionApp(platform);
			for (BalanceEvolution balanceEvolution : balanceEvolutions) {
				YearMonth yearMonth = YearMonth.from(balanceEvolution.getOperationDate());
				String month = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
				double balance = balanceEvolution.getBalance();
				monthlyBalance.put(month, monthlyBalance.get(month) + balance);
			}
		}
		return monthlyBalance;
	}


	/*-----------------------------------------------------------------------------------------------/
	/*  					    	Statistique 							       	             	/
	/          méthode pour recuperer le solde et versement par mois d'un compte applicatif        /
	/*--------------------------------------------------------------------------------------- ---*/


	@Override
	public Map<String, Map<String, Number>> getBalanceAndPaymentPerMonthApp(UUID applicatifId) {
		List<Platform> platforms = new ArrayList<>();
		platforms.add(platformRepo.findById(applicatifId).get());

		// Initialisez la Map avec tous les mois de l'année
		Map<String, Map<String, Number>> result = new HashMap<>();
		for (Month month : Month.values()) {
			String monthName = month.getDisplayName(TextStyle.FULL, Locale.FRENCH);
			Map<String, Number> balanceAndPayment = new HashMap<>();
			balanceAndPayment.put("solde", 0.0);
			balanceAndPayment.put("versement", 0);
			result.put(monthName, balanceAndPayment);
		}

		for (Platform platform : platforms) {
			List<BalanceEvolution> balanceEvolutions = balanceEvolutionRepository.findByPlatform(platform);
			for (BalanceEvolution balanceEvolution : balanceEvolutions) {
				YearMonth yearMonth = YearMonth.from(balanceEvolution.getOperationDate());
				String month = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
				result.get(month).merge("solde", balanceEvolution.getBalance(), (a, b) -> ((Number) a).doubleValue() + ((Number) b).doubleValue());
			}

			List<PaymentRequest> paymentRequests = paymentRequestRepository.findByPlatformAndStatus(platform, "success");
			for (PaymentRequest paymentRequest : paymentRequests) {
				YearMonth yearMonth = YearMonth.from(paymentRequest.getProcessingDate());
				String month = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
				result.get(month).merge("versement", paymentRequest.getAmount(), (a, b) -> ((Number) a).intValue() + ((Number) b).intValue());
			}
		}
		return result;
	}

	@Override
	public List <Platform> getClientPlatformApp(UUID compteApplicatifId) {
			User user = userRepository.findById(compteApplicatifId).get();
			List <Platform> platform =user.getPlatforms();
			return platform;
	}

	@Override
	public User getAccountApp(UUID applicatifId){
		User user = userRepository.findAll().stream().filter(u->u.getPlatforms().contains(platformRepo.findById(applicatifId).get())).collect(Collectors.toList()).get(0);
		return  user;
	}

	@Override
	public List <PaymentRequest> getClientPlatformAppPaymentRequest(UUID compteApplicatifId) {
		Platform platform = getClientPlatformApp(compteApplicatifId).get(0);
		List <PaymentRequest> paymentRequest = paymentRequestRepository.findByPlatform(platform).stream().filter(p->p.getStatus().equalsIgnoreCase(INITIATE) || p.getStatus().equalsIgnoreCase(PROCESSING)).collect(Collectors.toList());
		return paymentRequest;
	}

	@Override
	public List <PaymentRequest> getClientPlatformAppPaymentRequestSuccess(UUID compteApplicatifId) {
		 Platform platform = getClientPlatformApp(compteApplicatifId).get(0);
		List <PaymentRequest> paymentRequest = paymentRequestRepository.findByPlatform(platform).stream().filter(p->p.getStatus().compareToIgnoreCase(SUCCESS) == 0 ).sorted((p1, p2) -> p2.getSendingDate().compareTo(p1.getSendingDate())).collect(Collectors.toList());
		return paymentRequest;
	}

}
