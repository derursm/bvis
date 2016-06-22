package org.camunda.bpm.bvis.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;

import java.io.Serializable;
import java.util.Collection;

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
	protected String date_of_birth;

	protected String company;
	
	protected String insurance_company_name;
	protected String insurance_company_street;
	protected String insurance_company_house_number;
	protected String insurance_company_postcode;
	protected String insurance_company_city;
	protected String insurance_company_country;
	
	@ManyToMany(cascade = {DETACH,MERGE,PERSIST,REFRESH})
	protected Collection<Claim> claims;
	
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
	public String getDate_of_birth() {
		return date_of_birth;
	}
	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getInsurance_company_street() {
		return insurance_company_street;
	}
	public void setInsurance_company_street(String insurance_company_street) {
		this.insurance_company_street = insurance_company_street;
	}
	public String getInsurance_company_house_number() {
		return insurance_company_house_number;
	}
	public void setInsurance_company_house_number(String insurance_company_house_number) {
		this.insurance_company_house_number = insurance_company_house_number;
	}
	public String getInsurance_company_postcode() {
		return insurance_company_postcode;
	}
	public void setInsurance_company_postcode(String insurance_company_postcode) {
		this.insurance_company_postcode = insurance_company_postcode;
	}
	public String getInsurance_company_city() {
		return insurance_company_city;
	}
	public void setInsurance_company_city(String insurance_company_city) {
		this.insurance_company_city = insurance_company_city;
	}
	public String getInsurance_company_country() {
		return insurance_company_country;
	}
	public void setInsurance_company_country(String insurance_company_country) {
		this.insurance_company_country = insurance_company_country;
	}
	public Collection<Claim> getClaims() {
		return claims;
	}
	public void setClaims(Collection<Claim> claims) {
		this.claims = claims;
	}
	public String getInsurance_company_name() {
		return insurance_company_name;
	}
	public void setInsurance_company_name(String insurance_company_name) {
		this.insurance_company_name = insurance_company_name;
	}
	
	
	
}
