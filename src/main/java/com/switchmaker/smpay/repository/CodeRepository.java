package com.switchmaker.smpay.repository;

import com.switchmaker.smpay.entities.User;
import com.switchmaker.smpay.entities.ValidateCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CodeRepository extends JpaRepository<ValidateCode, UUID> {

	ValidateCode findByToken(String token);

  ValidateCode findByUser(User user);
}
