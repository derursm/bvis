package org.camunda.bpm.bvis.Entites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Order implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected Long order_ID;
	
	protected Customer cust;
	
	protected Date pickUpDate;
	protected Date returnDate;
	protected PickUpLocation pickUpStore;
	protected PickUpLocation returnStore;
	protected Integer insuranceType;
	protected String inquiryText;
	protected boolean fleetRental;
	protected Car[] cars;
	protected long insuranceID;
	
	public Long getId() {
		return order_ID;
	}
	
	public void setId(Long id) {
		this.order_ID = id;
	}
	
	public Date getPick_up_date() {
		return pickUpDate;
	}
	
	public void setPick_up_date(Date pick_up_date) {
		this.pickUpDate = pick_up_date;
	}
	
	public Date getReturn_date() {
		return returnDate;
	}
	
	public void setReturn_date(Date return_date) {
		this.returnDate = return_date;
	}
	
	public PickUpLocation getPick_up_store() {
		return pickUpStore;
	}

	public void setPick_up_store(PickUpLocation pick_up_store) {
		this.pickUpStore = pick_up_store;
	}

	public PickUpLocation getReturn_store() {
		return returnStore;
	}

	public void setReturn_store(PickUpLocation return_store) {
		this.returnStore = return_store;
	}

	public Integer getInsurance_type() {
		return insuranceType;
	}
	
	public void setInsurance_type(Integer insurance_type) {
		this.insuranceType = insurance_type;
	}
	
	public String getInquiry_text() {
		return inquiryText;
	}
	
	public void setInquiry_text(String inquiry_text) {
		this.inquiryText = inquiry_text;
	}
	
	public boolean isFleet_rental() {
		return fleetRental;
	}
	
	public void setFleet_rental(boolean fleet_rental) {
		this.fleetRental = fleet_rental;
	}
	
	public Car[] getCars() {
		return cars;
	}
	
	public void setCars(Car[] cars) {
		this.cars = cars;
	}
	
	public long getInsurance_ID() {
		return insuranceID;
	}
	
	public void setInsurance_ID(long insurance_ID) {
		this.insuranceID = insurance_ID;
	}
	
}
