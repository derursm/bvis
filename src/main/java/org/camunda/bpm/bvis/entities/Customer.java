package org.camunda.bpm.bvis.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
public class Customer implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long customerID;
	protected String firstname;
	@NotNull
	protected String surname;
	protected String email;
	@NotNull
	protected String phoneNumber;
	@NotNull	
	protected String street;
	@NotNull
	protected String houseNumber;
	@NotNull
	protected String postcode;
	@NotNull
	protected String city;
	@NotNull
	protected String country;
	@Temporal(TemporalType.DATE)
	@Past
	protected Date dateOfBirth;
	protected boolean company;
	protected String companyName;
	
	protected boolean isEligible;
	
	@OneToMany(cascade = {DETACH,MERGE,PERSIST,REFRESH}, fetch = FetchType.EAGER, mappedBy = "cust")
	protected Collection<RentalOrder> orders;
	
	public Customer (){}
	
	public Customer (String surname, String phoneNumber, String street, String houseNumber, String postcode, String city, String country){
		this.surname = surname;
		this.phoneNumber = phoneNumber;
		this.street = street;
		this.houseNumber = houseNumber;
		this.postcode = postcode;
		this.city = city;
		this.country = country;
	}
	
	public boolean isEligible() {
		return isEligible;
	}
	public void setEligibility(boolean isEligible) {
		this.isEligible = isEligible;
	}
	public Long getCustomerID() {
		return customerID;
	}
	public void setCustomerID(Long customerID) {
		this.customerID = customerID;
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

	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
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
	
	public boolean isCompany() {
		return company;
	}
	public void setCompany(boolean company) {
		this.company = company;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public boolean getCompany() {
		return company;
	}
	
}
