package org.camunda.bpm.bvis.bvis.contractingComplex;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Store implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected Long store_ID;
	
	protected Customer cust;
	
	protected Integer pick_up_location;
	protected Integer return_location;
	protected Integer insurance_type;
	protected String inquiry_text;
	protected boolean fleet_rental;
	protected String cars[];
	protected long insurance_ID;
	
	public Long getId() {
		return store_ID;
	}
	
	public void setId(Long id) {
		this.store_ID = id;
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
	
	public long getInsurance_ID() {
		return insurance_ID;
	}
	
	public void setInsurance_ID(long insurance_ID) {
		this.insurance_ID = insurance_ID;
	}
	
}
