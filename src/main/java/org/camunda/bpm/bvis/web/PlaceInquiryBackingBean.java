package org.camunda.bpm.bvis.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.camunda.bpm.bvis.web.util.WebUrls;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Named
@RequestScoped
@ManagedBean(name = "placeInquiry")
public class PlaceInquiryBackingBean {
	
	private String firstname;
	private String lastname;
	private String companyName; 
	private String email;
	private String phoneNumber;
	private String street;
	private String houseNumber;
	private String postcode;
	private String city;
	private String country;
	private Date dateOfBirth;
	private boolean fleetRental;
	private String car;
	private String comment;
	private Date pickupDate;
	private Date returnDate;
	private String pickupLocation;
	private String returnLocation;
	private String insuranceType;
	
	
	
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public boolean isFleetRental() {
		return fleetRental;
	}
	public void setFleetRental(boolean fleetRental) {
		this.fleetRental = fleetRental;
	}
	public String getCar() {
		return car;
	}
	public void setCar(String car) {
		this.car = car;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getPickupDate() {
		return pickupDate;
	}
	public Date getReturnDate() {
		return returnDate;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}
	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}
	public String getReturnLocation() {
		return returnLocation;
	}
	public void setReturnLocation(String returnLocation) {
		this.returnLocation = returnLocation;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	
	public void placeInquiry() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("customerFirstname", firstname);
		variables.put("customerSurname", lastname);
		variables.put("customerCompanyName", companyName);
		variables.put("customerEmail", email);
		variables.put("customerPhoneNumber", phoneNumber);
		variables.put("customerDateOfBirth", dateOfBirth);
		variables.put("customerStreet", street);
		variables.put("customerHouseNumber", houseNumber);
		variables.put("customerPostcode", postcode);
		variables.put("customerCity", city);
		variables.put("customerCountry", country);
		variables.put("fleet", fleetRental);
		variables.put("pickUpDate", pickupDate);
		variables.put("returnDate", returnDate);
		variables.put("pickUpLoc", pickupLocation);
		variables.put("returnStore", returnLocation);
		variables.put("insuranceType", insuranceType);
		variables.put("car", car);
		variables.put("inquiryText", comment);
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("contracting", variables);
		System.out.println(WebUrls.getUrl(WebUrls.ORDER_SUBMITTED, false, false));
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(WebUrls.getUrl(WebUrls.ORDER_SUBMITTED, false, false));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}