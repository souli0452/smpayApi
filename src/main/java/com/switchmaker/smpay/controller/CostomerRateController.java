package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.entities.CostomerRate;
import com.switchmaker.smpay.services.CostomerRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;


@CrossOrigin("*")
@RestController
@RequestMapping(ROOT_API)
public class CostomerRateController {

	@Autowired
	CostomerRateService costomerRateService;

	/*-------------------------------------/
	/*         Créer un taux client       /
	/*----------------------------------*/

 @RolesAllowed({ADMIN,MANAGER})
	@PostMapping("/costomer/{costomerId}/means-payment/{meansPayment}/rate/{rateValue}")
	public ResponseEntity<?> addCostomerRate(
			@PathVariable("costomerId") UUID costomerId,
			@PathVariable("meansPayment") UUID meansPayment,
			@PathVariable("rateValue") float rateValue){

		try {
				return ResponseEntity.status(HttpStatus.OK)
						.body(costomerRateService.saveCostomerRate(costomerId, meansPayment, rateValue));
		} catch (Exception e) {

            	return ResponseEntity.ok("\"Une erreur est survenue !\"");
		}
	}


	 /*-------------------------------------------/
	/*        Lister les taux d'un client        /
   /-------------------------------------------*/

	@RolesAllowed({ADMIN,DEVELOPER,MANAGER})
	@GetMapping("/costomer-rates/costomer/{costomerId}")
	public ResponseEntity<?> getCostomerRate(
			@PathVariable("costomerId") UUID costomerId){
		List <CostomerRate> costomerRates = costomerRateService.getCostomerRatesById(costomerId);
		return new ResponseEntity<List <CostomerRate> >(costomerRates, HttpStatus.OK);
	}

	@RolesAllowed({ADMIN,MANAGER})
	@GetMapping("/costomer-rates")
	public ResponseEntity<?>getAllCustomersRates(){
		try {
			return new ResponseEntity<>(costomerRateService.getAllCustomersRates(), HttpStatus.OK);

		}catch (Exception e){
			return ResponseEntity
					.badRequest()
					.body("Une erreure est survenue");
		}
	}
	@RolesAllowed({ADMIN,MANAGER})
	@PutMapping("/costomer-rate/{id}")
	public ResponseEntity<?> updateCustomerRate(@PathVariable UUID id, @RequestBody CostomerRate costomerRate){
		try {
			return new ResponseEntity<>(costomerRateService.updateCostumerRate(id, costomerRate), HttpStatus.OK);
		}catch (Exception e){
			return ResponseEntity.badRequest().body("Une erreure est survenue");
		}
	}
	@RolesAllowed({ADMIN,MANAGER})
	@DeleteMapping("/costomer-rate/{id}")
	public ResponseEntity<?>deleteCustomerRate(@PathVariable UUID id){
		try {
			costomerRateService.deleCostumerRate(id);
			return  new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e){
			return ResponseEntity.badRequest().body("Une erreure est survenue");
		}
	}



}
