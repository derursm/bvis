package org.camunda.bpm.bvis.Entites;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Insurance implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected long insuranceID;
	
	protected String type;
	protected double deductible;
	protected Date pickUpDate;
	protected Date returnDate;
	protected double estimatedCost;
	
	public long getInsuranceID() {
		return insuranceID;
	}
	public void setInsuranceID(long insuranceID) {
		this.insuranceID = insuranceID;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getDeductible() {
		return deductible;
	}
	public void setDeductible(double deductible) {
		this.deductible = deductible;
	}
	public Date getPickUpDate() {
		return pickUpDate;
	}
	public void setPickUpDate(Date pickUpDate) {
		this.pickUpDate = pickUpDate;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public double getEstimatedCost() {
		return estimatedCost;
	}
	public void setEstimatedCost(double estimatedCost) {
		this.estimatedCost = estimatedCost;
	}
}
