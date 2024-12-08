package com.switchmaker.smpay.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.switchmaker.smpay.abstractClass.Identifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "means_payment")
@AllArgsConstructor
@NoArgsConstructor
public class  MeansPayment extends Identifier {
	@Column(unique = true)
	private String wording;
	@Column(unique = true, name="code")
	private String code;
	@Column(columnDefinition = "TEXT")
	private String logo;
	@JsonProperty("applicable_rate")
	private float applicableRate;
	@JsonProperty("operator_rate")
	private float operatorRate;
	@Column(unique = true, name="color_code")
	@JsonProperty("color_code")
	private String colorCode;
	private boolean active = true;
//	@OneToMany(mappedBy="meanPayment")
//	@Setter(value = AccessLevel.NONE)
//	@Getter(value = AccessLevel.NONE)
//	private Collection<PaymentServices> paymentServices;
	@ManyToOne
	private Category category;
	@ManyToMany
	@JoinTable(
			name = "payments_currencies",
			joinColumns = @JoinColumn(name = "payment_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "currency_id", referencedColumnName = "id")

	)
	private List<Currencies> currencies = new ArrayList<>();
	@ManyToMany
	@JoinTable(
			name = "payments_countries",
			joinColumns = @JoinColumn(name = "payment_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "country_id", referencedColumnName = "id")
	)
	private List<CountryPresence> countries = new ArrayList<>();
}
