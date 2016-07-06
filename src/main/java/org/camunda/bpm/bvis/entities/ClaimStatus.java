package org.camunda.bpm.bvis.entities;

public enum ClaimStatus {
	REJECTED("rejected"), ACCEPTED("accepted"), ADJUSTED("adjusted"), 
	INSURANCE_ANSWER_PENDING("insurance answer pending"), 
	NOT_SEND_YET("not send yet");
	private final String rep;
		
	private ClaimStatus(String rep) {
		this.rep = rep;
	}

    public String toString() {
       return this.rep;
    }

}
