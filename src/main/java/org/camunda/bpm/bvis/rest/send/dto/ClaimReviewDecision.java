package org.camunda.bpm.bvis.rest.send.dto;

public class ClaimReviewDecision {

	public int getClaim_id() {
		return claim_id;
	}
	public void setClaim_id(int claim_id) {
		this.claim_id = claim_id;
	}
	public int getClaim_status() {
		return claim_status;
	}
	public void setClaim_status(int claim_status) {
		this.claim_status = claim_status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	private int claim_id;
	private int claim_status;
	private String description;
}
