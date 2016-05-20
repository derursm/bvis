package org.camunda.bpm.bvis.bvis.contractingComplex;

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
	
	protected Date pick_up_date;
	protected Date return_date;
	protected Store pick_up_store;
	protected Store return_store;
	protected Integer insurance_type;
	protected String inquiry_text;
	protected boolean fleet_rental;
	protected Car[] cars;
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
	
	public Store getPick_up_store() {
		return pick_up_store;
	}

	public void setPick_up_store(Store pick_up_store) {
		this.pick_up_store = pick_up_store;
	}

	public Store getReturn_store() {
		return return_store;
	}

	public void setReturn_store(Store return_store) {
		this.return_store = return_store;
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
	
	public Car[] getCars() {
		return cars;
	}
	
	public void setCars(Car[] cars) {
		this.cars = cars;
	}
	
	public long getInsurance_ID() {
		return insurance_ID;
	}
	
	public void setInsurance_ID(long insurance_ID) {
		this.insurance_ID = insurance_ID;
	}
	
}
