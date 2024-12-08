package com.switchmaker.smpay.serviceImplement;

import com.switchmaker.smpay.dto.ResponseMessage;
import com.switchmaker.smpay.dto.ValidPaymentInformations;
import com.switchmaker.smpay.entities.*;
import com.switchmaker.smpay.payout.PayoutInformation;
import com.switchmaker.smpay.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.FAILURE_CODE;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.SUCCESS_CODE;

@Service
public class CheckingPaymentInformationServiceImp implements CheckingPaymentInformationService {

	@Autowired
	PlatformService platformService;
	@Autowired
	MeansPaymentService meansPaymentService;
	@Autowired
	CountryService countryService;
	@Autowired
	CurrencyService currencyService;
	@Autowired
	CostomerRateService costomerRateService;
	@Override
	public ResponseMessage checkingPaymentInformation(PayoutInformation payoutInfos) {
		// TODO Auto-generated method stub
		ResponseMessage responseMessage = new ResponseMessage();
		Platform platform = platformService.getPlatformByCode(payoutInfos.getPlatformKey());
		MeansPayment meansPayment = meansPaymentService.getMeansPaymentByCode(payoutInfos.getServiceCode());
		if(platform!=null && meansPayment!=null) {
			CountryPresence country = countryService.getCountryByCode(payoutInfos.getCountryCode());
			Currencies currency = currencyService.getCurrencyByCode(payoutInfos.getCurrency());
			CostomerRate costomerRate = costomerRateService.getCostomerRateByCostomerAndMeansPayment(platform.getUser().getCustomer().getId(), meansPayment.getId());
			if(costomerRate!=null) {
				ValidPaymentInformations validPaymentInformations = new ValidPaymentInformations();
				validPaymentInformations.setPaymentType(meansPayment.getWording());
				validPaymentInformations.setPayoutInformation(payoutInfos);
				validPaymentInformations.setPlatform(platform);
				validPaymentInformations.setRateApplied(costomerRate.getRate());
				responseMessage.setCode(SUCCESS_CODE);
				responseMessage.setMessage("Informations de paiement correctes");
				responseMessage.setData(validPaymentInformations);
				return responseMessage;
			}else {
				ValidPaymentInformations validPaymentInformations = new ValidPaymentInformations();
				validPaymentInformations.setPaymentType(meansPayment.getWording());
				validPaymentInformations.setPayoutInformation(payoutInfos);
				validPaymentInformations.setPlatform(platform);
				validPaymentInformations.setRateApplied(meansPayment.getApplicableRate());

				responseMessage.setCode(SUCCESS_CODE);
				responseMessage.setMessage("Informations de paiement correctes");
				responseMessage.setData(validPaymentInformations);
				return responseMessage;
			}
		}else {
			responseMessage.setCode(FAILURE_CODE);
			responseMessage.setMessage("Informations de paiement incorrectes");
			return responseMessage;
		}
	}
}
