package com.switchmaker.smpay.dto;

import com.switchmaker.smpay.entities.PaymentRequest;

import java.util.List;

public class Versement {
	private List<PaymentRequest> newDemandes;
	private List<PaymentRequest> demandes;
	public List<PaymentRequest> getNewDemandes() {
		return newDemandes;
	}
	public void setNewDemandes(List<PaymentRequest> newDemandes) {
		this.newDemandes = newDemandes;
	}
	public List<PaymentRequest> getDemandes() {
		return demandes;
	}
	public void setDemandes(List<PaymentRequest> demandes) {
		this.demandes = demandes;
	}
}
