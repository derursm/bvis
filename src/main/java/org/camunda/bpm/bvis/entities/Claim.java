package org.camunda.bpm.bvis.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.camunda.bpm.bvis.entities.RentalOrder;

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
	@Temporal(TemporalType.DATE)
	@NotNull
	protected Date damageDate;
	@Min(value=0)
	protected BigDecimal workshopPrice;
	@Min(value=0)
	protected BigDecimal customerCosts;
	
	@OneToMany(cascade = {DETACH,MERGE,PERSIST,REFRESH}, mappedBy = "claim")
	protected Collection<ClaimReview> claimReviews;

	@NotNull
	protected String claimDescription;
	@Min(value=0)
	protected BigDecimal costsCoverage;
	protected String damageAddress;
	protected String clerkNotice;
	@NotNull
	protected boolean reportedByCustomer;
	@NotNull
	protected boolean towingServiceNeeded;
	protected int insurance_decision;
	protected ClaimStatus claim_status;
	protected String claim_response_description_from_Capitol;
	protected String claim_remarks_from_Bvis;
	

	@NotNull
	@OneToOne
	protected Insurance insurance;	
	@NotNull
	@OneToMany(cascade = {DETACH,MERGE,PERSIST,REFRESH}, mappedBy = "claim")
	protected Collection<InvolvedParty> involvedParties;
	@NotNull
	@OneToOne
	protected Car car;
	@NotNull
	@OneToOne
	protected RentalOrder rentalOrder;
	
	public int getInsurance_decision() {
		return insurance_decision;
	}
	public void setInsurance_decision(int insurance_decision) {
		this.insurance_decision = insurance_decision;
	}
	public ClaimStatus getClaim_status() {
		return claim_status;
	}
	public void setClaim_status(ClaimStatus claim_status) {
		this.claim_status = claim_status;
	}
	public String getClaim_response_description_from_Capitol() {
		return claim_response_description_from_Capitol;
	}
	public void setClaim_response_description_from_Capitol(String claim_response_description_from_Capitol) {
		this.claim_response_description_from_Capitol = claim_response_description_from_Capitol;
	}
	public String getClaim_remarks_from_Bvis() {
		return claim_remarks_from_Bvis;
	}
	public void setClaim_remarks_from_Bvis(String claim_remarks_from_Bvis) {
		this.claim_remarks_from_Bvis = claim_remarks_from_Bvis;
	}
	public Long getClaimID() {
		return claimID;
	}
	public void setClaimID(Long claimID) {
		this.claimID = claimID;
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
	
	
	public Collection<ClaimReview> getClaimReviews() {
		return claimReviews;
	}
	public void setClaimReviews(Collection<ClaimReview> claimReviews) {
		this.claimReviews = claimReviews;
	}
	public Insurance getInsurance() {
		return insurance;
	}
	public void setInsurance(Insurance insurance) {
		this.insurance = insurance;
	}
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	
	public RentalOrder getRentalOrder() {
		return rentalOrder;
	}
	public void setRentalOrder(RentalOrder rentalOrder) {
		this.rentalOrder = rentalOrder;
	}
	public BigDecimal getCustomerCosts() {
		return customerCosts;
	}
	public void setCustomerCosts(BigDecimal customerCosts) {
		this.customerCosts = customerCosts;
	}
	public boolean isReportedByCustomer() {
		return reportedByCustomer;
	}
	public void setReportedByCustomer(boolean reportedByCustomer) {
		this.reportedByCustomer = reportedByCustomer;
	}
	public boolean isTowingServiceNeeded() {
		return towingServiceNeeded;
	}
	public void setTowingServiceNeeded(boolean towingServiceNeeded) {
		this.towingServiceNeeded = towingServiceNeeded;
	}
	public void setInvolvedParties(Collection<InvolvedParty> involvedParties) {
		this.involvedParties = involvedParties;
	}
	
}
