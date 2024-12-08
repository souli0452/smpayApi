package com.switchmaker.smpay.serviceImplement;

import com.switchmaker.smpay.entities.CountryPresence;
import com.switchmaker.smpay.repository.CountryPresenceRepository;
import com.switchmaker.smpay.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CountryServiceImp implements CountryService {

	@Autowired
	CountryPresenceRepository countryPresenceRepo;

	@Override
	public CountryPresence saveCountry(CountryPresence country) {
		// TODO Auto-generated method stub
		return countryPresenceRepo.save(country);
	}

	@Override
	public CountryPresence updateCountry(UUID id, CountryPresence country) {
		countryPresenceRepo.findById((id)).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Id"+ id+" invalid"));
		country.setId(id);
		return countryPresenceRepo.save(country);
	}

	@Override
	public CountryPresence getCountryById(UUID id) {
		// TODO Auto-generated method stub
		return countryPresenceRepo.findById(id).get();
	}

	@Override
	public List<CountryPresence> getAllCountries() {
		// TODO Auto-generated method stub
		return countryPresenceRepo.findAll(Sort.by(Direction.ASC, "name"));
	}



  @Override
	public void deleteCountryById(UUID id) {
		// TODO Auto-generated method stub
		countryPresenceRepo.deleteById(id);
	}

	@Override
	public CountryPresence getCountryByCode(String code) {
		// TODO Auto-generated method stub
		return countryPresenceRepo.findByCode(code);
	}

}
