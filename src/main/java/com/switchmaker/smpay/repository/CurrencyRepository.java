package com.switchmaker.smpay.repository;

import com.switchmaker.smpay.entities.Currencies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Currencies, UUID> {
	Currencies findByCode(String code);
}
