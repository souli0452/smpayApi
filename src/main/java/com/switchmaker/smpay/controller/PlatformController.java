package com.switchmaker.smpay.controller;


import com.switchmaker.smpay.entities.Platform;
import com.switchmaker.smpay.entities.User;
import com.switchmaker.smpay.repository.PaymentRepository;
import com.switchmaker.smpay.repository.PlatformRepository;
import com.switchmaker.smpay.repository.UserRepository;
import com.switchmaker.smpay.services.CostomerService;
import com.switchmaker.smpay.services.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;

@CrossOrigin("*")
@RestController
@RequestMapping(ROOT_API)
public class PlatformController {

	@Autowired
	CostomerService costomerService;
	@Autowired
	PlatformService platformService;
	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PlatformRepository platformRepository;

	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


	/*---------------------------------------/
	/*         Créer une plateforme         /
	/*------------------------------------*/

	@RolesAllowed({ADMIN,SUPERVISOR, USER, APPLICATION})
	@PostMapping("/platform/client/{id}")
	public ResponseEntity<?> addPlatform(
			@PathVariable("id") UUID id,
			@RequestBody Platform platform){

		try {
			List<User> users = userRepository.findAll();
			User user = userRepository.findById(id).get();
			Platform checkPlatform = platformService.getByUserAndPlatformName(user, platform.getPlatformName());
			if(checkPlatform!=null) {
				return ResponseEntity.ok("\"Plateforme existe déjà !\"");
			}

			if (platform.getPaymentAccount() == null) {
				platform.setPaymentAccount(user.getCustomer().getCustomerAccountNumberForPayment()); // Assign customer account
			}

			if (platform.getHomeStructure() == null) {
				platform.setHomeStructure(user.getCustomer().getDomiciliaryAccountStructure()); // Assign customer home structure
			}

			if (platform.getPaymentAccountType() == null) {
				platform.setPaymentAccountType(user.getCustomer().getCustomerAccountTypeForPayment().toString()); // Assign customer payment account type
			}

			platform.setUser(user);
			Platform platformSaved = platformService.savePlatform(platform);
			this.simpMessagingTemplate.convertAndSend("/socket-publisher/platform",id.toString());
			return ResponseEntity.status(HttpStatus.OK).body(platformSaved);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}

	}

	/*------------------------------------------------/
	/*          Lister toutes les plateformes        /
	/*---------------------------------------------*/

	@RolesAllowed({ADMIN,SUPERVISOR, MANAGER, APPLICATION})
	@GetMapping("/platforms")
	public ResponseEntity<?> getPlatforms(){
		List <Platform> platforms = platformService.getAllPlatforms();
		return new ResponseEntity<List <Platform> >(platforms, HttpStatus.OK);
	}

	/*-----------------------------------/
	/*          Update platform         /
	/*--------------------------------*/

	@RolesAllowed({USER,SUPERVISOR, APPLICATION})
	@PutMapping("/platform/{id}")
	public ResponseEntity<?> updatePlatform(
			@PathVariable("id") UUID id,
			@RequestBody Platform platform) {
		try {
			Platform currentPlatform = platformService.getPlatformById(id);
			if(!currentPlatform.getPlatformName().equals(platform.getPlatformName()) && platformService.getByUserAndPlatformName(currentPlatform.getUser(), platform.getPlatformName()) != null) {
				return ResponseEntity.ok("\"Plateforme existe déjà !\"");

			}
		    if(platform.getPlatformName() != null) {
		 //   	platform.setAccountCreationDate(LocalDateTime.now());
		    }
			platform.setId(id);
		    Platform platformUpdated = platformService.updatePlatform(id, platform);
		    return new ResponseEntity<Platform>(platformUpdated,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("\"echec\"",HttpStatus.BAD_REQUEST);
		}

	}



	/*--------------------------------------------/
	/*          Supprimer une plateforme         /
	/*-----------------------------------------*/

	@RolesAllowed({SUPERVISOR,USER, APPLICATION})
	@DeleteMapping("/platform/{id}")
	public ResponseEntity<?> deletePlatform(@PathVariable("id") UUID id) {
			try {
				 platformService.deletePlatform(id);
				 this.simpMessagingTemplate.convertAndSend("/socket-publisher/platform/deleted",id.toString());
				 return new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				return ResponseEntity
						.badRequest()
						.body("Une erreur est survenue !");
			}
	}



	/*-------------------------------------------------/
	/*      Lister les plateformes d'un client        /
	/*----------------------------------------------*/

	@RolesAllowed({ADMIN,SUPERVISOR, USER, APPLICATION})
	@GetMapping("/platform/client/{id}")
	public ResponseEntity<?> getClientPlatforms(@PathVariable("id") UUID id) {
		User user = userRepository.findById(id).get();
		try {
			List<Platform> platforms = platformRepository.findByUser(user);
			return new ResponseEntity<List <Platform> >(platforms,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("\"echec\"",HttpStatus.BAD_REQUEST);
		}
	}


	/*-------------------------------------------------/
	/*      plateforme  d'un compte applicatif        /
	/*----------------------------------------------*/

	@RolesAllowed({ADMIN,SUPERVISOR, USER, APPLICATION})
	@GetMapping("/platform/compte/applicatif/{id}")
	public ResponseEntity<?> getClientPlatformApp(@PathVariable("id") UUID compteApplicatifId) {
		try {
			List <Platform> platforms = platformService.getClientPlatformApp(compteApplicatifId);
			return new ResponseEntity <> (platforms,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("\"echec\"",HttpStatus.BAD_REQUEST);
		}
	}

	/*------------------------------------------------------------------------------------------------------------/
	/*      Une méthode qui permet de récuperer un compte apllicatif en fonction de l'ID de l'application        /
	/*---------------------------------------------------------------------------------------------------------*/


	@RolesAllowed({ADMIN,SUPERVISOR, USER, APPLICATION})
	@GetMapping("/platform/{id}")
	public ResponseEntity<?> getAccountApp(@PathVariable("id") UUID applicatifId) {
		try {
			List <Platform> platforms = platformService.getClientPlatformApp(applicatifId);
			return new ResponseEntity <> (platforms,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("\"echec\"",HttpStatus.BAD_REQUEST);
		}
	}



}

