package org.camunda.bpm.bvis.rest.send.dto;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


@XmlAccessorType(XmlAccessType.FIELD)
public class Involved_party {
	private String firstname;
	private String surname;
	private String email;
	private String phone_number;
	private String street;
	private String house_number;
	private String postcode;
	private String city;
	private String country;
	private Calendar date_of_birth;
	private String company;
	private boolean has_insurance;
	private ClaimInsurance insurance;

	public Involved_party(){
		super();
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

	public Calendar getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(Calendar date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public boolean isHas_insurance() {
		return has_insurance;
	}

	public void setHas_insurance(boolean has_insurance) {
		this.has_insurance = has_insurance;
	}

	public ClaimInsurance getInsurance() {
		return insurance;
	}

	public void setInsurance(ClaimInsurance insurance) {
		this.insurance = insurance;
	}
}