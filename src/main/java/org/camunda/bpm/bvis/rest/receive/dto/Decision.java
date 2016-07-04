package org.camunda.bpm.bvis.rest.receive.dto;

public class Decision {

	private int claim_id;
	
	private double coverage_costs;
	
	private double customer_costs;
	
	private int insurance_decision;
	
	private String description;

	public int getClaim_id() {
		return claim_id;
	}

	public void setClaim_id(int claim_id) {
		this.claim_id = claim_id;
	}

	public double getCoverage_costs() {
		return coverage_costs;
	}

	public void setCoverage_costs(double coverage_costs) {
		this.coverage_costs = coverage_costs;
	}

	public double getCustomer_costs() {
		return customer_costs;
	}

	public void setCustomer_costs(double customer_costs) {
		this.customer_costs = customer_costs;
	}

	public int getInsurance_decision() {
		return insurance_decision;
	}

	public void setInsurance_decision(int insurance_decision) {
		this.insurance_decision = insurance_decision;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
