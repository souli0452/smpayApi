package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.dto.CostomerApps;
import com.switchmaker.smpay.dto.CustomerDto;
import com.switchmaker.smpay.entities.Customer;
import com.switchmaker.smpay.enumeration.ClientType;
import com.switchmaker.smpay.mappers.CustomerMapper;
import com.switchmaker.smpay.repository.CostomerRepository;
import com.switchmaker.smpay.repository.PlatformRepository;
import com.switchmaker.smpay.services.CostomerService;
import com.switchmaker.smpay.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;

@CrossOrigin("*")
@RestController
@RequestMapping(ROOT_API)
public class CostomerController {
	@Autowired
	CostomerService costomerService;
	@Autowired
	PlatformRepository platformRepository;
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
  @Autowired
  private CostomerRepository costomerRepository;

  @Autowired
  CustomerMapper customerMapper;


	/*----------------------------------/
	/*          Créer un client        /
	/*-------------------------------*/

	@RolesAllowed({ADMIN,MANAGER})
	@PostMapping("/costomer")
	public Response createCostomer(@RequestBody CustomerDto costomer) throws IOException {


				if (costomerService.existsByEmail(costomer.getEmail())) {
					return new Response(FAILURE_CODE,"email existe!",null);
				}
        if (costomer.getClientType().toString().compareToIgnoreCase(ClientType.LEGAL_PERSON.toString())==0){
          List<Map<String, String>> errors = costomer.validateFields();
          if (!errors.isEmpty()) {
             return new Response(FAILURE_CODE,"des erreurs ce sont produites!!",errors);
          }
        }
        else if (costomer.getClientType().toString().compareToIgnoreCase(ClientType.INDIVIDUAL_PERSON.toString())==0){
          List<Map<String, String>> errors = costomer.validateSelectedFields();
          if (!errors.isEmpty()) {
             return new Response(FAILURE_CODE,"des erreurs ce sont produites!",errors);
          }
        }
        else {
           return new Response(FAILURE_CODE,"Le type de client est invalide!",null);
        }

				Customer clientSaved = costomerService.saveCostomer(costomer);
				this.simpMessagingTemplate.convertAndSend("/socket-publisher/client","ok");
				return new Response(SUCCESS_CODE,"Le client a été crée avec succès!",clientSaved);
	}




	/*-------------------------------------/
	/*          Modifier un client        /
	/*----------------------------------*/

	@RolesAllowed({ADMIN,SUPERVISOR,MANAGER})
	@PutMapping("/client")
	public Response updateClient(@RequestParam UUID id, @RequestBody CustomerDto costomerdto){
		try {
				Customer clientSaved = costomerService.updateCostomer(id, costomerdto);
				return new Response(SUCCESS_CODE,"client mise à jour!",clientSaved);
		} catch (DataIntegrityViolationException e) {
			String message = e.getMostSpecificCause().getMessage();
      System.out.println("======================");
      System.out.println(message);
            if (message.contains("email")) {
            	return new Response(FAILURE_CODE,"Email existe déjà !",null);
            } else {
            	return new Response(FAILURE_CODE,"Une erreur est survenue  !",null);
            }
		} catch (IOException e) {
      throw new RuntimeException(e);
    }

  }



	/*------------------------------------/
	/*          Get all clients          /
	/*--------------------------------*/

	@RolesAllowed({ADMIN,MANAGER})
	@GetMapping("/costomers")
	public ResponseEntity<?> getAllCostomer(){
		return new ResponseEntity<List <CostomerApps> >(costomerService.getAllCostomersWithCostomerPlatforms(), HttpStatus.OK);
	}


	/*------------------------------------/
	/*          Get all clients          /
	/*--------------------------------*/

  @RolesAllowed({ADMIN,MANAGER})
  @GetMapping("/customers")
  public ResponseEntity<?> getAllCostomerByType(@RequestParam ClientType type){
    return new ResponseEntity<>(costomerService.getAllCostomersByCustomertype(type), HttpStatus.OK);
  }


	/*------------------------------------/
	/*          Get Client clients          /
	/*--------------------------------*/

  @RolesAllowed({ADMIN,MANAGER})
  @GetMapping("/costomer/{customerId}")
  public ResponseEntity<?> getCostomer(@PathVariable UUID customerId){
    return new ResponseEntity<Customer>(costomerService.getCostomerById(customerId), HttpStatus.OK);
  }

	/*------------------------------------------/
	/*          Supprime un client             /
	/*---------------------------------------*/


	@RolesAllowed({ADMIN,MANAGER})
	@DeleteMapping("/costomer/{id}")
	public ResponseEntity<?> deleteCostomer(@PathVariable("id") UUID id) {
		try {
			costomerService.deleteCostomerById(id);
			this.simpMessagingTemplate.convertAndSend("/socket-publisher/contact","ok");
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity
					.badRequest()
					.body("Une erreur est survenue !");
		}
	}








}
