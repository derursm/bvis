package org.camunda.bpm.bvis.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;

import static javax.persistence.CascadeType.*;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
public class RentalOrder implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long orderID;
	@NotNull
	@ManyToOne(cascade = {DETACH,MERGE,PERSIST,REFRESH}, fetch = FetchType.EAGER)
	protected Customer cust;
	@Temporal(TemporalType.DATE)
	protected Date pickUpDate;
	@Temporal(TemporalType.DATE)
	protected Date returnDate;
	@ManyToOne
	protected PickUpLocation pickUpStore;
	@ManyToOne
	protected PickUpLocation returnStore;
	protected String inquiryText;
	@NotNull
	protected boolean fleetRental;
	@ManyToMany(fetch = FetchType.EAGER)
	protected Collection<Car> cars;
	
	@OneToOne(cascade = {DETACH,MERGE,PERSIST,REFRESH}, fetch = FetchType.EAGER)
	protected Insurance insurance;
	
	@Temporal(TemporalType.DATE)
	protected Date requestDate;
	
	protected double priceCars;
	protected double price;	

	protected String clerkComments;
	protected boolean approveStatus;
	
	protected OrderStatus orderStatus;
	
	public RentalOrder(){}
	
	public RentalOrder(Customer cust, boolean fleetRental){
		this.cust = cust;
		this.fleetRental = fleetRental;
	}
	
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Insurance getInsurance() {
		return insurance;
	}

	public void setInsurance(Insurance insurance) {
		this.insurance = insurance;
	}
	
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getId() {
		return orderID;
	}
	
	public void setId(Long id) {
		this.orderID = id;
	}
	
	public Customer getCustomer() {
		return cust;
	}
	
	public void setCustomer(Customer customer) {
		this.cust = customer;
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
	
	public Long getOrderID() {
		return orderID;
	}

	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}

	public PickUpLocation getPickUpStore() {
		return pickUpStore;
	}

	public void setPickUpStore(PickUpLocation pickUpStore) {
		this.pickUpStore = pickUpStore;
	}

	public PickUpLocation getReturnStore() {
		return returnStore;
	}

	public void setReturnStore(PickUpLocation returnStore) {
		this.returnStore = returnStore;
	}

	public String getInquiryText() {
		return inquiryText;
	}

	public void setInquiryText(String inquiryText) {
		this.inquiryText = inquiryText;
	}

	public boolean isFleetRental() {
		return fleetRental;
	}

	public void setFleetRental(boolean fleetRental) {
		this.fleetRental = fleetRental;
	}

	public Collection<Car> getCars() {
		return cars;
	}
	
	public void setCars(Collection<Car> cars) {
		this.cars = cars;
	}
	
	public String getClerkComments() {
		return clerkComments;
	}
	
	public void setClerkComments(String clerkComments) {
		this.clerkComments = clerkComments;
	}
	
	public boolean getApproveStatus() {
		return approveStatus;
	}
	
	public void setApproveStatus(boolean approveStatus) {
		this.approveStatus = approveStatus;
	}
	
	public void setPriceCars(double priceCars) {
		this.priceCars = priceCars;
	}
	public double getPriceCars() {
		return this.priceCars;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPrice() {
		return this.price;
	}
	
	public void addCar(Car car) {
		cars.add(car);
	}
	
	public void removeCar(Car car) {
		cars.remove(car);
	}
}
