package com.switchmaker.smpay.entities;

import com.switchmaker.smpay.abstractClass.Identifier;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name="costomer_rate")
public class CostomerRate extends Identifier {
	private float rate;
	@OneToOne
	private Customer costomer;
	@OneToOne
	private MeansPayment meansPayment;
}
