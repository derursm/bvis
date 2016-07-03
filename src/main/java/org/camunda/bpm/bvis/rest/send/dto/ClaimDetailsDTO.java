package org.camunda.bpm.bvis.rest.send.dto;

import java.util.Calendar;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ClaimDetailsDTO {

	private String processinstance_id_bvis;
	private Calendar request_date;
	private Claim claim;

	

	public String getProcessinstance_id_bvis() {
		return processinstance_id_bvis;
	}

	public void setProcessinstance_id_bvis(String processinstance_id_bvis) {
		this.processinstance_id_bvis = processinstance_id_bvis;
	}

	public Calendar getRequest_date() {
		return request_date;
	}

	public void setRequest_date(Calendar request_date) {
		this.request_date = request_date;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}
}
