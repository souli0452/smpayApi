package com.switchmaker.smpay.repository;


import com.switchmaker.smpay.entities.MeansPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MeansPaymentRepository extends JpaRepository<MeansPayment, UUID> {

    List<MeansPayment> getMeansPaymentByCountriesId(UUID id);

    MeansPayment getMeansPaymentByCategoryId(UUID id);

    MeansPayment getMeansPaymentByCode(String code);

    MeansPayment findByCode(String paymentCode);
}
