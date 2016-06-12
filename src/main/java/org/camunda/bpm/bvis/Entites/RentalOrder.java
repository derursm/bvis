package org.camunda.bpm.bvis.Entites;

import javax.persistence.Entity;
import static javax.persistence.CascadeType.*;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	@OneToOne
	protected Customer cust;
//	@Temporal(TemporalType.DATE)
//	protected Date pickUpDate;
//	@Temporal(TemporalType.DATE)
//	protected Date returnDate;
	@ManyToOne
	protected PickUpLocation pickUpStore;
	@ManyToOne
	protected PickUpLocation returnStore;
	protected InsuranceType insuranceType;
	protected String inquiryText;
	@NotNull
	protected boolean fleetRental;
	@ManyToMany(cascade = {DETACH,MERGE,PERSIST,REFRESH}, mappedBy = "rentalOrders")
	protected Collection<Car> cars;
	protected long insuranceID;
	
	protected int contractStatus;
	
	public int getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(int contractStatus) {
		this.contractStatus = contractStatus;
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
	
//	public Date getPick_up_date() {
//		return pickUpDate;
//	}
//	
//	public void setPick_up_date(Date pick_up_date) {
//		this.pickUpDate = pick_up_date;
//	}
//	
//	public Date getReturn_date() {
//		return returnDate;
//	}
//	
//	public void setReturn_date(Date return_date) {
//		this.returnDate = return_date;
//	}
	
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

	public InsuranceType getInsurance_type() {
		return insuranceType;
	}
	
	public void setInsurance_type(InsuranceType insurance_type) {
		this.insuranceType = insurance_type;
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

	public InsuranceType getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(InsuranceType insuranceType) {
		this.insuranceType = insuranceType;
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

	public long getInsuranceID() {
		return insuranceID;
	}

	public void setInsuranceID(long insuranceID) {
		this.insuranceID = insuranceID;
	}

	public Collection<Car> getCars() {
		return cars;
	}
	
	public void setCars(Collection<Car> cars) {
		this.cars = cars;
	}
	
	public long getInsurance_ID() {
		return insuranceID;
	}
	
	public void setInsurance_ID(long insurance_ID) {
		this.insuranceID = insurance_ID;
	}
	
}