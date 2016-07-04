package org.camunda.bpm.bvis.rest.send.dto;

public class ClaimReviewDTO {

	private String processinstance_id_bvis;
	private String processinstance_id_capitol;
	private ClaimReviewDecision decision;
	public String getProcessinstance_id_bvis() {
		return processinstance_id_bvis;
	}
	public void setProcessinstance_id_bvis(String processinstance_id_bvis) {
		this.processinstance_id_bvis = processinstance_id_bvis;
	}
	public String getProcessinstance_id_capitol() {
		return processinstance_id_capitol;
	}
	public void setProcessinstance_id_capitol(String processinstance_id_capitol) {
		this.processinstance_id_capitol = processinstance_id_capitol;
	}
	public ClaimReviewDecision getDecision() {
		return decision;
	}
	public void setDecision(ClaimReviewDecision decision) {
		this.decision = decision;
	}
}
