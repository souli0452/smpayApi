package com.switchmaker.smpay.test;

import com.switchmaker.smpay.wave_ci.WaveWebhookSignatureVerifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.GeneralSecurityException;

@CrossOrigin("*")
@RestController
public class WaveSignatureTest {
	//@RolesAllowed(DEVELOPER)
	@PostMapping("/signature/verify")
	public boolean signatureTest() throws GeneralSecurityException {
		String webhookSecret = "WH_A24ewqtV07VB";
		String waveSignature = "t=1667920421,v1=bd84aea4ff649ae8fd5cc842bad84a3b2cd6fb301781b3c5b457250d20a37721";
		String body = "{\"id\": null, \"type\": \"checkout.session.completed\", \"data\": [{\"id\": \"cos-1b01sghpg100j\", \"amount\": \"100\", \"checkout_status\": \"complete\", \"client_reference\": null, \"currency\": \"XOF\", \"error_url\": \"https://example.com/error\", \"last_payment_error\": null, \"business_name\": \"Anna\\'s Apiaries\", \"payment_status\": \"succeeded\", \"success_url\": \"https://example.com/success\", \"wave_launch_url\": \"https://pay.wave.com/c/cos-1b01sghpg100j?a=100&c=XOF&m=Anna%27s%20Apiaries\", \"when_completed\": \"2022-11-08T15:05:45Z\", \"when_created\": \"2022-11-08T15:05:32Z\", \"when_expires\": \"2022-11-09T15:05:32Z\", \"transaction_id\": \"TCN4Y4ZC3FM\"}]}";
		boolean verify = WaveWebhookSignatureVerifier.verifySignature(waveSignature, body, webhookSecret);
		return verify;
	}
}
