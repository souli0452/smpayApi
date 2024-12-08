package com.switchmaker.smpay.services;

import com.switchmaker.smpay.entities.Currencies;

import java.util.List;
import java.util.UUID;

public interface CurrencyService {
	public Currencies saveCurrency(Currencies currency);
	Currencies updateCurrency(UUID id, Currencies currency);
	public Currencies getCurrencyById(UUID id);
	public Currencies getCurrencyByCode(String code);
	public List<Currencies> getAllCurrencies();
	public void deleteCurrencyById(UUID id);
}
