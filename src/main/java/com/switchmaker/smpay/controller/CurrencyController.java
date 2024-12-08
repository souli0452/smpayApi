package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.entities.Currencies;
import com.switchmaker.smpay.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.ADMIN;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.MANAGER;

@CrossOrigin("*")
@RestController
@RequestMapping(ROOT_API)
public class CurrencyController {
	@Autowired
	CurrencyService currencyService;

	/*-------------------------------------------/
	/*        Ajouter une devise                /
	/*---------------------------------------*/

	@RolesAllowed({ADMIN,MANAGER})
	@PostMapping("/currency")
	public ResponseEntity<?> createCurrency(@RequestBody Currencies currency){
		try {
				return ResponseEntity.status(HttpStatus.OK).body(currencyService.saveCurrency(currency));
		} catch (DataIntegrityViolationException e) {
			String message = e.getMostSpecificCause().getMessage();
            if (message.contains("code")) {
            	return ResponseEntity.ok("\"Le code existe déjà !\"");
            } else {
            	return ResponseEntity.ok("\"Une erreur est survenue !\"");
            }
		}
	}


		/*---------------------------------/
		/*      Liste les devises         /
		/*------------------------------*/

		@RolesAllowed({ADMIN,MANAGER})
		@GetMapping("/currencies")
		public ResponseEntity<?> getAllCurrencies(){
			return new ResponseEntity<List <Currencies> >(currencyService.getAllCurrencies(), HttpStatus.OK);
		}


		/*----------------------------------------/
		/*        Supprime une devise            /
		/*-------------------------------------*/


		@RolesAllowed({ADMIN,MANAGER})
		@DeleteMapping("/currency/{id}")
		public ResponseEntity<?> deleteCurrency(@PathVariable("id") UUID id) {
			try {
				currencyService.deleteCurrencyById(id);
				return new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				return ResponseEntity
						.badRequest()
						.body("Une erreur est survenue !");
			}
		}
		/*----------------------------------------/
		/*          affiche une devise           /
		/*-------------------------------------*/

		@RolesAllowed({ADMIN,MANAGER})
		@GetMapping("/currency/{id}")
		public ResponseEntity<?> geCurrencyById(@PathVariable UUID id){
			try {
				return new ResponseEntity<>(currencyService.getCurrencyById(id), HttpStatus.OK);
			}catch (Exception e){
				return  ResponseEntity
						.badRequest()
						.body("Une erreur est survenue !");
			}
		}
		/*----------------------------------------/
		/* affiche une devise en fonction du code/
		/*-------------------------------------*/

		@RolesAllowed({ADMIN,MANAGER})
		@GetMapping("/currency/{code}")
		public ResponseEntity<?> geCurrencyByCode(@PathVariable String code){
		try {
			return new ResponseEntity<>(currencyService.getCurrencyByCode(code), HttpStatus.OK);
		}catch (Exception e){
			return  ResponseEntity
					.badRequest()
					.body("Une erreur est survenue !");
		}
	}
		/*----------------------------------------/
		/*          modifie une devise           /
		/*-------------------------------------*/

		@RolesAllowed({ADMIN,MANAGER})
		@PutMapping("/currency/{id}")
		public ResponseEntity<?> updateCurrency(@PathVariable UUID id, @RequestBody Currencies currencies){
			try {
				return new ResponseEntity<>(currencyService.updateCurrency(id, currencies), HttpStatus.OK);
			}catch (Exception e){
				return ResponseEntity
						.badRequest()
						.body("Une erreur est survenue !");
			}
		}
}
