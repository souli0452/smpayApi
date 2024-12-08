package com.switchmaker.smpay.wave_ci;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.MeansOfPaymentConstantValues.CI_WAVE_WEBHOOK_SECRET;

@CrossOrigin("*")
@RestController
@RequestMapping(ROOT_API)
public class EventController {

	@Autowired
	ModelMapper modelMapper;
	@Autowired
	EventRepo eventRepo;

	@PostMapping("/wave/webhooks")
	public ResponseEntity<?> waveWebhook(@RequestHeader(name = "Wave-Signature", required = false) String waveSignature,
			@RequestBody Object request ) {
		try {
			if(waveSignature != null) {
				ObjectMapper mapper = new ObjectMapper();
				EventObject eventObject = convertObjectToEventObject(request);
				String json = mapper.writeValueAsString(eventObject);
				if(WaveWebhookSignatureVerifier.verifySignature(waveSignature, json, CI_WAVE_WEBHOOK_SECRET)) {
					eventRepo.save(eventObject.getData());
					return ResponseEntity.ok().body(eventObject);
				}
			}
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity
					.badRequest().build();
		}
	}


	//@SuppressWarnings("unused")
	private EventObject convertObjectToEventObject(Object object) {
		EventObject eventObject = new EventObject();
		eventObject = modelMapper.map(object, EventObject.class);
		return eventObject;
	}
}
