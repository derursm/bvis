package org.camunda.bpm.bvis.Entites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

@Entity
public class Claim implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long claimID;
	@NotNull
	protected Long insuranceID;	
	@Temporal(TemporalType.DATE)
	@NotNull
	protected Date damageDate;
	@Min(value=0)
	protected BigDecimal workshopPrice;
	@NotNull
	protected String claimDescription;
	@Min(value=0)
	protected BigDecimal costsCoverage;
	@NotNull
	@ManyToMany(cascade = {DETACH,MERGE,PERSIST,REFRESH}, mappedBy = "claims")
	protected Collection<InvolvedParty> involvedParties;
	@NotNull
	protected RentalOrder orderID;	
	@NotNull
	protected Car carID;
	protected String damageAddress;
	protected String clerkNotice;
	@NotNull
	protected boolean reportedByCustomer;
	@NotNull
	protected boolean towingServiceNeeded;
	
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
	public BigDecimal getWorkshopPrice() {
		return workshopPrice;
	}
	public void setWorkshopPrice(BigDecimal workshopPrice) {
		this.workshopPrice = workshopPrice;
	}
	public String getClaimDescription() {
		return claimDescription;
	}
	public void setClaimDescription(String claimDescription) {
		this.claimDescription = claimDescription;
	}
	public BigDecimal getCostsCoverage() {
		return costsCoverage;
	}
	public void setCostsCoverage(BigDecimal costsCoverage) {
		this.costsCoverage = costsCoverage;
	}
	public Collection<InvolvedParty> getInvolvedParties() {
		return involvedParties;
	}
	public void setInvolvedParty(Collection<InvolvedParty> involvedParties) {
		this.involvedParties = involvedParties;
	}
	public RentalOrder getOrderID() {
		return orderID;
	}
	public void setOrderID(RentalOrder orderID) {
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

	public String getClerkNotice() {
		return clerkNotice;
	}
	public void setClerkNotice(String clerkNotice) {
		this.clerkNotice = clerkNotice;
	}
	
	
	
}
