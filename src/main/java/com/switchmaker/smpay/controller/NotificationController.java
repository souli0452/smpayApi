package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.entities.Customer;
import com.switchmaker.smpay.entities.Notification;
import com.switchmaker.smpay.repository.CostomerRepository;
import com.switchmaker.smpay.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.USER;

@CrossOrigin("*")
@RestController
@RequestMapping(ROOT_API)
public class NotificationController {
	@Autowired
	NotificationRepository notificationRepo;
	@Autowired
	CostomerRepository clientRepo;
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;




	/*-------------------------------------------------------/
	/*          liste des notifications d'un client         /
	/*----------------------------------------------------*/

	@RolesAllowed(USER)
	@GetMapping("/notification/client/{id}/status/{status}")
	public ResponseEntity<?> getClientNotification(@PathVariable("id") UUID id, @PathVariable("status") boolean status){
		Optional<Customer> clientOptional = clientRepo.findById(id);
		return new ResponseEntity<List <Notification> >(notificationRepo.findByCostomerAndStatus(clientOptional.get(), status, Sort.by(Direction.DESC, "date")), HttpStatus.OK);
	}



	/*-------------------------------------------------------/
	/*          liste des messages d'un client              /
	/*----------------------------------------------------*/

	@RolesAllowed(USER)
	@GetMapping("/message/client/{id}")
	public ResponseEntity<?> getClientMessage(@PathVariable("id") UUID id){
		Optional<Customer> clientOptional = clientRepo.findById(id);
		List <Notification> listNoti = notificationRepo.findByCostomer(clientOptional.get(), Sort.by(Direction.DESC, "date"));
		for(Notification notif : listNoti) {
			notif.setStatus(true);
			notificationRepo.save(notif);
		}

		return new ResponseEntity<List <Notification> >(listNoti, HttpStatus.OK);
	}



	/*-----------------------------------------------/
	/*          Supprimer une notification          /
	/*--------------------------------------------*/

	@RolesAllowed(USER)
	@DeleteMapping("/notification/{id}")
	public ResponseEntity<?> deleteNotification(@PathVariable("id") UUID id) {
			try {
					Optional<Notification> notificationOpt = notificationRepo.findById(id);
					notificationRepo.delete(notificationOpt.get());
					this.simpMessagingTemplate.convertAndSend("/socket-publisher/notification/deleted",notificationOpt.get().getCostomer().getId().toString());
					return new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				return ResponseEntity
						.badRequest()
						.body("\"Une erreur est survenue !\"");
			}
	}


}
