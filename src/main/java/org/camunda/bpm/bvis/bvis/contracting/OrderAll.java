package org.camunda.bpm.bvis.bvis.contracting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class OrderAll implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected Long order_ID;
	
	protected Date pick_up_date;
	protected Date return_date;
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
	protected Integer pick_up_location;
	protected Integer return_location;
	protected Integer insurance_type;
	protected String inquiry_text;
	protected boolean fleet_rental;
	protected String cars[];
	protected String company;
	protected long insurance_ID;
	
	public Long getId() {
		return order_ID;
	}
	
	public void setId(Long id) {
		this.order_ID = id;
	}
	
	public Date getPick_up_date() {
		return pick_up_date;
	}
	
	public void setPick_up_date(Date pick_up_date) {
		this.pick_up_date = pick_up_date;
	}
	
	public Date getReturn_date() {
		return return_date;
	}
	
	public void setReturn_date(Date return_date) {
		this.return_date = return_date;
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
	
	public Integer getPick_up_location() {
		return pick_up_location;
	}
	
	public void setPick_up_location(Integer pick_up_location) {
		this.pick_up_location = pick_up_location;
	}
	
	public Integer getReturn_location() {
		return return_location;
	}
	
	public void setReturn_location(Integer return_location) {
		this.return_location = return_location;
	}
	
	public Integer getInsurance_type() {
		return insurance_type;
	}
	
	public void setInsurance_type(Integer insurance_type) {
		this.insurance_type = insurance_type;
	}
	
	public String getInquiry_text() {
		return inquiry_text;
	}
	
	public void setInquiry_text(String inquiry_text) {
		this.inquiry_text = inquiry_text;
	}
	
	public boolean isFleet_rental() {
		return fleet_rental;
	}
	
	public void setFleet_rental(boolean fleet_rental) {
		this.fleet_rental = fleet_rental;
	}
	
	public String[] getCars() {
		return cars;
	}
	
	public void setCars(String[] cars) {
		this.cars = cars;
	}
	
	public String getCompany() {
		return company;
	}
	
	public void setCompany(String company) {
		this.company = company;
	}
	
	public long getInsurance_ID() {
		return insurance_ID;
	}
	
	public void setInsurance_ID(long insurance_ID) {
		this.insurance_ID = insurance_ID;
	}
	
}
