package org.camunda.bpm.bvis.rest.receive.dto;

public class InsuranceAnswer {
	private String processInstanceIDBVIS;
	private String processInstanceIDCapitol;
	private Order order;
	
	public InsuranceAnswer() {}
	
	public InsuranceAnswer(String processInstanceIDBVIS,
			String processInstanceIDCapitol,
			Order order) {
		this.processInstanceIDBVIS = processInstanceIDBVIS;
		this.processInstanceIDCapitol = processInstanceIDCapitol;
		this.order = order;  
	}
	
	public String getProcessInstanceIDBVIS() {
		return processInstanceIDBVIS;
	}
	public void setProcessInstanceIDBVIS(String processInstanceIDBVIS) {
		this.processInstanceIDBVIS = processInstanceIDBVIS;
	}
	public String getProcessInstanceIDCapitol() {
		return processInstanceIDCapitol;
	}
	public void setProcessInstanceIDCapitol(String processInstanceIDCapitol) {
		this.processInstanceIDCapitol = processInstanceIDCapitol;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}	
}
