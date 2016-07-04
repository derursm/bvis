package org.camunda.bpm.bvis.rest.receive.dto;

public class InsuranceClaimDecision {

	private String processinstance_id_bvis;
	
	private String processinstance_id_capitol;
	
	private Decision decision;

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

	public Decision getDecision() {
		return decision;
	}

	public void setDecision(Decision decision) {
		this.decision = decision;
	}
}
