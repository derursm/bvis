package org.camunda.bpm.bvis.entities;

public enum OrderStatus {
	PENDING("pending"), REJECTED("rejectd"), ACCEPTED("accepted");
	private String s;
	
	private OrderStatus(String s) {
		this.s = s;
	}
	
	public String toString() {
		return s;
	}
}
