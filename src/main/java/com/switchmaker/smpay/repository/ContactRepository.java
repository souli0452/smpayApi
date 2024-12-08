package com.switchmaker.smpay.repository;

import com.switchmaker.smpay.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {
	@Query(value = "SELECT COUNT (*) FROM contact", nativeQuery = true)
	Integer findCountContact();
}
