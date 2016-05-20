package org.camunda.bpm.bvis.Persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Claim implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected Long claimID;
	
	protected Long insuranceID;
	protected Date damageDate;
	protected double workshopPrice;
	protected String claimDescription;
	protected double costsCoverage;
	protected InvolvedParty involvedParty;
	protected Order orderID;
	protected Car carID;
	protected String damageAddress;
	protected double amountOfCoverage;
	protected String clerkNotice;
	public Long getClaimID() {
		return claimID;
	}
	public void setClaimID(Long claimID) {
		this.claimID = claimID;
	}
	public Long getInsuranceID() {
		return insuranceID;
	}
	public void setInsuranceID(Long insuranceID) {
		this.insuranceID = insuranceID;
	}
	public Date getDamageDate() {
		return damageDate;
	}
	public void setDamageDate(Date damageDate) {
		this.damageDate = damageDate;
	}
	public double getWorkshopPrice() {
		return workshopPrice;
	}
	public void setWorkshopPrice(double workshopPrice) {
		this.workshopPrice = workshopPrice;
	}
	public String getClaimDescription() {
		return claimDescription;
	}
	public void setClaimDescription(String claimDescription) {
		this.claimDescription = claimDescription;
	}
	public double getCostsCoverage() {
		return costsCoverage;
	}
	public void setCostsCoverage(double costsCoverage) {
		this.costsCoverage = costsCoverage;
	}
	public InvolvedParty getInvolvedParty() {
		return involvedParty;
	}
	public void setInvolvedParty(InvolvedParty involvedParty) {
		this.involvedParty = involvedParty;
	}
	public Order getOrderID() {
		return orderID;
	}
	public void setOrderID(Order orderID) {
		this.orderID = orderID;
	}
	public Car getCarID() {
		return carID;
	}
	public void setCarID(Car carID) {
		this.carID = carID;
	}
	public String getDamageAddress() {
		return damageAddress;
	}
	public void setDamageAddress(String damageAddress) {
		this.damageAddress = damageAddress;
	}
	public double getAmountOfCoverage() {
		return amountOfCoverage;
	}
	public void setAmountOfCoverage(double amountOfCoverage) {
		this.amountOfCoverage = amountOfCoverage;
	}
	public String getClerkNotice() {
		return clerkNotice;
	}
	public void setClerkNotice(String clerkNotice) {
		this.clerkNotice = clerkNotice;
	}
	
	
	
}
