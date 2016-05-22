package org.camunda.bpm.bvis.Entites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.io.Serializable;

@Entity
public class Car implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected Long vehicleIdentificationNumber;
	
	protected String brand;
	protected Integer constructionYear;
	protected String fuelType;
	protected String model;
	protected Integer ps;
	protected String registrationNumber;
	protected String type;
	protected boolean rented;
	protected PickUpLocation CurrentLocation;
	
	public Long getVehicleIdentificationNumber() {
		return vehicleIdentificationNumber;
	}
	public void setVehicleIdentificationNumber(Long vehicleIdentificationNumber) {
		this.vehicleIdentificationNumber = vehicleIdentificationNumber;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public Integer getConstructionYear() {
		return constructionYear;
	}
	public void setConstructionYear(Integer constructionYear) {
		this.constructionYear = constructionYear;
	}
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getPs() {
		return ps;
	}
	public void setPs(Integer ps) {
		this.ps = ps;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
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
	public PickUpLocation getCurrentLocation() {
		return CurrentLocation;
	}
	public void setCurrentLocation(PickUpLocation currentLocation) {
		CurrentLocation = currentLocation;
	}
	
}
