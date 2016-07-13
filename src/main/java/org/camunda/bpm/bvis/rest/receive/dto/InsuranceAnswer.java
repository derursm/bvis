package org.camunda.bpm.bvis.rest.receive.dto;

public class InsuranceAnswer {
	private String processinstance_id_bvis;
	private String processinstance_id_capitol;
	private Order order;
	
	public InsuranceAnswer() {}
	
	public InsuranceAnswer(String processInstanceIDBVIS,
			String processInstanceIDCapitol,
			Order order) {
		this.processinstance_id_bvis = processInstanceIDBVIS;
		this.processinstance_id_capitol = processInstanceIDCapitol;
		this.order = order;  
	}

	public String getProcessinstance_id_bvis() {
		return processinstance_id_bvis;
	}

	public void setProcess_instance_bvis(String processinstance_id_bvis) {
		this.processinstance_id_bvis = processinstance_id_bvis;
	}

	public String getProcessinstance_id_capitol() {
		return processinstance_id_capitol;
	}

	public void setProcessinstance_id_capitol(String processinstance_id_capitol) {
		this.processinstance_id_capitol = processinstance_id_capitol;
	}

	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}	
}
