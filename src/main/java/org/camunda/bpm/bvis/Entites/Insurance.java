package org.camunda.bpm.bvis.Entites;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Insurance implements Serializable {
	
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
	
	public long getInsuranceID() {
		return insuranceID;
	}
	public void setInsuranceID(long insuranceID) {
		this.insuranceID = insuranceID;
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
}
