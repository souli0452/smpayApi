package com.switchmaker.smpay.entities;

import com.switchmaker.smpay.abstractClass.Identifier;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name="country_presence")
public class CountryPresence extends Identifier {
	@Column(unique = true, name="name")
	private String name;
	@Column(unique = true, name="code")
	private String code;
	@Column(unique = true, name="indicative")
	private String indicative;

}
