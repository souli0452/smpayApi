package com.switchmaker.smpay.serviceImplement;

import com.switchmaker.smpay.controller.FilesController;
import com.switchmaker.smpay.entities.Category;
import com.switchmaker.smpay.entities.CountryPresence;
import com.switchmaker.smpay.entities.Currencies;
import com.switchmaker.smpay.entities.MeansPayment;
import com.switchmaker.smpay.repository.CategoryRepository;
import com.switchmaker.smpay.repository.MeansPaymentRepository;
import com.switchmaker.smpay.services.CountryService;
import com.switchmaker.smpay.services.CurrencyService;
import com.switchmaker.smpay.services.MeansPaymentService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.switchmaker.smpay.constant.RootPath.imageRootPath;



@Service
public class MeansPaymentServiceImp implements MeansPaymentService {

	@Autowired
	MeansPaymentRepository meansPaymentRepo;
	@Autowired
	CountryService countryService;
	@Autowired
	CurrencyService currencyService;
	@Autowired
	CategoryRepository categoryRepository;


	@Override
	public MeansPayment saveMeansPayment(MeansPayment meansPayment, UUID categoryId) throws IOException {
		// TODO Auto-generated method stub
		meansPayment.setLogo(uploadLogo(meansPayment.getLogo(), meansPayment.getWording()));
		Category category = categoryRepository.findById(categoryId).get();
		meansPayment.setCategory(category);
		setMeansPayment(meansPayment, categoryId);
		meansPayment.setCode(meansPayment.getCode().toUpperCase());
		return meansPaymentRepo.save(meansPayment);
	}

	@Override
	public MeansPayment getMeansPaymentById(UUID id) {
		// TODO Auto-generated method stub
		Optional<MeansPayment> payementOpt = meansPaymentRepo.findById(id);
		return payementOpt.get();
	}

	@Override
	public MeansPayment updateMeansPayment(UUID id, MeansPayment meansPayment) throws IOException {
		meansPaymentRepo.findById(id);
		setMeansPayment(meansPayment, meansPayment.getCategory().getId());
		meansPayment.setLogo(uploadLogo(meansPayment.getLogo(), meansPayment.getWording()));
		meansPayment.setId(id);
		return meansPaymentRepo.save(meansPayment);
	}

	@Override
	public List<MeansPayment> getAllMeansPayment() {
		// TODO Auto-generated method stub
		return meansPaymentRepo.findAll(Sort.by(Direction.ASC, "wording"));
	}

	@Override
	public void deleteMeansPaymentById(UUID id) {
		// TODO Auto-generated method stub
		meansPaymentRepo.deleteById(id);
	}

	@Override
	public MeansPayment updateStatus(UUID id) {
		MeansPayment meansPayment = meansPaymentRepo.findById(id).get();
		boolean status = meansPayment.isActive()?  false : true ;
		meansPayment.setActive(status);
		return meansPaymentRepo.save(meansPayment);
	}

	@Override
	public List<MeansPayment> getMeansPaymentByCountry(UUID id) {
		return meansPaymentRepo.getMeansPaymentByCountriesId(id);
	}

	@Override
	public MeansPayment getMeansPaymentByCategory(UUID id) {
		return meansPaymentRepo.getMeansPaymentByCategoryId(id);
	}

	@Override
	public MeansPayment getMeansPaymentByCode(String code) {
		return meansPaymentRepo.getMeansPaymentByCode(code);
	}

	void setMeansPayment(MeansPayment meansPayment, UUID categoryId){
		Category category = categoryRepository.findById(categoryId).get();
		meansPayment.setCategory(category);
		List<CountryPresence> countries = new ArrayList<>();
		for (CountryPresence countryPresence: meansPayment.getCountries()) {
			CountryPresence existingCountryPresence = countryService.getCountryById(countryPresence.getId());
			if (existingCountryPresence != null) {
				countries.add(existingCountryPresence);
			}
		}
		meansPayment.setCountries(countries);
		List<Currencies> currencies = new ArrayList<>();
		for (Currencies currency : meansPayment.getCurrencies()) {
			Currencies existingCurrency = currencyService.getCurrencyById(currency.getId());
			if (existingCurrency != null) {
				currencies.add(existingCurrency);
			}
		}
		meansPayment.setCurrencies(currencies);
	}
	String uploadLogo(String logo, String  logoName) throws IOException {
		byte[] logoBase = Base64.decodeBase64(logo);
		new FileOutputStream(String.valueOf(imageRootPath.resolve(logoName+ ".jpeg"))).write(logoBase);
		String fileUrl = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", logoName +".jpeg").build().toString();
		return fileUrl;
	}
}
