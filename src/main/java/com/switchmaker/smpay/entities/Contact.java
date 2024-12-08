package com.switchmaker.smpay.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.switchmaker.smpay.abstractClass.Identifier;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Data
@Entity
public class Contact extends Identifier {
	@Column(name="first_name")
	@JsonProperty("first_name")
	private String firstName;
	@Column(name="last_name")
	@JsonProperty("last_name")
	private String lastName;
	private String country;
	private String town;
	private String email;
	private String phone;
	@Column(name="message", columnDefinition="text")
	private String message;
	private LocalDateTime date;
}
