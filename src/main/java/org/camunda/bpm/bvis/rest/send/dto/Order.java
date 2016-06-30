package org.camunda.bpm.bvis.rest.send.dto;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
	private int order_id;
	private String request_date;
	private boolean fleet_rental;
	private String inquiry_text;
	private User user;
	private Collection<Car> car;
	private Insurance insurance;
	
	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public String getRequest_date() {
		return request_date;
	}

	public void setRequest_date(String request_date) {
		this.request_date = request_date;
	}

	public boolean isFleet_rental() {
		return fleet_rental;
	}

	public void setFleet_rental(boolean fleet_rental) {
		this.fleet_rental = fleet_rental;
	}

	public String getInquiry_text() {
		return inquiry_text;
	}

	public void setInquiry_text(String inquiry_text) {
		this.inquiry_text = inquiry_text;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Collection<Car> getCar() {
		return car;
	}

	public void setCar(Collection<Car> car) {
		this.car = car;
	}

	public Insurance getInsurance() {
		return insurance;
	}

	public void setInsurance(Insurance insurance) {
		this.insurance = insurance;
	}
}