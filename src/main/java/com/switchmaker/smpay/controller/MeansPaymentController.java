package com.switchmaker.smpay.controller;


import com.switchmaker.smpay.entities.MeansPayment;
import com.switchmaker.smpay.services.MeansPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;

@CrossOrigin("*")
@RestController
@RequestMapping(ROOT_API)
public class MeansPaymentController {
	@Autowired
	MeansPaymentService meansPaymentService;

	/*-----------------------------------------------/
	/*         Ajouter un moyen de paiement         /
	/*--------------------------------------------*/

	@RolesAllowed({ADMIN,MANAGER})
	@PostMapping("/mean/payment/category/{categoryId}")
	public ResponseEntity<?> addMeansPayment(
			@RequestBody MeansPayment meanPayment,
			@PathVariable UUID categoryId
			){

		try {
			meanPayment.setWording(meanPayment.getWording().toUpperCase());
			return ResponseEntity.status(HttpStatus.OK)
					.body(meansPaymentService.saveMeansPayment(meanPayment,categoryId));
		} catch (DataIntegrityViolationException e) {
			String message = e.getMostSpecificCause().getMessage();
            if (message.contains("wording")) {
            	return ResponseEntity.ok("\"Le moyen de paiement existe déjà !\"");
            } else if (message.contains("color_code")) {
            	return ResponseEntity.ok("\"Le code couleur existe déjà !\"");
            } else {
            	return ResponseEntity.ok("\"Une erreur est survenue !\"");
            }
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}


	/*------------------------------------------------------/
	/*          Lister tous les moyens de paiement         /
	/*---------------------------------------------------*/

	@RolesAllowed({ADMIN,DEVELOPER,MANAGER})
	@GetMapping("/mean/payment")
	public ResponseEntity<?> getMeansPayment(){
		return new ResponseEntity<List <MeansPayment> >(meansPaymentService.getAllMeansPayment(), HttpStatus.OK);
	}
	@RolesAllowed({ADMIN,DEVELOPER,MANAGER})
	@GetMapping("/mean/payment/{id}")
	public ResponseEntity<?> getMeansPaymentById(@PathVariable UUID id){
		return new ResponseEntity<MeansPayment>(meansPaymentService.getMeansPaymentById(id), HttpStatus.OK);
	}


	/*--------------------------------------------------/
	/*          Supprimer un moyen de paiement         /
	/*-----------------------------------------------*/

	@RolesAllowed({ADMIN,MANAGER})
	@DeleteMapping("/mean/payment/{id}")
	public ResponseEntity<?> deleteMeansPayment(@PathVariable("id") UUID id) {
			try {
				 meansPaymentService.deleteMeansPaymentById(id);
				 return new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				return ResponseEntity
						.badRequest()
						.body("Une erreur est survenue !");
			}
	}
	@RolesAllowed({ADMIN,MANAGER})
	@GetMapping("/mean/payment/code/{code}")
	public ResponseEntity<?> getMeansPaymentByCode(@PathVariable String code){
		try {
			return new ResponseEntity<>(meansPaymentService.getMeansPaymentByCode(code), HttpStatus.OK);
		}catch (Exception e){
			return ResponseEntity
					.badRequest()
					.body("Une erreur est survenue !");
		}
	}
	@RolesAllowed({ADMIN,MANAGER})
	@GetMapping("/mean/payment/category/{id}")
	public ResponseEntity<?> getMeansPaymentByCategory(@PathVariable UUID id){
		try {
			return new ResponseEntity<>(meansPaymentService.getMeansPaymentByCategory(id), HttpStatus.OK);
		}catch (Exception e){
			return ResponseEntity
					.badRequest()
					.body("Une erreur est survenue !");
		}
	}
	@RolesAllowed({ADMIN,MANAGER})
	@GetMapping("/mean/payment/country/{id}")
	public ResponseEntity<List<MeansPayment>> getMeansPaymentBycountry(@PathVariable UUID id){

			return new ResponseEntity<>(meansPaymentService.getMeansPaymentByCountry(id), HttpStatus.OK);

	}

	/*--------------------------------------------------/
	/*          Modifier un moyen de paiement          /
	/*-----------------------------------------------*/

	@RolesAllowed({ADMIN,MANAGER})
	@PutMapping("/mean/payment/{id}")
	public ResponseEntity<?> updateMeansPayment(@PathVariable UUID id,
												@RequestBody MeansPayment meansPayment){
		try {
			return new ResponseEntity<>(meansPaymentService
					.updateMeansPayment(id,meansPayment), HttpStatus.OK);
		}catch (Exception e){
			return ResponseEntity
					.badRequest()
					.body("Une erreur est survenue !");
		}
	}
	@RolesAllowed({ADMIN,MANAGER})
	@PutMapping("/mean/payment/{id}/status")
	public ResponseEntity<?> changeMeansPaymentStatus(@PathVariable UUID id){
			try {
				return new ResponseEntity<>(meansPaymentService.updateStatus(id), HttpStatus.OK);
			}catch (Exception e){
				return ResponseEntity
						.badRequest()
						.body("Une erreur est survenue !");
			}

	}

}
