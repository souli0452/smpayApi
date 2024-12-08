package com.switchmaker.smpay.serviceImplement;

import com.switchmaker.smpay.entities.Currencies;
import com.switchmaker.smpay.repository.CurrencyRepository;
import com.switchmaker.smpay.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CurrencyServiceImp implements CurrencyService {
	@Autowired
	CurrencyRepository currencyRepo;

	@Override
	public Currencies saveCurrency(Currencies currency) {
		// TODO Auto-generated method stub
		return currencyRepo.save(currency);
	}

	@Override
	public Currencies updateCurrency(UUID id, Currencies currencies) {
		currencyRepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Id "+ id+" invalid"));
		currencies.setId(id);
		return currencyRepo.save(currencies);
	}

	@Override
	public Currencies getCurrencyById(UUID id) {
		// TODO Auto-generated method stub
		return currencyRepo.findById(id).get();
	}

	@Override
	public Currencies getCurrencyByCode(String code) {
		// TODO Auto-generated method stub
		return currencyRepo.findByCode(code);
	}

	@Override
	public List<Currencies> getAllCurrencies() {
		// TODO Auto-generated method stub
		return currencyRepo.findAll(Sort.by(Direction.ASC, "code"));
	}

	@Override
	public void deleteCurrencyById(UUID id) {
		currencyRepo.deleteById(id);
		// TODO Auto-generated method stub

	}

}
