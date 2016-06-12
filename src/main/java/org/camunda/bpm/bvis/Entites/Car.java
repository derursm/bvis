package org.camunda.bpm.bvis.Entites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

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
	protected String vehicle_identification_number;
	public String getVehicle_identification_number() {
		return vehicle_identification_number;
	}
	public void setVehicle_identification_number(String vehicle_identification_number) {
		this.vehicle_identification_number = vehicle_identification_number;
	}
	@NotNull
	protected CarType type;
	@NotNull
	protected boolean returned;
	protected PickUpLocation currentLocation;

	@ManyToMany(cascade = {DETACH,MERGE,PERSIST,REFRESH})
	protected Collection<RentalOrder> rentalOrders;
	
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
		currentLocation = currentLocation;
	}
	
}
