package org.camunda.bpm.bvis.rest.send.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class FeedbackDTO {
	
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



	public class Decision{
		private int claim_id;
		private int claim_status;
		private String description;
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
		
	}
	
	

}
