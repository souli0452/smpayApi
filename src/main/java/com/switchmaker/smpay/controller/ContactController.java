package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.entities.Contact;
import com.switchmaker.smpay.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.ADMIN;

@CrossOrigin("*")
@RestController
@RequestMapping(ROOT_API)
public class ContactController {
	@Autowired
	ContactRepository contactRepo;
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;



	@PostMapping("/contact")
	public ResponseEntity<?> createContact(@RequestBody Contact contact){
		try {
				contact.setDate(LocalDateTime.now());
				Contact contactSaved = contactRepo.save(contact);
				this.simpMessagingTemplate.convertAndSend("/socket-publisher/contact","ok");
				return ResponseEntity.status(HttpStatus.OK).body(contactSaved);
		} catch (Exception e) {
			return ResponseEntity.ok("\"echec\"");
		}

	}



	@RolesAllowed(ADMIN)
	@GetMapping("/contacts")
	public ResponseEntity<?> getContacts(){
		return new ResponseEntity<List <Contact> >(contactRepo.findAll(Sort.by(Direction.DESC, "date")), HttpStatus.OK);
	}


	@RolesAllowed(ADMIN)
	@DeleteMapping("/contact/{id}")
	public ResponseEntity<?> deleteContact(@PathVariable("id") UUID id) {
		try {
			contactRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity
					.badRequest()
					.body("echec");
		}
	}

}
