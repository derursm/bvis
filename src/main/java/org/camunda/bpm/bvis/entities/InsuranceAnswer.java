package org.camunda.bpm.bvis.entities;

public enum InsuranceAnswer {

	REJECTED("rejected"), ACCEPTED("accepted"), ADJUSTED("adjusted");
	private final String rep;
	
	private InsuranceAnswer(String rep) {
		this.rep = rep;
	}

    public String toString() {
       return this.rep;
    }
}
