package com.switchmaker.smpay.serviceImplement;

import com.switchmaker.smpay.dto.*;
import com.switchmaker.smpay.entities.*;
import com.switchmaker.smpay.moov_bf.MoovServices;
import com.switchmaker.smpay.mtn_ci.MtnServices;
import com.switchmaker.smpay.repository.*;
import com.switchmaker.smpay.services.BalanceEvolutionService;
import com.switchmaker.smpay.services.PaymentService;
import com.switchmaker.smpay.services.PlatformService;
import com.switchmaker.smpay.services.UserService;
import com.switchmaker.smpay.utilsClass.UtilsClass;
import com.switchmaker.smpay.wave_ci.WaveServices;
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
public class PaymentServiceImp implements PaymentService {

	@Autowired
	PaymentRepository paymentRepo;
	@Autowired
	UserService userService;
	@Autowired
	PlatformService platformService;
	@Autowired
	MoovServices moovServices;
	@Autowired
	WaveServices waveServices;
	@Autowired
	MtnServices mtnServices;
  @Autowired
  PlatformRepository platformRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  PaymentRequestRepository paymentRequestRepository;
  @Autowired
  MeansPaymentRepository meansPaymentRepository;
  @Autowired
  CategoryRepository categoryRepository;
  @Autowired
  private BalanceEvolutionService balanceEvolutionService;
  @Autowired
  private  BalanceEvolutionRepository balanceEvolutionRepository;
  @Autowired
  private CostomerRepository costomerRepository;

	@Override
	public int countTransactions() {
		// TODO Auto-generated method stub
		return paymentRepo.findCountTransaction();
	}

	@Override
	public int countTransactionByStatus(String status) {
		// TODO Auto-generated method stub
		return paymentRepo.countTransanctionByStatus(status);
	}

    @Override
    public int amountTransactionByStatus(String status) {
        List<Platform> platformList=platformRepository.findAll();
        List<Payment> paymentList=new ArrayList<>();
        platformList.forEach(p->{
            paymentList.addAll(paymentRepo.findByPlatform(p).stream().filter(payment -> payment.getStatus().compareToIgnoreCase(status)==0).collect(Collectors.toList()));
        });
        return paymentList.size();
        //return paymentRepo.amountTransactionByStatus(status);
    }

//	@Override
//	public int amountTransactionByStatus(String status) {
//		// TODO Auto-generated method stub
//		if(paymentRepo.amountTransactionByStatus(status)!=null) {
//			return paymentRepo.amountTransactionByStatus(status);
//		}
//		return 0;
//	}

	@Override
	public double amountTransaction() {
		// TODO Auto-generated method stub
      //return  paymentRepo.amountTransaction();
        AtomicReference<Double> volume = new AtomicReference<>((double) 0);

            List<Payment> paymentList = paymentRepo.findAll();
            paymentList.forEach(payment -> {
                volume.updateAndGet(currentVolume -> currentVolume + payment.getPaymentAmount());
            });
        return volume.get();
    }

  /**
   * cette méthode compte le nombre total de transactions associées à un superviseur en agrégeant les paiements de toutes les plateformes associées à ce superviseur dans une liste globale, puis en renvoyant la taille de cette liste
   * @param supervisor
   * @return
   */

  @Override
  public int getTotalNumberOfTransactions(UUID supervisor) {
    List<Platform> platformList=platformRepository.findByUser(userRepository.findById(supervisor).get());
    List<Payment> paymentList=new ArrayList<>();
    platformList.forEach(p->{
      paymentList.addAll(paymentRepo.findByPlatform(p));
    });
    return paymentList.size();
  }

  /**
   * cette méthode compte le nombre total de transactions avec un statut spécifique associées à un superviseur en filtrant les paiements pour chaque plateforme et en agrégeant les résultats dans une liste globale
   * @param supervisor
   * @param status
   * @return
   */
  @Override
  public int getTotalTransactionsByStatus(UUID supervisor,String status) {
    List<Platform> platformList=platformRepository.findByUser(userRepository.findById(supervisor).get());
    List<Payment> paymentList=new ArrayList<>();
    platformList.forEach(p->{
      paymentList.addAll(paymentRepo.findByPlatform(p).stream().filter(payment -> payment.getStatus().compareToIgnoreCase(status)==0).collect(Collectors.toList()));
    });
    return paymentList.size();
  }

  /**
   * cette méthode calcule le volume total des transactions associées à un superviseur en additionnant le montant de chaque paiement effectué sur les plateformes associées à ce superviseur
   * @param supervisor
   * @return
   */
  @Override
  public double getTransactionsVolume(UUID supervisor) {
    List<Platform> platformList = platformRepository.findByUser(userRepository.findById(supervisor).orElse(null));
    AtomicReference<Double> volume = new AtomicReference<>((double) 0);

    platformList.forEach(platform -> {
      List<Payment> paymentList = paymentRepo.findByPlatform(platform);
      paymentList.forEach(payment -> {
        volume.updateAndGet(currentVolume -> currentVolume + payment.getPaymentAmount());
      });
    });

    return volume.get();
  }


  /**
   * cette méthode permet de calculer le volume total des paiements associés à un superviseur, en tenant compte uniquement des paiements ayant le statut spécifié
   * @param supervisor
   * @param status
   * @return
   */

  @Override
  public double getTransactionsVolumeBystatus(UUID supervisor, String status) {
    List<Platform> platformList = platformRepository.findByUser(userRepository.findById(supervisor).orElse(null));
    AtomicReference<Double> volume = new AtomicReference<>((double) 0);
    platformList.forEach(platform -> {
      List<Payment> paymentList = paymentRepo.findByPlatform(platform);
      paymentList.stream()
        .filter(payment -> payment.getStatus().equalsIgnoreCase(status))
        .forEach(payment -> volume.updateAndGet(currentVolume -> currentVolume + payment.getPaymentAmount()));
    });

    return volume.get();
  }

  /**
   * Cette méthode a pour objectif de calculer et de renvoyer le volume total des commissions associées à un superviseur pour toutes les transactions effectuées sur les plateformes associées à ce superviseur
   * @param supervisor
   * @return
   */
  @Override
  public double getCommissionsVolume(UUID supervisor) {
    List<Platform> platformList = platformRepository.findByUser(userRepository.findById(supervisor).orElse(null));
    AtomicReference<Double> volume = new AtomicReference<>((double) 0);

    platformList.forEach(platform -> {
      List<Payment> paymentList = paymentRepo.findByPlatform(platform);
      paymentList.forEach(payment -> {
        volume.updateAndGet(currentVolume -> currentVolume + payment.getCommissionAmount());
      });
    });

    return volume.get();
  }

  /**
   * Cette méthode a pour but de calculer et de renvoyer le volume total des paiements associés à un superviseur pour les demandes de paiement ayant un statut spécifique (représenté par la constante SUCCESS)
   * @param supervisor
   * @return double
   */
  @Override
  public double getPaymentsVolume(UUID supervisor) {
    List<Platform> platformList = platformRepository.findByUser(userRepository.findById(supervisor).orElse(null));
    AtomicReference<Double> volume = new AtomicReference<>((double) 0);
    platformList.forEach(platform -> {
     paymentRequestRepository.findByPlatform(platform).stream().filter(paymentRequest -> paymentRequest.getStatus().compareToIgnoreCase(SUCCESS)==0).collect(Collectors.toList()).forEach(r->{
       volume.updateAndGet(currentVolume -> currentVolume + r.getAmount());
     });

    });

    return volume.get();

  }
	@Override
	public Double commissionVolume() {
        AtomicReference<Double> volume = new AtomicReference<>((double) 0);

            List<Payment> paymentList = paymentRepo.findAll();
            paymentList.forEach(payment -> {
                volume.updateAndGet(currentVolume -> currentVolume + payment.getCommissionAmount());
            });
        return volume.get();
	}

	@Override
	public Double paymentVolume() {
        AtomicReference<Double> volume = new AtomicReference<>((double) 0);
            paymentRequestRepository.findAll().stream().filter(paymentRequest -> paymentRequest.getStatus().compareToIgnoreCase(SUCCESS)==0).collect(Collectors.toList()).forEach(r->{
                volume.updateAndGet(currentVolume -> currentVolume + r.getAmount());
            });

        return volume.get();
	}

	@Override
	public Map<String, Integer> totalTransactionByAllStatus() {
      List<Object[]> results =paymentRepo.totalTransactionByAllStatus();
      Map<String, Integer> resultMap = new HashMap<>();
      for (Object[] result : results) {
        String status = (String) result[0];
        Integer count = ((Number) result[1]).intValue();
        resultMap.put(status,count);
      }

      return resultMap;
	}

	@Override
	public Map<String, Double> transactionsVolumeByStatus() {
      List<Object[]> results =paymentRepo.transactionsVolumeByStatus();
      Map<String, Double> resultMap = new HashMap<>();
      for (Object[] result : results) {
        String status = (String) result[0];
        Double count = (Double) result[1];
        resultMap.put(status,count);
      }

      return resultMap;
  }

	@Override
	public Map<String, Map<String, Object>> totalTransactionsByPaymentMethod() {
			List<Payment> payments = paymentRepo.findAll();
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

	@Override
	public Map<String, Map<String, Object>> volumeTransactionByPaymentMethod() {
			List<Payment> payments = paymentRepo.findAll();
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

	@Override
	public Map<String,Long> totalTransactionOfPaymentMethodCategory() {
        List<Payment> paymentList=paymentRepo.findAll();
        List<MeansPayment> meansPayments=getTransactionMeansPayment(paymentList);
        Set<Category> categories=getMeansPaymentCategorie(meansPayments);
        return categories.stream()
                .collect(Collectors.groupingBy(Category::getWording, Collectors.counting()));	}

	@Override
	public Map<String, Long> volumeTransactionsByPaymentMethodCategory() {
        List<Payment> paymentList = paymentRepo.findAll();
        List<MeansPayment> meansPayments = getTransactionMeansPayment(paymentList);
        Set<Category> categories = getMeansPaymentCategorie(meansPayments);
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

        return resultat;	}

	@Override
	public Map<String, Map<String, Long>> monthlyPaymentsCountByStatus() {
		return paymentRepo.monthlyPaymentsCountByStatus();
	}

	@Override
	public Map<String, Map<String, Double>> monthlyPaymentsVolumeByStatus() {
		return paymentRepo.monthlyPaymentsVolumeByStatus();
	}

	@Override
	public Map<String, Long> monthlyPaymentsCount() {
      List<PaymentRequest> depositList = paymentRequestRepository.findAll();

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

      return monthlyDeposits;	}

	@Override
	public Map<String, Double> monthlyPaymentsVolume() {
      List<PaymentRequest> paymentRequests = paymentRequestRepository.findAll();

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

	@Override
	public Map<String, Map<String, Long>> monthlyTransactionsCountByStatus() {
      List<Payment> paymentList = paymentRepo.findAll();
      // Obtenir tous les statuts uniques
      //Set<String> allStatuses = paymentList.stream()
      // .map(Payment::getStatus)
      // .collect(Collectors.toSet());
      Set<String> allStatuses=new HashSet<>();
      allStatuses.add(SUCCESS);
      allStatuses.add(FAILURE);
      allStatuses.add(INITIATE);
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
		//return paymentRepo.monthlyTransactionsCountByStatus();
	}

	@Override
	public Map<String, Map<String, Double>> monthlyTransactionsVolumeByStatus() {
      List<Payment> paymentList = paymentRepo.findAll();

      // Obtenir tous les statuts uniques
      Set<String> allStatuses=new HashSet<>();
      allStatuses.add(SUCCESS);
      allStatuses.add(FAILURE);
      allStatuses.add(INITIATE);

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

      return monthlyTransactions;	}

  @Override
  public AdminContainer getAdminContainerStat() {
    return AdminContainer.builder()
      .totalNumberOfPlatform(platformRepository.findAll().size())
      .totalNumberOfCustomer(costomerRepository.findAll().size())
      .totalNumberOfNewPaymentRequest((int) paymentRequestRepository.findAll().stream().filter(p -> p.getStatus().compareToIgnoreCase(INITIATE) == 0 || p.getStatus().compareToIgnoreCase(PROCESSING) == 0).count())
      .totalNumberOfMessage(0)
      .build();
  }

    @Override
    public ApplicatifContainer getContainerStatApplicatifState(UUID compteApplicatifId) {
        return ApplicatifContainer.builder()
                .numberOfcurrentRequestsAndInitiateOfApplicationAccount(platformService.getClientPlatformAppPaymentRequest(compteApplicatifId).size())
                .numberOfSuccessfulRequestsApplicationAccount(platformService.getClientPlatformAppPaymentRequestSuccess(compteApplicatifId).size())
                .build();
    }

  @Override
  public SupervisorContainer getSupervisorStat(UUID supervisor) {
    Optional<User> userOptional = userRepository.findById(supervisor);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      long count = paymentRequestRepository.findByUser(user)
        .stream()
        .filter(p -> !Objects.equals(p.getStatus(), SUCCESS))
        .count();

      long succesCount = paymentRequestRepository.findByUser(user)
        .stream()
        .filter(p -> Objects.equals(p.getStatus(), SUCCESS))
        .count();

      return SupervisorContainer.builder()
        .totalNumberOfPlatform(platformRepository.findByUser(userRepository.findById(supervisor).get()).size()).
        totalNumberOfInitiateOrProcessingPaymentRequest((int) count)
        .totalNumberOfSuccesPaymentRequest((int) succesCount)
          .totalNumberOfCApplicationAccount(userService.getCustomerApplicationAccounts(supervisor).size()).
        build();
    } else {
      throw new RuntimeException("Aucun superviseur trouvé pour l'Id spécifié");
    }




  }

  @Override
  public Map<String, Map<String, Object>> numberOfTransactionsByPaymentMethod(UUID supervisor) {
    List<Payment> payments = getAllSupervisorTransactions(supervisor);

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



  @Override
  public Map<String, Map<String, Object>> volumeOfTransactionsByPaymentMethod(UUID supervisor) {
    List<Payment> payments = getAllSupervisorTransactions(supervisor);

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


  /**
   * cette methode recupere le volume de versemment mensuel lié à un superviseur
   * @param supervisor
   * @return
   */

  @Override
  public Map<String, Double> getMonthlyDepositVolume(UUID supervisor) {
    // Récupérer toutes les demandes de versement
    List<PaymentRequest> paymentRequests = getAllSupervisorDeposits(supervisor);

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


  /**
   * cette methode recupere le nombre versemment mensuel lié a un superviseur
   * @param supervisor
   * @return
   */
  @Override
  public Map<String, Long> getMonthlyDepositCount(UUID supervisor) {
    // Récupérer tous les versements d'un superviseur spécifique
    List<PaymentRequest> depositList = getAllSupervisorDeposits(supervisor);

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



  @Override
  public double getAccountBalance(UUID supervisor){
    List<Platform> platformList = platformRepository.findByUser(userRepository.findById(supervisor).get());
    double solde=0.0;
    for (Platform platform : platformList){
      solde=solde+platform.getBalance();
    }
    return  solde;
  }


  private List<Payment> getAllSupervisorTransactions(UUID supervisor) {
    List<Platform> platformList=platformRepository.findByUser(userRepository.findById(supervisor).get());
    List<Payment> paymentList=new ArrayList<>();
    platformList.forEach(p->{
      paymentList.addAll(paymentRepo.findByPlatform(p));
    });
    return paymentList;
  }
  private List<PaymentRequest> getAllSupervisorDeposits(UUID supervisor) {
    List<Platform> platformList = platformRepository.findByUser(userRepository.findById(supervisor).get());
    List<PaymentRequest> depositList = new ArrayList<>();
    platformList.forEach(p -> {
      depositList.addAll(paymentRequestRepository.findByPlatform(p));
    });
    return depositList;
  }


  private List<MeansPayment> getTransactionMeansPayment(List<Payment> paymentList){
    List<MeansPayment> meansPayments=new ArrayList<>();
    paymentList.forEach(p->{
      MeansPayment meansPayment=meansPaymentRepository.getMeansPaymentByCode(p.getServiceCode());
      meansPayments.add(meansPayment);
    });
    return meansPayments;
  }

  private Set<Category> getMeansPaymentCategorie(List<MeansPayment> meansPayments){
    Set<Category> categories = new HashSet<>();
    meansPayments.forEach(m -> {
      Category category = categoryRepository.findById(m.getCategory().getId()).get();
      categories.add(category);
    });
    return categories;
  }


  /**
   * cette methode calcule les transactions par catégorie de méthode de paiement pour un superviseur donné
   * @param supervisor
   * @return
   */
  @Override
  public Map<String,Long> getTransactionsByPaymentMethodCategory(UUID supervisor){
    List<Payment> paymentList=getAllSupervisorTransactions(supervisor);
    List<MeansPayment> meansPayments=getTransactionMeansPayment(paymentList);
    Set<Category> categories=getMeansPaymentCategorie(meansPayments);
    return categories.stream()
      .collect(Collectors.groupingBy(Category::getWording, Collectors.counting()));
  }


  /**
   * Ce code est une méthode qui calcule et renvoie le volume total des transactions pour chaque catégorie de méthode de paiement pour un superviseur donné
   * @param supervisor
   * @return
   */
  @Override
  public Map<String, Long> getVolumeOfTransactionsByPaymentMethodCategory(UUID supervisor) {
    List<Payment> paymentList = getAllSupervisorTransactions(supervisor);
    List<MeansPayment> meansPayments = getTransactionMeansPayment(paymentList);
    Set<Category> categories = getMeansPaymentCategorie(meansPayments);
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

  /**
   * cette methode recupere le nommbre de transaction mensuel par statut
   * @param supervisor
   * @return
   */
  @Override
  public Map<String, Map<String, Long>> getMonthlyTransactionsByStatus(UUID supervisor) {
    // Récupérer toutes les transactions
    List<Payment> paymentList = getAllSupervisorTransactions(supervisor);

    // Obtenir tous les statuts uniques
    //Set<String> allStatuses = paymentList.stream()
     // .map(Payment::getStatus)
     // .collect(Collectors.toSet());
    Set<String> allStatuses=new HashSet<>();
    allStatuses.add(SUCCESS);
    allStatuses.add(FAILURE);
    allStatuses.add(INITIATE);
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



  /**
   * cette methode retourne le volume de transaction mensuel par statut
   * @param supervisor
   * @return
   */

  @Override
  public Map<String, Map<String, Double>> getMonthlyTransactionVolumeByStatus(UUID supervisor) {
    // Récupérer toutes les transactions
    List<Payment> paymentList = getAllSupervisorTransactions(supervisor);

    // Obtenir tous les statuts uniques
    Set<String> allStatuses=new HashSet<>();
    allStatuses.add(SUCCESS);
    allStatuses.add(FAILURE);
    allStatuses.add(INITIATE);

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


  /**
   * cette methode calcule et renvoie le solde mensuel pour une liste de plateformes
   * @param supervisor
   * @return
   */

  @Override
  public Map<String, Double> getMonthlyBalance(UUID supervisor) {
    List<Platform> platforms = platformRepository.findByUser(userRepository.findById(supervisor).get());

    // Initialiser la Map avec tous les mois de l'année à 0
    Map<String, Double> monthlyBalance = new LinkedHashMap<>();
    for (Month month : Month.values()) {
      monthlyBalance.put(month.getDisplayName(TextStyle.FULL, Locale.FRENCH), 0.0);
    }

    for (Platform platform : platforms) {
      List<BalanceEvolution> balanceEvolutions = getBalanceEvolution(platform);
      for (BalanceEvolution balanceEvolution : balanceEvolutions) {
        YearMonth yearMonth = YearMonth.from(balanceEvolution.getOperationDate());
        String month = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
        double balance = balanceEvolution.getBalance();
        monthlyBalance.put(month, monthlyBalance.get(month) + balance);
      }
    }
    return monthlyBalance;
  }

  /**
   * Cette methode recupere le solde et le verssemment mensuel liés a un superviseur
   * @param supervisor
   * @return
   */
  @Override
  public Map<String, Map<String, Number>> getBalanceAndPaymentPerMonth(UUID supervisor) {
    List<Platform> platforms = platformRepository.findByUser(userRepository.findById(supervisor).get());

    // Initialisez la Map avec tous les mois de l'année
    Map<String, Map<String, Number>> result = new LinkedHashMap<>();
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



  private List<BalanceEvolution> getBalanceEvolution(Platform platform){
    return balanceEvolutionService.getPlatformeBalanceEvolutionSolde(platform);
  }


  @Override
	public ResponseMessage savePayment(ValidPaymentInformations validPaymentInformations, String paymentStatus) {
		Payment payment = new Payment();
		payment.setAccountNumber(validPaymentInformations.getPayoutInformation().getAccountNumber());
		payment.setCommissionAmount(UtilsClass.commission(validPaymentInformations.getPayoutInformation().getAmountPayment(), validPaymentInformations.getRateApplied()));
		payment.setCurrency(validPaymentInformations.getPayoutInformation().getCurrency());
		payment.setPaymentAmount(validPaymentInformations.getPayoutInformation().getAmountPayment());
		payment.setPaymentCode(validPaymentInformations.getPayoutInformation().getRequestId());
		payment.setPaymentDate(LocalDateTime.now());
		payment.setPaymentOrigin(validPaymentInformations.getPayoutInformation().getCountryCode());
		payment.setPaymentType(validPaymentInformations.getPaymentType());
		payment.setServiceCode(validPaymentInformations.getPayoutInformation().getServiceCode());
		payment.setPlatform(validPaymentInformations.getPlatform());
		payment.setRateApplied(validPaymentInformations.getRateApplied());
		payment.setRealAmount(validPaymentInformations.getPayoutInformation().getAmountPayment() - payment.getCommissionAmount());
		payment.setStatus(paymentStatus);

		Payment paymentSaved = paymentRepo.save(payment);

		if(paymentStatus.equals(SUCCESS)) {
			//MAJ du solde de la plateforme
	  		Platform platformSaved = platformService.balanceUpdate(validPaymentInformations);
	  		//Historisation du solde
	  		//accountService.accountHistory(platformSaved);
		}

		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setAccountNumber(paymentSaved.getAccountNumber());
		//paymentResponse.setCommissionAmount(paymentSaved.getCommissionAmount());
		paymentResponse.setCurrency(paymentSaved.getCurrency());
		//paymentResponse.setPaymentAmount(paymentSaved.getPaymentAmount());
		paymentResponse.setPaymentCode(paymentSaved.getPaymentCode());
		paymentResponse.setPaymentDate(paymentSaved.getPaymentDate());
		paymentResponse.setPaymentOrigin(paymentSaved.getPaymentOrigin());
		paymentResponse.setPaymentStatus(paymentStatus);
		paymentResponse.setPaymentType(paymentSaved.getPaymentType());
		paymentResponse.setServiceCode(paymentSaved.getServiceCode());
		paymentResponse.setPlatformKey(validPaymentInformations.getPayoutInformation().getPlatformKey());
		paymentResponse.setRateApplied(paymentSaved.getRateApplied());
		//paymentResponse.setRealAmount(paymentSaved.getRealAmount());

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setCode(SUCCESS_CODE);
		responseMessage.setData(paymentResponse);
		responseMessage.setMessage(paymentStatus);

		return responseMessage;
	}

	@Override
	public ResponseMessage updateBFMoovPayment(Payment payment) {
		// TODO Auto-generated method stub
		String response = moovServices.paymentStatus(payment.getPaymentCode());
		if(response.equals("success")) {
			//MAJ du solde de la plateforme
			Platform platform = payment.getPlatform();
			//platform.setBalance(platform.getBalance()+payment.getRealAmount());
	//		platform.setBalanceModificationDate(payment.getPaymentDate());
	//		Platform platformSaved = platformService.updatePlatform(platform);
	  		//Historisation du solde
	//		accountService.accountHistory(platformSaved);

			payment.setStatus(SUCCESS);
	//		payment.setPlatform(platformSaved);
			Payment paymentUpdated = paymentRepo.save(payment);
			return getPayment(paymentUpdated);
		}
		if(response.equals("failed")) {
			payment.setStatus(FAILURE);
			Payment paymentUpdated = paymentRepo.save(payment);
			return getPayment(paymentUpdated);
		}
		return getPayment(payment);
	}

	@Override
	public Payment getPaymentByPaymentCode(String paymentCode) {
		// TODO Auto-generated method stub
		return paymentRepo.findByPaymentCode(paymentCode);
	}

	@Override
	public ResponseMessage getPayment(Payment payment) {
		// TODO Auto-generated method stub
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setAccountNumber(payment.getAccountNumber());
		paymentResponse.setCommissionAmount(payment.getCommissionAmount());
		paymentResponse.setCurrency(payment.getCurrency());
		paymentResponse.setPaymentAmount(payment.getPaymentAmount());
		paymentResponse.setPaymentCode(payment.getPaymentCode());
		paymentResponse.setPaymentDate(payment.getPaymentDate());
		paymentResponse.setPaymentOrigin(payment.getPaymentOrigin());
		paymentResponse.setPaymentStatus(payment.getStatus());
		paymentResponse.setPaymentType(payment.getPaymentType());
		paymentResponse.setServiceCode(payment.getServiceCode());
		paymentResponse.setPlatformKey(payment.getPlatform().getPlatformCode());
		paymentResponse.setRateApplied(payment.getRateApplied());
		paymentResponse.setRealAmount(payment.getRealAmount());

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setCode(SUCCESS_CODE);
		responseMessage.setData(paymentResponse);
		responseMessage.setMessage(payment.getStatus());

		return responseMessage;
	}

	@Override
	public ResponseMessage updateCIWavePayment(Payment payment) {
		// TODO Auto-generated method stub
		String response = waveServices.paymentStatus(payment.getPaymentCode());
		if(response.equals("succeeded")) {
			//MAJ du solde de la plateforme
			Platform platform = payment.getPlatform();
			//platform.setBalance(platform.getBalance()+payment.getRealAmount());
	//		platform.setBalanceModificationDate(payment.getPaymentDate());
	//		Platform platformSaved = platformService.updatePlatform(platform);
	  		//Historisation du solde
	//		accountService.accountHistory(platformSaved);

			payment.setStatus(SUCCESS);
	//		payment.setPlatform(platformSaved);
			Payment paymentUpdated = paymentRepo.save(payment);
			return getPayment(paymentUpdated);
		}
		if(response.equals("cancelled")) {
			payment.setStatus(FAILURE);
			Payment paymentUpdated = paymentRepo.save(payment);
			return getPayment(paymentUpdated);
		}
		return getPayment(payment);
	}

	@Override
	public ResponseMessage updateCIMTNPayment(Payment payment) {
		String response = mtnServices.paymentStatus(payment.getPaymentCode());
		if(response.equals("successful")) {
			//MAJ du solde de la plateforme
			Platform platform = payment.getPlatform();
			//platform.setBalance(platform.getBalance()+payment.getRealAmount());
	//		platform.setBalanceModificationDate(payment.getPaymentDate());
	//		Platform platformSaved = platformService.updatePlatform(platform);
	  		//Historisation du solde
	//		accountService.accountHistory(platformSaved);

			payment.setStatus(SUCCESS);
	//		payment.setPlatform(platformSaved);
			Payment paymentUpdated = paymentRepo.save(payment);
			return getPayment(paymentUpdated);
		}
		if(response.equals("failed")) {
			payment.setStatus(FAILURE);
			Payment paymentUpdated = paymentRepo.save(payment);
			return getPayment(paymentUpdated);
		}
		return getPayment(payment);
	}



}
