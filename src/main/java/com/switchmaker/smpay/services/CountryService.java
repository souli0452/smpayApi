package com.switchmaker.smpay.services;

import com.switchmaker.smpay.entities.CountryPresence;

import java.util.List;
import java.util.UUID;

public interface CountryService {
	public CountryPresence saveCountry(CountryPresence country);
	CountryPresence updateCountry(UUID id, CountryPresence country);
	public CountryPresence getCountryById(UUID id);
	public CountryPresence getCountryByCode(String code);
	public List<CountryPresence> getAllCountries();

	public void deleteCountryById(UUID id);
}
