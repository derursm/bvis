package org.camunda.bpm.bvis.bvis.contractingComplex;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Car implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected Long car_ID;
	
	protected String brand;
	protected String type;
	protected boolean rented;
	protected Store StoreName;
	public Long getCar_ID() {
		return car_ID;
	}
	public void setCar_ID(Long car_ID) {
		this.car_ID = car_ID;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isRented() {
		return rented;
	}
	public void setRented(boolean rented) {
		this.rented = rented;
	}
	public Store getStoreName() {
		return StoreName;
	}
	public void setStoreName(Store storeName) {
		StoreName = storeName;
	}
	
}
