package org.camunda.bpm.bvis.Persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class InvolvedParty implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected Long party_ID;
	
	protected String name;
	protected String address;
	protected String insurance_company_name;
	
	public Long getParty_ID() {
		return party_ID;
	}
	public void setParty_ID(Long party_ID) {
		this.party_ID = party_ID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getInsurance_company_name() {
		return insurance_company_name;
	}
	public void setInsurance_company_name(String insurance_company_name) {
		this.insurance_company_name = insurance_company_name;
	}
	
	
	
}
