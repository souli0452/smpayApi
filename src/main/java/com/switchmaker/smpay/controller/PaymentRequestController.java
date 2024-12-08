package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.dto.PaymentRequestDtoTransfert;
import com.switchmaker.smpay.dto.RequestPaymentDto;
import com.switchmaker.smpay.entities.PaymentRequest;
import com.switchmaker.smpay.entities.Platform;
import com.switchmaker.smpay.entities.User;
import com.switchmaker.smpay.entities.ValidateCode;
import com.switchmaker.smpay.repository.*;
import com.switchmaker.smpay.services.PaymentRequestService;
import com.switchmaker.smpay.services.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;

@CrossOrigin("*")
@RestController
@RequestMapping(ROOT_API)
public class PaymentRequestController {
  @Autowired
  PlatformRepository platformRepo;
  @Autowired
  UserRepository userRepository;
  @Autowired
  CostomerRepository costomerRepository;
  @Autowired
  CodeRepository codeRepository;
  @Autowired
  PaymentRequestRepository paymetRequestRepo;
  @Autowired
  PaymentRepository transactionRepo;
  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  NotificationRepository notificationRepo;

  @Autowired
  private PaymentRequestService paymentRequestService;

  @Autowired
  private PlatformService platformService;

	/*--------------------------------------/
	/*          Envoyer une demande        /
	/*-----------------------------------*/

  @RolesAllowed({USER,SUPERVISOR, APPLICATION,MANAGER})
  @PostMapping("/payment/request")
  public ResponseEntity<?> sendPaymentRequest(@RequestBody RequestPaymentDto requestPaymentDto) {
    ValidateCode validateCode = codeRepository.findByToken(requestPaymentDto.getCode());
    if (validateCode != null) {
      if (validateCode.getUser() != null) {
        Platform platform = platformRepo.findById(requestPaymentDto.getPlatformId()).get();
        if (platform.getBalance() >= requestPaymentDto.getAmount()){

          Calendar cal = Calendar.getInstance();
          if ((validateCode.getExpiryDate().getTime() - cal.getTime().getTime()) >= 0) {
            codeRepository.delete(validateCode);

/*
            ResponseEntity<Boolean> appResult = getStringResponseEntity(platform);
            if (appResult.getBody()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("\"Une demande est déjà en cours pour cette application.\"");;
*/

            // Vérification si une demande est déjà en cours pour la même application
//            PaymentRequest existingRequestProcessing = paymetRequestRepo.findByPlatformAndStatus(platform, PROCESSING);
//            if (existingRequestProcessing != null) {
//              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("\"Une demande est déjà en cours pour cette application.\"");
//            }

            PaymentRequest paymentRequest = PaymentRequest.builder()
              .paymentAccount(platform.getPaymentAccount())
              .paymentType(platform.getPaymentAccountType())
                    .homeStructure(platform.getHomeStructure())

              .sendingDate(requestPaymentDto.getSendingDate())
              .user(userRepository.findById(requestPaymentDto.getUserId()).get())
              .reason("Demande de versement")
              .amount(requestPaymentDto.getAmount())
              .platform(platformRepo.findById(requestPaymentDto.getPlatformId()).get())
              .processingDate(requestPaymentDto.getProcessingDate())
              .transactionNumber(requestPaymentDto.getTransactionNumber())
              .status(requestPaymentDto.getStatus())
              .build();

        /*    PaymentRequest oldDemande = paymetRequestRepo.findByPlatformAndStatus(paymentRequest.getPlatform(),
              INITIATE);
            if (oldDemande != null) {
              paymetRequestRepo.delete(oldDemande);
            }*/


            paymentRequest.setSendingDate(LocalDateTime.now());
            paymentRequest.setStatus(INITIATE);
            PaymentRequest demandeSaved = paymetRequestRepo.save(paymentRequest);
            platform.setBalance(platform.getBalance() - demandeSaved.getAmount());
            platformRepo.save(platform);
            this.simpMessagingTemplate.convertAndSend("/socket-publisher/demande", "ok");
            return ResponseEntity.status(HttpStatus.OK).body(demandeSaved);
          }

        }else {
          return ResponseEntity.ok("\"Solde insuffisant\"");

        }

      }
    }
    return ResponseEntity.ok("\"code invalide\"");
  }

                  /*--------------------------------------------------------------------------/
                 /*          Vérification, si l'application à une demande non términé        /
                 /*------------------------------------------------------------------------*/
  @RolesAllowed({USER,SUPERVISOR, APPLICATION,MANAGER})
  @PostMapping("/payment/request/verifie/statut")
  public ResponseEntity<Boolean> getStringResponseEntity(@RequestBody Platform platform) {
    // Vérification si une demande est déjà initié pour la même application
    PaymentRequest existingRequestInitiate = paymetRequestRepo.findByPlatform(platform).stream().filter(r -> r.getStatus().compareToIgnoreCase(INITIATE) == 0 || r.getStatus().compareToIgnoreCase(PROCESSING) == 0).findFirst().get();
    if (existingRequestInitiate != null) {
      return ResponseEntity.status(HttpStatus.OK).body(true);
    } else {
      return  ResponseEntity.status(HttpStatus.OK).body(false);
    }
  }

                   /*----------------------------------------------------------/
                  /*          Valider, changer de statut d'une demande        /
                  /*--------------------------------------------------------*/

  @RolesAllowed({ADMIN, USER, SUPERVISOR, APPLICATION,MANAGER})
  @PutMapping("/payment/request/statut/{requestId}")
  public ResponseEntity<?> markRequestAsProcessingOrCompleted(@PathVariable UUID requestId,@RequestBody PaymentRequestDtoTransfert paymentRequestDtoTransfert) {
    try {
      PaymentRequest updatedRequest = paymentRequestService.validateChangeStatutPaymentRequest(requestId, paymentRequestDtoTransfert);
      return ResponseEntity.status(HttpStatus.OK).body(updatedRequest);
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

                  /*----------------------------------------------------------/
                  /*          Annulation d'une demande                       /
                  /*-------------------------------------------------------*/
  @RolesAllowed({USER,SUPERVISOR,APPLICATION,MANAGER})
  @PutMapping("/cancelPaymentRequest/{requestId}")
  public ResponseEntity<?> cancelPaymentRequest(@PathVariable UUID requestId) {
    return paymentRequestService.cancelPaymentRequest(requestId);
  }

                  /*----------------------------------------------------------/
                  /*       supprimée une demande annulée                     /
                  /*-------------------------------------------------------*/

  @RolesAllowed({USER,SUPERVISOR, ADMIN, APPLICATION,MANAGER})
  @DeleteMapping("/delete/{requestId}")
  public ResponseEntity<?> deleteCanceledPaymentRequest(@PathVariable UUID requestId) {
    return paymentRequestService.deleteInitiedPaymentRequest(requestId);
  }

                /*----------------------------------------------------------/
                /*         lister les demandes pat ID                      /
                /*-------------------------------------------------------*/

  @RolesAllowed({ADMIN,MANAGER})
  @GetMapping("/getPaymentRequestById/{requestId}")
  public ResponseEntity<?> getPaymentRequestById(@PathVariable UUID requestId) {
    return paymentRequestService.getPaymentRequestById(requestId);
  }


              /*-----------------------------------------------------------------------------/
             /*         lister les demandes en cous et en attente                           /
            /*----------------------------------------------------------------------------*/

  @RolesAllowed({ADMIN,MANAGER})
  @GetMapping("/paymentRequest/initie/processing")
  public List<PaymentRequest> getAllPaymentRequestsInitieProcessing(){
    return paymentRequestService.getAllPaymentRequestsInitieProcessing();
  }

                /*----------------------------------------------------------/
                /*         lister toutes les demandes                      /
                /*-------------------------------------------------------*/

  @RolesAllowed({ADMIN,MANAGER})
  @GetMapping("/getAllPaymentRequests")
  public ResponseEntity<?> getAllPaymentRequests() {
    List<PaymentRequest> paymentRequests = paymentRequestService.getAllPaymentRequests().stream()
            .sorted((p1, p2) -> p2.getSendingDate().compareTo(p1.getSendingDate())) // Tri par date de création décroissante
            .collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.OK).body(paymentRequests);
  }

                /*----------------------------------------------------------/
                /*         lister les demandes annulées                    /
                /*-------------------------------------------------------*/

  @RolesAllowed({ADMIN,MANAGER})
  @GetMapping("/getAllCanceledPaymentRequests")
  public ResponseEntity<?> getAllCanceledPaymentRequests() {
    List<PaymentRequest> canceledPaymentRequests = paymentRequestService.getAllCanceledPaymentRequests().stream()
            .sorted((p1, p2) -> p2.getSendingDate().compareTo(p1.getSendingDate())) // Tri par date de création décroissante
            .collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.OK).body(canceledPaymentRequests);
  }

                /*------------------------------------------------------/
               /*         lister les demandes traitée                  /
              /*-----------------------------------------------------*/
  @RolesAllowed({ADMIN,MANAGER})
  @GetMapping("/getAllValidatedPaymentRequests")
  public ResponseEntity<?> getAllValidatedPaymentRequests() {
    List<PaymentRequest> validatedPaymentRequests = paymentRequestService.getAllValidatedPaymentRequests().stream()
            .sorted((p1, p2) -> p2.getSendingDate().compareTo(p1.getSendingDate())) // Tri par date de création décroissante
            .collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.OK).body(validatedPaymentRequests);
  }


              /*------------------------------------------------------------------/
             /*         lister les demandes associé à un client                  /
            /*-----------------------------------------------------------------*/

  @RolesAllowed({ADMIN, USER,SUPERVISOR, APPLICATION,MANAGER})
  @GetMapping("/user/paymentrequest/{userId}")
  public ResponseEntity<?> getPaymentRequestsByUser(@PathVariable UUID userId) {
    List<PaymentRequest> paymentRequests = paymentRequestService.getPaymentRequestsByUser(userId).stream()
            .sorted((p1, p2) -> p2.getSendingDate().compareTo(p1.getSendingDate())) // Tri par date de création décroissante
            .collect(Collectors.toList());

    if (!paymentRequests.isEmpty()) {
      return ResponseEntity.status(HttpStatus.OK).body(paymentRequests);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune demande de paiement trouvée pour cet utilisateur.");
    }
  }

              /*-----------------------------------------------------------------------------/
             /        lister les demandes en cous et en attente associé à un client       /
            /----------------------------------------------------------------------------*/


  @RolesAllowed({ADMIN, USER, SUPERVISOR, APPLICATION,MANAGER})
  @GetMapping("/user/paymentrequestinitieprocessing/{userId}")
  public ResponseEntity<?> getPaymentRequestsByUserInitieProcessing(@PathVariable UUID userId) {

    User user=userRepository.findById(userId).get();
    List<PaymentRequest> paymentRequestsf=new ArrayList<>();
    //if (user.getAccountType().compareTo(UserAccountType.supervisor)==0){
      List<Platform> platformList=platformRepo.findByUser(user);
     platformList.forEach(p->{
       paymentRequestsf.addAll(paymetRequestRepo.findByPlatform(p)
               .stream()
               .filter(pay -> pay.getStatus().equalsIgnoreCase(INITIATE) || pay.getStatus().equalsIgnoreCase(PROCESSING))
               .sorted((p1, p2) -> p2.getSendingDate().compareTo(p1.getSendingDate())) // Tri par date de création décroissante
               .collect(Collectors.toList()));
     });
    //}

    if (!paymentRequestsf.isEmpty()) {
      return ResponseEntity.status(HttpStatus.OK).body(paymentRequestsf);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune demande de paiement trouvée pour cet utilisateur.");
    }
  }

              /*-----------------------------------------------------------------------------/
             /*         lister les demandes en cous et en attente associé à un client       /
            /*----------------------------------------------------------------------------*/


  @RolesAllowed({ADMIN, USER, SUPERVISOR, APPLICATION,MANAGER})
  @GetMapping("/user/applicatif/{userId}")
  public ResponseEntity<?> getClientPlatformAppPaymentRequest(@PathVariable UUID userId) {
    List <PaymentRequest> paymentRequests = platformService.getClientPlatformAppPaymentRequest(userId);

    if (paymentRequests != null) {
      return ResponseEntity.status(HttpStatus.OK).body(paymentRequests);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune demande  trouvée pour cette plateforme.");
    }
  }

              /*-----------------------------------------------------------------------------/
             /*         lister les demandes success associé à une application               /
            /*----------------------------------------------------------------------------*/

  @RolesAllowed({ADMIN, USER, SUPERVISOR, APPLICATION,MANAGER})
  @GetMapping("/user/applicatif/success/{userId}")
  public ResponseEntity<?> getClientPlatformAppPaymentRequestSuccess(@PathVariable UUID userId) {
   List <PaymentRequest> paymentRequests = platformService.getClientPlatformAppPaymentRequestSuccess(userId);

    if (paymentRequests != null) {
      return ResponseEntity.status(HttpStatus.OK).body(paymentRequests);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune demande  trouvée pour cette plateforme.");
    }
  }

              /*------------------------------------------------------------------/
             /*         lister les demandes par statut                           /
            /*-----------------------------------------------------------------*/

  @RolesAllowed({ADMIN, USER, SUPERVISOR, APPLICATION,MANAGER})
  @GetMapping("payment/requests/{status}")
    public ResponseEntity<?> getPaymentRequestsByStatus(@PathVariable("status") String status) {
      List<PaymentRequest> requests = paymentRequestService.getPaymentRequestsByStatus(status).stream()
              .sorted((p1, p2) -> p2.getSendingDate().compareTo(p1.getSendingDate())) // Tri par date de création décroissante
              .collect(Collectors.toList());
      if (!requests.isEmpty()) {
        return ResponseEntity.status(HttpStatus.OK).body(requests);
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune demande trouvée avec le statut " + status);
      }
    }




/*
	  @RolesAllowed(ADMIN)

	  @PutMapping("/validate/demande") public ResponseEntity<?>validateRequest(@RequestBody Request request){

	  ValidateCode validateCode = codeRepository.findByToken(request.getCode());
	 if(validateCode!=null) { if(validateCode.getAdmin() != null) { Calendar cal =
	  Calendar.getInstance(); if((validateCode.getExpiryDate().getTime() -
	  cal.getTime().getTime()) >= 0) { codeRepository.delete(validateCode);
	  PaymentRequest paymentRequest = request.getDemande(); LocalDateTime date =
	 LocalDateTime.now(); Platform platform = paymentRequest.getPlatform();
	 platform.setSolde(platform.getSolde() - paymentRequest.getAmount());
	 platform.setDateSoldeModif(date); Platform platformSaved =
	  platformRepo.save(platform); //Historisation du solde
		 Account account = new Account(); account.setDateSoldeModif(date);
	  account.setSolde(platformSaved.getSolde());
	  account.setPlatform(platformSaved); compteRepo.save(account);

	  Notification notification = new Notification();
	  notification.setClient(platformSaved.getClient());
	  notification.setDate(date); notification.setMessage("Votre versement de " +
	 paymentRequest.getAmount() + " FCFA a été éffectué");
	  notificationRepo.save(notification); paymentRequest.setDateTraitement(date);
	  paymentRequest.setStatus(SUCCESS); PaymentRequest demandeSaved =
	  demandeRepo.save(paymentRequest);
	  this.simpMessagingTemplate.convertAndSend("/socket-publisher/demande/treat",
	  demandeSaved.getPlatform().getClient().getId().toString()); return
	  ResponseEntity.status(HttpStatus.OK).body(demandeSaved); } } } return
	  ResponseEntity.ok("\"code invalide\""); }


      /*--------------------------------------/
	 /*          Rejeter une demande        /
	/*-----------------------------------*//*



	 @RolesAllowed(ADMIN)
	  @PutMapping("/reject/demande") public ResponseEntity<?> rejectDemande(
	 @RequestBody PaymentRequest demande){ try { LocalDateTime date =
	 LocalDateTime.now(); demande.setDateTraitement(date);
	 demande.setStatus(FAILURE); PaymentRequest demandeSaved =
	  demandeRepo.save(demande);

	  Notification notification = new Notification();
	  notification.setClient(demandeSaved.getPlatform().getClient());
	  notification.setDate(date);
	  notification.setMessage("Votre demande de versement de " +
	  demandeSaved.getAmount() + " FCFA a été rejetée pour raison suivante : " +
	  demandeSaved.getMotif()); notificationRepo.save(notification);

	  this.simpMessagingTemplate.convertAndSend("/socket-publisher/demande/reject",
	  demandeSaved.getPlatform().getClient().getId().toString()); return
	  ResponseEntity.status(HttpStatus.OK).body(demandeSaved); }
	 catch (Exception e) { return ResponseEntity .badRequest() .body("Une erreur est survenue !");} }


	  */
/*--------------------------------------/
	 /*  liste des demandes d'un client     /
	/*-----------------------------------*//*


	  @RolesAllowed({ADMIN,USER})

	  @GetMapping("/demandes/client/{id}/limit/{limit}") public ResponseEntity<?> getClientDemande(@PathVariable("id") UUID id, @PathVariable("limit") int limit){
		  try {
			  List<PaymentRequest> paymentRequest = demandeRepo.findClientDemande(id, limit); List<DemandeXOF> demandesXOF =
	  new ArrayList<>(); Locale bf = new Locale("fr", "BF"); NumberFormat cfaFormat = NumberFormat.getCurrencyInstance(bf);

	 if(!paymentRequest.isEmpty()) { for(PaymentRequest demande : paymentRequest) {
		 DemandeXOF demandeXOF = new DemandeXOF();
	  demandeXOF.setId(demande.getId()); String montant = cfaFormat.format(demande.getAmount());
	  demandeXOF.setAmount(montant.substring(0, montant.length() - 4));
	  demandeXOF.setCompte(demande.getCompte());
	  demandeXOF.setDateEnvoie(demande.getDateEnvoie());
	  demandeXOF.setDateTraitement(demande.getDateTraitement());
	  demandeXOF.setMotif(demande.getMotif());
	  demandeXOF.setPaymentType(demande.getPaymentType());
	  demandeXOF.setPlatform(demande.getPlatform());
	  demandeXOF.setStatus(demande.getStatus()); demandesXOF.add(demandeXOF); } }
	  return new ResponseEntity<List <DemandeXOF> >(demandesXOF, HttpStatus.OK); }
	  catch (Exception e) { return ResponseEntity .badRequest().body("Une erreur est survenue !"); }}


	  */
/*-----------------------------------------------/
	 /*  liste des demandes d'un client par statut    /
	/*----------------------------------------------*//*


	  @RolesAllowed({ADMIN,USER})

	 @GetMapping("/demandes/client/{id}/status/{status}/limit/{limit}")
	  public ResponseEntity<?> getClientDemandeByStatus(@PathVariable("id") UUID id, @PathVariable("status") String status, @PathVariable("limit") int limit){ try { List<PaymentRequest> paymentRequest
	  = demandeRepo.findClientDemandeStatus(id, status, limit); List<DemandeXOF>
	 demandesXOF = new ArrayList<>(); Locale bf = new Locale("fr", "BF");
	  NumberFormat cfaFormat = NumberFormat.getCurrencyInstance(bf);

	  if(!paymentRequest.isEmpty()) { for(PaymentRequest demande : paymentRequest)
	  { DemandeXOF demandeXOF = new DemandeXOF();
	  demandeXOF.setId(demande.getId()); String montant =
	  cfaFormat.format(demande.getAmount());
	  demandeXOF.setAmount(montant.substring(0, montant.length() - 4));
	  demandeXOF.setCompte(demande.getCompte());
	  demandeXOF.setDateEnvoie(demande.getDateEnvoie());
	  demandeXOF.setDateTraitement(demande.getDateTraitement());
	  demandeXOF.setMotif(demande.getMotif());
	  demandeXOF.setPaymentType(demande.getPaymentType());
	  demandeXOF.setPlatform(demande.getPlatform());
	  demandeXOF.setStatus(demande.getStatus());
	  demandeXOF.setNumTransaction(demande.getNumTransaction().toUpperCase());
	  demandesXOF.add(demandeXOF); } } return new ResponseEntity<List <DemandeXOF>>(demandesXOF, HttpStatus.OK);
	  }
	  catch (Exception e) { return ResponseEntity .badRequest().body("Une erreur est survenue !"); }}


	 */
/*------------------------------------------------/
	 /*         liste des demandes par statut         /
	/*----------------------------------------------*//*


	  @RolesAllowed(ADMIN)

	  @GetMapping("/demandes/status/{status}/limit/{limit}/offset/{offset}")
	  public ResponseEntity<?> getDemandeByStatus(@PathVariable("status") String status, @PathVariable("limit") int limit, @PathVariable("offset") int offset){ try { if(limit==0 && offset==0) {
	 List<PaymentRequest> paymentRequest = demandeRepo.findByStatus(status,
	  Sort.by(Direction.DESC, "dateEnvoie")); return new ResponseEntity<List<PaymentRequest> >(paymentRequest,HttpStatus.OK); } DemandePageable demandes
	  = new DemandePageable(); List<PaymentRequest> demandesList =
	  demandeRepo.findByStatus(status, limit, offset); List<DemandeXOF> demandesXOF
	  = new ArrayList<>(); Locale bf = new Locale("fr", "BF"); NumberFormat
	  cfaFormat = NumberFormat.getCurrencyInstance(bf);

	  if(!demandesList.isEmpty()) { for(PaymentRequest demande : demandesList) {
	  DemandeXOF demandeXOF = new DemandeXOF(); demandeXOF.setId(demande.getId());
	  String montant = cfaFormat.format(demande.getAmount());
	  demandeXOF.setAmount(montant.substring(0, montant.length() - 4));
	  demandeXOF.setCompte(demande.getCompte());
	  demandeXOF.setDateEnvoie(demande.getDateEnvoie());
	  demandeXOF.setDateTraitement(demande.getDateTraitement());
	  demandeXOF.setMotif(demande.getMotif());
	  demandeXOF.setPaymentType(demande.getPaymentType());
	  demandeXOF.setPlatform(demande.getPlatform());
	  demandeXOF.setStatus(demande.getStatus());
	  if(demande.getNumTransaction()!=null) {
	  demandeXOF.setNumTransaction(demande.getNumTransaction().toUpperCase()); }
	  demandesXOF.add(demandeXOF); } } demandes.setDemandes(demandesXOF);
	  demandes.setNumberOfElements(demandes.getDemandes().size());
	  demandes.setTotalRecords(demandeRepo.findByStatus(status).size());
	  demandes.setTotalPage((int)
	  Math.ceil((float)demandes.getTotalRecords()/(float)limit)); return new
	  ResponseEntity<DemandePageable> (demandes, HttpStatus.OK); } catch (Exception e) {
		  return ResponseEntity .badRequest() .body("Une erreur est survenue !");
	  }
	  }

	   */
/*--------------------------------------------------------------------/
	 /*         liste des demandes par statut statut et par période         /
	/*--------------------------------------------------------------------*//*



	 @RolesAllowed(ADMIN)

	  @GetMapping("/demandes/status/{status}/debut/{debut}/fin/{fin}")
	 public ResponseEntity<?> getPeriodDemandeByStatus(@PathVariable("status") String status, @PathVariable("debut") String dateDebut, @PathVariable("fin") String dateFin){
		 try { DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH); LocalDate debut =
	  LocalDate.parse(dateDebut, formatter); LocalDate fin = LocalDate.parse(dateFin, formatter); DemandePageable demandes = new DemandePageable();

	  List<PaymentRequest> demandesList = demandeRepo.periodDemandeByStatus(status,
	  debut, fin); List<DemandeXOF> demandesXOF = new ArrayList<>(); Locale bf =
	  new Locale("fr", "BF"); NumberFormat cfaFormat =
	  NumberFormat.getCurrencyInstance(bf);
	  if(!demandesList.isEmpty()) { for(PaymentRequest demande : demandesList) {
	  DemandeXOF demandeXOF = new DemandeXOF(); demandeXOF.setId(demande.getId());
	  String montant = cfaFormat.format(demande.getAmount());
	  demandeXOF.setAmount(montant.substring(0, montant.length() - 4));
	  demandeXOF.setCompte(demande.getCompte());
	  demandeXOF.setDateEnvoie(demande.getDateEnvoie());
	  demandeXOF.setDateTraitement(demande.getDateTraitement());
	  demandeXOF.setMotif(demande.getMotif());
	  demandeXOF.setPaymentType(demande.getPaymentType());
	  demandeXOF.setPlatform(demande.getPlatform());
	  demandeXOF.setStatus(demande.getStatus());
	  demandeXOF.setNumTransaction(demande.getNumTransaction().toUpperCase());
	  demandesXOF.add(demandeXOF); } } demandes.setDemandes(demandesXOF);
	  demandes.setNumberOfElements(demandes.getDemandes().size());
	  demandes.setTotalRecords(demandes.getDemandes().size());
	  demandes.setTotalPage(1); //demandes.setTotalPage((int)
	  Math.ceil((float)demandes.getTotalRecords()/(float)limit)); return new
	  ResponseEntity<DemandePageable> (demandes, HttpStatus.OK);}
		 catch (Exception e) { return ResponseEntity .badRequest().body("Une erreur est survenue !"); }}

	   */
	   /*--------------------------------------------------------------------/
	 /*         Nouvelles et anciennes demandes                             /
	/*--------------------------------------------------------------------*//*


	 @RolesAllowed(ADMIN)
	 @GetMapping("/demandes/new/and/old/client/{id}/limit/{limit}")
	 public ResponseEntity<?> getDemandeNewAndOld(@PathVariable("id") UUID id, @PathVariable("limit") int limit){
		 try { Versement versement = new Versement(); versement.setNewDemandes(demandeRepo.findClientDemandeStatus(id, INITIATE, limit));
	  versement.setDemandes(demandeRepo.findClientDemandeStatusNotAttempt(id));
	  return new ResponseEntity<Versement>(versement,HttpStatus.OK); }
		 catch
	  (Exception e) { return ResponseEntity .badRequest().body("Une erreur est survenue !"); }

	  }
*/

}
