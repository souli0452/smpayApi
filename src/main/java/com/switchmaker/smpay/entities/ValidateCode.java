package com.switchmaker.smpay.entities;

import com.switchmaker.smpay.abstractClass.Identifier;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name="code")
public class ValidateCode extends Identifier {
	private String token;
	private Date expiryDate;
	@ManyToOne
	private User user;
}
