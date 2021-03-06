package org.camunda.bpm.bvis.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
public class PickUpLocation implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long storeID;
	@NotNull
	protected String storeName;
	@NotNull
	protected String phoneNumber;
	@NotNull	
	protected String street;
	
	protected String houseNumber;
	@NotNull
	protected String postcode;
	@NotNull
	protected String city;
	@NotNull
	protected String country;
	
	public PickUpLocation(){}
	
	public PickUpLocation(String storeName, String phoneNumber, String street, String houseNumber, String postcode, String city, String country){
		this.storeName = storeName;
		this.phoneNumber = phoneNumber;
		this.street = street;
		this.houseNumber = houseNumber;
		this.postcode = postcode;
		this.city = city;
		this.country = country;
	}
	
	public String getHTMLContactDetails(){
		String contactDetails = this.getStoreName() + "<br>" + this.getStreet() + " " + this.getHouseNumber() + "<br>" + this.getPostcode() + " " + this.getCity() + "<br>" + this.getPhoneNumber();
		return contactDetails;
	}
	
	public String getContactDetails(){
		String contactDetails = this.getStoreName() + ", " + this.getStreet() + " " + this.getHouseNumber() + ", " + this.getPostcode() + " " + this.getCity() + ", " + this.getPhoneNumber();
		return contactDetails;
	}
	
	public Long getStoreID() {
		return storeID;
	}
	public void setStoreID(Long storeID) {
		this.storeID = storeID;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}