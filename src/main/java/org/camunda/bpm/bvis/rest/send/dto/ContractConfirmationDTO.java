package org.camunda.bpm.bvis.rest.send.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ContractConfirmationDTO {

	private String processinstance_id_bvis;
	private String processinstance_id_capitol;
	private ContractConfirmationOrder order;
	public String getProcessintance_id_bvis() {
		return processinstance_id_bvis;
	}
	public void setProcessintance_id_bvis(String processinstance_id_bvis) {
		this.processinstance_id_bvis = processinstance_id_bvis;
	}
	public String getProcessinstance_id_capitol() {
		return processinstance_id_capitol;
	}
	public void setProcessinstance_id_capitol(String processinstance_id_capitol) {
		this.processinstance_id_capitol = processinstance_id_capitol;
	}
	public ContractConfirmationOrder getOrder() {
		return order;
	}
	public void setOrder(ContractConfirmationOrder order) {
		this.order = order;
	}
}
