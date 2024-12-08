package com.switchmaker.smpay.repository;

import com.switchmaker.smpay.entities.CountryPresence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CountryPresenceRepository extends JpaRepository<CountryPresence, UUID> {
	CountryPresence findByCode(String code);
}
