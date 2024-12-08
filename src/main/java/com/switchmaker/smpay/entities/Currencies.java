package com.switchmaker.smpay.entities;

import com.switchmaker.smpay.abstractClass.Identifier;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class Currencies extends Identifier {
	private String wording;
	@Column(unique = true, name="code")
	private String code;
}
