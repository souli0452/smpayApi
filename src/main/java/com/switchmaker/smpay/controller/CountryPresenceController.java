package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.entities.CountryPresence;
import com.switchmaker.smpay.services.CountryService;
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
public class CountryPresenceController {
	@Autowired
	CountryService countryService;

	/*------------------------------------------/
	/*        Ajouter un pays de présence      /
	/*---------------------------------------*/

	@RolesAllowed({ADMIN,MANAGER})
	@PostMapping("/country")
	public ResponseEntity<?> createCountry(@RequestBody CountryPresence country){
		try {
				return ResponseEntity.status(HttpStatus.OK).body(countryService.saveCountry(country));
		} catch (DataIntegrityViolationException e) {
			String message = e.getMostSpecificCause().getMessage();
            if (message.contains("name")) {
            	return ResponseEntity.ok("\"Le pays existe déjà !\"");
            } else if (message.contains("code")) {
            	return ResponseEntity.ok("\"Le code existe déjà !\"");
            }else if (message.contains("indicative")) {
            	return ResponseEntity.ok("\"L'indicatif existe déjà !\"");
            } else {
            	return ResponseEntity.ok("\"Une erreur est survenue !\"");
            }
		}
	}


		/*---------------------------------/
		/*        Liste des pays          /
		/*------------------------------*/

		@RolesAllowed({ADMIN,MANAGER})
		@GetMapping("/countries")
		public ResponseEntity<?> getAllCountries(){
			return new ResponseEntity<List <CountryPresence> >(countryService.getAllCountries(), HttpStatus.OK);
		}


		/*----------------------------------------/
		/*          Supprime un pays             /
		/*-------------------------------------*/


		@RolesAllowed({ADMIN,MANAGER})
		@DeleteMapping("/country/{id}")
		public ResponseEntity<?> deleteCountry(@PathVariable("id") UUID id) {
			try {
				countryService.deleteCountryById(id);
				return new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				return ResponseEntity
						.badRequest()
						.body("Une erreur est survenue !");
			}
		}
		/*----------------------------------------/
		/*          Affiche un pays              /
		/*-------------------------------------*/

		@RolesAllowed({ADMIN,MANAGER})
		@GetMapping("/country/{id}")
		public ResponseEntity<?> getCountryById(@PathVariable UUID id){
			try {
				return new ResponseEntity<>(countryService.getCountryById(id), HttpStatus.OK);
			}catch (Exception e){
				return ResponseEntity
						.badRequest()
						.body("Une erreur est survenue !");		}
		}
		/*----------------------------------------/
		/*  Affiche un pays en fonctionn du code /
		/*-------------------------------------*/

	@RolesAllowed({ADMIN,MANAGER})
	@GetMapping("/country/{code}")
	public ResponseEntity<?> getCountryByCode(@PathVariable String code){
		try {
			return new ResponseEntity<>(countryService.getCountryByCode(code), HttpStatus.OK);
		}catch (Exception e){
			return ResponseEntity
					.badRequest()
					.body("Une erreur est survenue !");		}
	}
		/*----------------------------------------/
		/*          Modifie un pays              /
		/*-------------------------------------*/

	    @RolesAllowed({ADMIN,MANAGER})
		@PutMapping("/country/{id}")
		public ResponseEntity<?>updateCountry(@PathVariable UUID id, @RequestBody CountryPresence country){
			try {
				return new ResponseEntity<>(countryService.updateCountry(id, country), HttpStatus.OK);
			}catch (Exception e){
				return ResponseEntity
						.badRequest()
						.body("Une erreur est survenue !");
			}
		}
}
