package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.dto.TransactionPageable;
import com.switchmaker.smpay.entities.Payment;
import com.switchmaker.smpay.repository.PaymentRepository;
import com.switchmaker.smpay.repository.PaymentRequestRepository;
import com.switchmaker.smpay.repository.PlatformRepository;
import com.switchmaker.smpay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static com.switchmaker.smpay.constant.urls.PaymentUrls.*;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;

@CrossOrigin("*")
@RestController
public class PaymentController {

	@Autowired
	PlatformRepository platformRepository;
	@Autowired
	PaymentRepository paymentRepository;
	@Autowired
	PaymentRequestRepository demandeVersement;
	@Autowired
	UserRepository userRepository;


	/*-------------------------------------------------/
	/*        Lister les transactions par statut      /
	/*----------------------------------------------*/

	@RolesAllowed({ADMIN,MANAGER})
	@GetMapping(GET_TRANSACTIONS)
	public ResponseEntity<?> getTransactions(
			@PathVariable("status") String statut,
			@PathVariable("limit") int limit,
			@PathVariable("offset") int offset){
		   List<Payment> transactionsList = paymentRepository.findByStatus(statut, limit, offset);
		   /*List<TransactionXOF> transList = new ArrayList<TransactionXOF>();
		   Locale bf = new Locale("fr", "BF");
		   NumberFormat cfaFormat = NumberFormat.getCurrencyInstance(bf);
		   if(!transactionsList.isEmpty()) {
			   for(Transaction transaction : transactionsList) {
				   TransactionXOF transXOF = new TransactionXOF();
				   String montant = cfaFormat.format(transaction.getTransactionAmount());
				   transXOF.setId(transaction.getId());
				   transXOF.setClientPhoneNumber(transaction.getClientPhoneNumber());
				   transXOF.setPlatform(transaction.getPlatform());
				   transXOF.setStatus(transaction.getStatus());
				   transXOF.setTransactionCode(transaction.getTransactionCode());
				   transXOF.setTransactionDate(transaction.getTransactionDate());
				   //transXOF.setTransactionType(transaction.getTransactionType());
				   transXOF.setTransactionAmount(montant.substring(0, montant.length() - 4));
				   transList.add(transXOF);
			   }
		   }*/
		TransactionPageable transactions = new TransactionPageable();
		transactions.setTransaction(transactionsList);
		transactions.setNumberOfElements(transactions.getTransaction().size());
		transactions.setTotalRecords(paymentRepository.findByStatus(statut).size());
		transactions.setTotalPage((int) Math.ceil((float)transactions.getTotalRecords()/(float)limit));
		return new ResponseEntity<TransactionPageable> (transactions, HttpStatus.OK);

	}



	/*-------------------------------------------------------------------------/
	/*          Rechercher une transaction par l'ID de la transaction         /
	/*----------------------------------------------------------------------*/

	@RolesAllowed({ADMIN,USER,MANAGER})
	@GetMapping(GET_TRANSACTION)
	public ResponseEntity<?> getTransaction(@PathVariable("id") UUID id) {
		if(!StringUtils.isEmpty(id)) {
		   Optional<Payment> transactionOpt = paymentRepository.findById(id);
		   if(transactionOpt!=null) {
			   return new ResponseEntity<Payment>(transactionOpt.get(),HttpStatus.OK);
		   }
		}
		return new ResponseEntity<String>("\"echec\"",HttpStatus.BAD_REQUEST);
	}


	/*-------------------------------------------------------/
	/*      Lister les transactions d'une plateforme        /
	/*----------------------------------------------------*/

	@RolesAllowed({ADMIN,USER,MANAGER})
	@GetMapping(GET_PLATFORM_TRANSACTIONS)
	public ResponseEntity<?> getPlatformTrans(
			@PathVariable("platformId") UUID platformId,
			@PathVariable("limit") int limit,
			@PathVariable("offset") int offset) {
		   List<Payment> transactionsList = paymentRepository.findPlatformTransactions(platformId,limit,offset);
		TransactionPageable transactions = new TransactionPageable();
		transactions.setTransaction(transactionsList);
		transactions.setNumberOfElements(transactions.getTransaction().size());
		transactions.setTotalRecords(paymentRepository.findPlatformTransactions(platformId).size());
		transactions.setTotalPage((int) Math.ceil((float)transactions.getTotalRecords()/(float)limit));
		return new ResponseEntity<TransactionPageable> (transactions, HttpStatus.OK);
	}


	/*----------------------------------------------------/
	/*        Lister les transactions d'un client        /
	/*-------------------------------------------------*/

	@RolesAllowed({ADMIN,USER,MANAGER})
	@GetMapping(GET_CLIENT_TRANSACTIONS)
	public ResponseEntity<?> getClientTrans(
			@PathVariable("clientId") UUID clientId,
			@PathVariable("limit") int limit,
			@PathVariable("offset") int offset) {
		   List<Payment> transactionsList = paymentRepository.findCostomerTransactions(clientId,limit,offset);
		TransactionPageable transactions = new TransactionPageable();
		transactions.setTransaction(transactionsList);
		transactions.setNumberOfElements(transactions.getTransaction().size());
		transactions.setTotalRecords(paymentRepository.findCostomerTransactions(clientId).size());
		transactions.setTotalPage((int) Math.ceil((float)transactions.getTotalRecords()/(float)limit));
		return new ResponseEntity<TransactionPageable> (transactions, HttpStatus.OK);
	}




	/*---------------------------------------------------------------/
	/*        Lister les transactions d'un client par période       /
	/*------------------------------------------------------------*/

	@RolesAllowed({ADMIN,USER,MANAGER})
	@GetMapping(GET_CLIENT_PERIOD_TRANSACTIONS)
	public ResponseEntity<?> getClientPeriodTrans(
			@PathVariable("clientId") UUID clientId,
			@PathVariable("debut") String dateDebut,
			@PathVariable("fin") String dateFin) {
		if(!StringUtils.isEmpty(clientId)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
			LocalDate debut = LocalDate.parse(dateDebut, formatter);
			LocalDate fin = LocalDate.parse(dateFin, formatter);
			List<Payment> transactionsList = paymentRepository.costomerTransactionsBetweenToDates(clientId, debut, fin);
			TransactionPageable transactions = new TransactionPageable();
			transactions.setTransaction(transactionsList);
			transactions.setNumberOfElements(transactions.getTransaction().size());
			transactions.setTotalRecords(transactions.getTransaction().size());
			transactions.setTotalPage(1);
			return new ResponseEntity<TransactionPageable> (transactions, HttpStatus.OK);
		}
		return new ResponseEntity<String>("\"echec\"",HttpStatus.BAD_REQUEST);
	}




	/*------------------------------------------------------------------/
	/*        Lister les transactions d'une platform par période       /
	/*---------------------------------------------------------------*/

	@RolesAllowed({ADMIN,USER,MANAGER})
	@GetMapping(GET_PLATFORM_PERIOD_TRANSACTIONS)
	public ResponseEntity<?> getPlatformPeriodTrans(
			@PathVariable("platformId") UUID platformId,
			@PathVariable("debut") String dateDebut,
			@PathVariable("fin") String dateFin) {
		if(!StringUtils.isEmpty(platformId)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
			LocalDate debut = LocalDate.parse(dateDebut, formatter);
			LocalDate fin = LocalDate.parse(dateFin, formatter);
			List<Payment> transactionsList = paymentRepository.platformTransactionsBetweenToDates(platformId, debut, fin);
			TransactionPageable transactions = new TransactionPageable();
			transactions.setTransaction(transactionsList);
			transactions.setNumberOfElements(transactions.getTransaction().size());
			transactions.setTotalRecords(transactions.getTransaction().size());
			transactions.setTotalPage(1);
			return new ResponseEntity<TransactionPageable> (transactions, HttpStatus.OK);
		}
		return new ResponseEntity<String>("\"echec\"",HttpStatus.BAD_REQUEST);
	}





	/*-----------------------------------------------------------------/
	/*        Lister les transactions par statut et par période       /
	/*--------------------------------------------------------------*/

	@RolesAllowed({ADMIN,USER,MANAGER})
	@GetMapping(GET_PERIOD_TRANSACTIONS_BY_STATUS)
	public ResponseEntity<?> getPeriodTrans(
			@PathVariable("status") String status,
			@PathVariable("debut") String dateDebut,
			@PathVariable("fin") String dateFin) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
			LocalDate debut = LocalDate.parse(dateDebut, formatter);
			LocalDate fin = LocalDate.parse(dateFin, formatter);
			List<Payment> transactionsList = paymentRepository.transactionsByStatusBetweenToDates(status, debut, fin);
			TransactionPageable transactions = new TransactionPageable();
			transactions.setTransaction(transactionsList);
			transactions.setNumberOfElements(transactions.getTransaction().size());
			transactions.setTotalRecords(transactions.getTransaction().size());
			transactions.setTotalPage(1);
			return new ResponseEntity<TransactionPageable> (transactions, HttpStatus.OK);
	}

}
