package org.camunda.bpm.bvis.rest.send.dto;

import java.util.Calendar;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Claim {
	private int claim_id;
	private int insurance_id;
	private String vehicle_identification_number;
	private int order_id;
	private Calendar damage_date;
	private String damage_address;
	private String claim_description;
	private double workshop_price;
	@XmlElement(name = "involved_parties")
	private Collection<Involved_party> involvedParties;

	public int getClaim_id() {
		return claim_id;
	}

	public void setClaim_id(int claim_id) {
		this.claim_id = claim_id;
	}

	public int getInsurance_id() {
		return insurance_id;
	}

	public void setInsurance_id(int insurance_id) {
		this.insurance_id = insurance_id;
	}

	public String getVehicle_identification_number() {
		return vehicle_identification_number;
	}

	public void setVehicle_identification_number(String vehicle_identification_number) {
		this.vehicle_identification_number = vehicle_identification_number;
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public Calendar getDamage_date() {
		return damage_date;
	}

	public void setDamage_date(Calendar damage_date) {
		this.damage_date = damage_date;
	}

	public String getDamage_address() {
		return damage_address;
	}

	public void setDamage_address(String damage_address) {
		this.damage_address = damage_address;
	}

	public String getClaim_description() {
		return claim_description;
	}

	public void setClaim_description(String claim_description) {
		this.claim_description = claim_description;
	}

	public double getWorkshop_price() {
		return workshop_price;
	}

	public void setWorkshop_price(double workshop_price) {
		this.workshop_price = workshop_price;
	}

	public Collection<Involved_party> getInvolvedParties() {
		return involvedParties;
	}

	public void setInvolvedParties(Collection<Involved_party> involvedParties) {
		this.involvedParties = involvedParties;
	}
}
