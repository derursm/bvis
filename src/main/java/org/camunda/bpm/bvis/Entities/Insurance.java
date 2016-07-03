package org.camunda.bpm.bvis.Entities;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Insurance implements Serializable {
	
	public RentalOrder getOrder() {
		return order;
	}
	public void setOrder(RentalOrder order) {
		this.order = order;
	}
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected long insuranceID;
	@NotNull
	protected InsuranceType type;
	@NotNull
	@Min(value=0)
	protected BigDecimal deductible;
	@NotNull
	@Min(value=0)
	protected BigDecimal estimatedCosts;
	@Min(value=0)
	protected BigDecimal actualCosts;
	
	@Temporal(TemporalType.DATE)
	protected Date pickUpDate;
	@Temporal(TemporalType.DATE)
	protected Date returnDate;
	
	protected String inquiryText;
	
	protected InsuranceAnswer insuranceAnswer;

	@OneToOne(mappedBy = "insurance")
	protected RentalOrder order;
	
	
	public InsuranceAnswer getInsuranceAnswer() {
		return insuranceAnswer;
	}
	public void setInsuranceAnswer(InsuranceAnswer insuranceAnswer) {
		this.insuranceAnswer = insuranceAnswer;
	}
	public String getInquiryText() {
		return inquiryText;
	}
	public void setInquiryText(String inquiryText) {
		this.inquiryText = inquiryText;
	}
	public InsuranceType getType() {
		return type;
	}
	public void setType(InsuranceType type) {
		this.type = type;
	}
	public BigDecimal getDeductible() {
		return deductible;
	}
	public void setDeductible(BigDecimal deductible) {
		this.deductible = deductible;
	}
	
	public BigDecimal getEstimatedCost() {
		return estimatedCosts;
	}
	public void setEstimatedCost(BigDecimal estimatedCosts) {
		this.estimatedCosts = estimatedCosts;
	}
	public BigDecimal getEstimatedCosts() {
		return estimatedCosts;
	}
	public void setEstimatedCosts(BigDecimal estimatedCosts) {
		this.estimatedCosts = estimatedCosts;
	}
	public BigDecimal getActualCosts() {
		return actualCosts;
	}
	public void setActualCosts(BigDecimal actualCosts) {
		this.actualCosts = actualCosts;
	}
	public long getInsuranceID() {
		return insuranceID;
	}
	public void setInsuranceID(long insuranceID) {
		this.insuranceID = insuranceID;
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
	
	
	
}
