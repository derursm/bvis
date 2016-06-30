package org.camunda.bpm.bvis.rest.send.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Car {
	private String registration_number;
	private String brand;
	private String type;
	private String model;
	private String vehicle_identification_number;
	private String fuel_type;
	private int ps;
	private int construction_year;
	
	public Car(){
		super();
	}
	
	public String getRegistration_number() {
		return registration_number;
	}
	public void setRegistration_number(String registration_number) {
		this.registration_number = registration_number;
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
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getVehicle_identification_number() {
		return vehicle_identification_number;
	}
	public void setVehicle_identification_number(String vehicle_identification_number) {
		this.vehicle_identification_number = vehicle_identification_number;
	}
	public String getFuel_type() {
		return fuel_type;
	}
	public void setFuel_type(String fuel_type) {
		this.fuel_type = fuel_type;
	}
	public int getPs() {
		return ps;
	}
	public void setPs(int ps) {
		this.ps = ps;
	}
	public int getConstruction_year() {
		return construction_year;
	}
	public void setConstruction_year(int construction_year) {
		this.construction_year = construction_year;
	}
}