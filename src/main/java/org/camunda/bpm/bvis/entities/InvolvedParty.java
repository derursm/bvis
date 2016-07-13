package org.camunda.bpm.bvis.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
public class InvolvedParty implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long party_ID;
	@NotNull
	protected String firstname;
	@NotNull
	protected String surname;
	@NotNull
	protected String email;
	@NotNull
	protected String phone_number;
	@NotNull
	protected String street;
	@NotNull
	protected String house_number;
	@NotNull
	protected String postcode;
	@NotNull
	protected String city;
	@NotNull
	protected String country;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	protected Date date_of_birth;

	protected String company;
	
	@ManyToOne
	protected ClaimInsurance claimInsurance;
	
	@ManyToOne
	protected Claim claim;
	
	public Long getParty_ID() {
		return party_ID;
	}
	public void setParty_ID(Long party_ID) {
		this.party_ID = party_ID;
	}

	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
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
	public Date getDate_of_birth() {
		return date_of_birth;
	}
	public void setDate_of_birth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}

	public Claim getClaim() {
		return claim;
	}
	public void setClaim(Claim claim) {
		this.claim = claim;
	}
	public ClaimInsurance getClaimInsurance() {
		return claimInsurance;
	}
	public void setClaimInsurance(ClaimInsurance claimInsurance) {
		this.claimInsurance = claimInsurance;
	}
	
	
	
}
