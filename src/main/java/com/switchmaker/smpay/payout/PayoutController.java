package com.switchmaker.smpay.payout;

import com.switchmaker.smpay.coris_bf.CorisServices;
import com.switchmaker.smpay.dto.ResponseMessage;
import com.switchmaker.smpay.dto.ValidPaymentInformations;
import com.switchmaker.smpay.entities.Payment;
import com.switchmaker.smpay.moov_bf.MoovServices;
import com.switchmaker.smpay.mtn_ci.MtnServices;
import com.switchmaker.smpay.orange_bf.OrangeServices;
import com.switchmaker.smpay.services.CheckingPaymentInformationService;
import com.switchmaker.smpay.services.PaymentService;
import com.switchmaker.smpay.services.UserService;
import com.switchmaker.smpay.utilsClass.UtilsClass;
import com.switchmaker.smpay.wave_ci.WaveServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Objects;

import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;
import static com.switchmaker.smpay.constant.values.MeansOfPaymentConstantValues.*;
import static com.switchmaker.smpay.payout.PayoutUrls.GET_PAYOUT_STATUS;
import static com.switchmaker.smpay.payout.PayoutUrls.INITIATE_PAYOUT;

@CrossOrigin("*")
@RestController
public class PayoutController {

	@Autowired
	OrangeServices orangeServices;
	@Autowired
	MoovServices moovServices;
	@Autowired
	CorisServices corisServices;
	@Autowired
	WaveServices waveServices;
	@Autowired
	MtnServices mtnServices;
	@Autowired
	UserService userService;
	@Autowired
	CheckingPaymentInformationService checkingInformationService;
	@Autowired
	PaymentService paymentService;


	/*-------------------------------------------/
	/*       Initier un paiement client         /
	/*----------------------------------------*/
	  @RolesAllowed({ADMIN,DEVELOPER,USER})
	  @PostMapping(INITIATE_PAYOUT) public ResponseEntity<?>
	  costomerPayout(@RequestBody PayoutInformation payoutInfos) {
		  try {

			ResponseMessage responseMessage = checkingInformationService.checkingPaymentInformation(payoutInfos);
			if(Objects.equals(responseMessage.getCode(), "200")) {
				ValidPaymentInformations validPaymentInformation = (ValidPaymentInformations) responseMessage.getData();
				switch (validPaymentInformation.getPayoutInformation().getServiceCode()) {
						case BF_ORANGE_MONEY_SERVICE:
							return orangeServices.payment(validPaymentInformation);

						case BF_MOOV_MONEY_SERVICE:
							return moovServices.clientPayment(validPaymentInformation);

						case BF_CORIS_MONEY_CODE_SERVICE:
							return corisServices.paymentOtp(validPaymentInformation);

						case BF_CORIS_MONEY_SERVICE:
							return corisServices.paymentAccountDebited(validPaymentInformation);

						case CI_WAVE_SERVICE:
							return waveServices.clientPayment(validPaymentInformation);

						case CI_MTN_SERVICE:
							return mtnServices.clientPayment(validPaymentInformation);

						default:
							return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Service code non valide2"),HttpStatus.OK);
						}
			}else {
			  return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"paramètres erronés"),HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Une erreur est survenue"),HttpStatus.OK);
		}
	}



  	/*-----------------------------------------------------/
  	/*               Rechercher un paiement               /
  	/*--------------------------------------------------*/

  	@RolesAllowed({ADMIN,DEVELOPER,USER})
  	@GetMapping(GET_PAYOUT_STATUS)
  	public ResponseEntity<?> getPayment(@PathVariable("code") String code) {
  		try {
  			Payment payment = paymentService.getPaymentByPaymentCode(code);
			switch (payment.getServiceCode()) {
				case BF_ORANGE_MONEY_SERVICE:
					return ResponseEntity.ok(paymentService.getPayment(payment));
				case BF_CORIS_MONEY_SERVICE:
					return ResponseEntity.ok(paymentService.getPayment(payment));
  				case BF_MOOV_MONEY_SERVICE:
  					return ResponseEntity.ok(paymentService.updateBFMoovPayment(payment));
  				case CI_WAVE_SERVICE:
  					return ResponseEntity.ok(paymentService.updateCIWavePayment(payment));
  				case CI_MTN_SERVICE:
  					return ResponseEntity.ok(paymentService.updateCIMTNPayment(payment));

  				default:
  					return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Une erreur est survenue"),HttpStatus.OK);
  				}

		} catch (Exception e) {
			return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"Une erreur est survenue"),HttpStatus.OK);
		}
  	}
}

