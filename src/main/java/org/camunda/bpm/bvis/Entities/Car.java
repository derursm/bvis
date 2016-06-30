package org.camunda.bpm.bvis.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;

import java.io.Serializable;
import java.util.Collection;

@Entity
public class Car implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;	
	@NotNull
	protected String brand;
	@NotNull
	@Min(value=1900)
	protected Integer constructionYear;
	@NotNull
	protected String fuelType;
	@NotNull
	protected String model;
	@NotNull
	@Min(value=0)
	protected Integer ps;
	@NotNull
	protected String registrationNumber;
	@NotNull
	protected String vehicleIdentificationNumber;
	@NotNull
	protected CarType type;
	@NotNull
	protected boolean returned;
	protected PickUpLocation currentLocation;

	@ManyToMany(cascade = {DETACH,MERGE,PERSIST,REFRESH}, mappedBy = "cars")
	protected Collection<RentalOrder> rentalOrders;
	
	public String getHTMLCarDetails(){
		String carDetails = this.getModel() + " " +  this.getBrand() + ", " + this.getFuelType() + ", PS: " + this.getPs();
		return carDetails;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBrand() {
		return brand;
	}
	public CarType getType() {
		return type;
	}
	public void setType(CarType type) {
		this.type = type;
	}
	public void setBrand(String brand) {
		this.brand = brand;
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
	
	public boolean isReturned() {
		return returned;
	}
	public void setRented(boolean returned) {
		this.returned = returned;
	}
	public PickUpLocation getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(PickUpLocation currentLocation) {
		this.currentLocation = currentLocation;
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
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getVehicleIdentificationNumber() {
		return vehicleIdentificationNumber;
	}
	public void setVehicleIdentificationNumber(String vehicleIdentificationNumber) {
		this.vehicleIdentificationNumber = vehicleIdentificationNumber;
	}
	public Collection<RentalOrder> getRentalOrders() {
		return rentalOrders;
	}
	public void setRentalOrders(Collection<RentalOrder> rentalOrders) {
		this.rentalOrders = rentalOrders;
	}
	
	
	
}
