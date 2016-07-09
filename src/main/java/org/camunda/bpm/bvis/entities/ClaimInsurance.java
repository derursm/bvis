package org.camunda.bpm.bvis.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * Class for other insurances (insurances of involved parties)
 * To be differentiated from the Insurance class which represents an insurance for one order
 * This insurance represents actual insurance companies, not insurance policies
 */
@Entity
public class ClaimInsurance implements Serializable {

	private static final long serialVersionUID = 1L;
	@NotNull
	private String company;
	@NotNull
	private String street;
	@NotNull
	private String houseNumber;
	@NotNull
	private String postcode;
	@NotNull
	private String city;
	@NotNull
	private String country;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long claimInsuranceID;

	public ClaimInsurance(){
		super();
	}
	
	public ClaimInsurance(String company, String street, String houseNumber, String postcode, String city, String country){
		this.company = company;
		this.street = street;
		this.houseNumber = houseNumber;
		this.postcode = postcode;
		this.city = city;
		this.country = country;
	}
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getHouse_number() {
		return houseNumber;
	}
	public void setHouse_number(String house_number) {
		this.houseNumber = house_number;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}
