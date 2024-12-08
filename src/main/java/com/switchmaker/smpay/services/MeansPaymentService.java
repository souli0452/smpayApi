package com.switchmaker.smpay.services;


import com.switchmaker.smpay.entities.MeansPayment;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MeansPaymentService {

	MeansPayment saveMeansPayment(MeansPayment meansPayment, UUID categoryId) throws IOException;

	public MeansPayment getMeansPaymentById(UUID id);
	MeansPayment updateMeansPayment(UUID id, MeansPayment meansPayment) throws IOException;
	public List<MeansPayment> getAllMeansPayment();
	public void deleteMeansPaymentById(UUID id);

	MeansPayment updateStatus(UUID id);

	List<MeansPayment> getMeansPaymentByCountry(UUID id);

	MeansPayment getMeansPaymentByCategory(UUID id);

	MeansPayment getMeansPaymentByCode(String code);
}
