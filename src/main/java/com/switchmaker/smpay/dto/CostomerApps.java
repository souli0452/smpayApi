package com.switchmaker.smpay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.switchmaker.smpay.entities.Platform;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CostomerApps {
	private UUID id;

	private String denomination;
	private String address;
	private String country;
	private String town;
	private String email;
	private String phone;
	@JsonProperty("creation_date")
	private LocalDateTime creationDate;
	@JsonProperty("payment_account")
	private String paymentAccount;
	@JsonProperty("account_type")
	private String accountType;
	@JsonProperty("orange_rate")
	private float orangeRate = 0;
	@JsonProperty("moov_rate")
	private float moovRate = 0;
	@JsonProperty("coris_rate")
	private float corisRate = 0;
	@JsonProperty("mtn_rate")
	private float mtnRate = 0;
	@JsonProperty("wave_rate")
	private float waveRate = 0;
	private List<Platform> platforms=new ArrayList<>();
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getDenomination() {
		return denomination;
	}
	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPaymentAccount() {
		return paymentAccount;
	}
	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public float getOrangeRate() {
		return orangeRate;
	}
	public void setOrangeRate(float orangeRate) {
		this.orangeRate = orangeRate;
	}
	public float getMoovRate() {
		return moovRate;
	}
	public void setMoovRate(float moovRate) {
		this.moovRate = moovRate;
	}
	public float getCorisRate() {
		return corisRate;
	}
	public void setCorisRate(float corisRate) {
		this.corisRate = corisRate;
	}
	public float getMtnRate() {
		return mtnRate;
	}
	public void setMtnRate(float mtnRate) {
		this.mtnRate = mtnRate;
	}
	public float getWaveRate() {
		return waveRate;
	}
	public void setWaveRate(float waveRate) {
		this.waveRate = waveRate;
	}
	public List<Platform> getPlatforms() {
		return platforms;
	}
	public void setPlatforms(List<Platform> platforms) {
		this.platforms = platforms;
	}
}
