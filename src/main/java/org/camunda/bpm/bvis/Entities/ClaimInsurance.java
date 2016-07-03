package org.camunda.bpm.bvis.Entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class ClaimInsurance implements Serializable {

	private static final long serialVersionUID = 1L;
	@NotNull
	private String company;
	@NotNull
	private String street;
	@NotNull
	private String house_number;
	@NotNull
	private String postcode;
	@NotNull
	private String city;
	@NotNull
	private String country;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long claimInsuranceID;
	
	@OneToOne(mappedBy = "insurance")
	protected Claim claim;

	public ClaimInsurance(){
		super();
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
		return house_number;
	}
	public void setHouse_number(String house_number) {
		this.house_number = house_number;
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
