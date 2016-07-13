package org.camunda.bpm.bvis.rest.send.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Insurance {
	private int insurance_id;
	private String type;
	private double deductible;
	private Date pick_up_date;
	private Date return_date;
	
	public int getInsurance_id() {
		return insurance_id;
	}
	public void setInsurance_id(int insurance_id) {
		this.insurance_id = insurance_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getDeductible() {
		return deductible;
	}
	public void setDeductible(double deductible) {
		this.deductible = deductible;
	}
	public Date getPick_up_date() {
		return pick_up_date;
	}
	public void setPick_up_date(Date pick_up_date) {
		this.pick_up_date = pick_up_date;
	}
	public Date getReturn_date() {
		return return_date;
	}
	public void setReturn_date(Date return_date) {
		this.return_date = return_date;
	}
	
}