package org.camunda.bpm.bvis.Persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Customer implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected Long customer_ID;
	
	protected String firstname;
	protected String surname;
	protected String email;
	protected String phone_number;
	protected String street;
	protected String house_number;
	protected Integer postcode;
	protected String city;
	protected String country;
	protected Date date_of_birth;
	protected String company;
	
	public Long getId() {
		return customer_ID;
	}
	
	public void setId(Long id) {
		this.customer_ID = id;
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
	
	public Integer getPostcode() {
		return postcode;
	}
	
	public void setPostcode(Integer postcode) {
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
	
}
