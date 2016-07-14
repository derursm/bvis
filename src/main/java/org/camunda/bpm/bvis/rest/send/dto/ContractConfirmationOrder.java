package org.camunda.bpm.bvis.rest.send.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ContractConfirmationOrder {

	private long order_id;
	private Date request_date;
	private int contract_status;
	public long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(long order_id) {
		this.order_id = order_id;
	}
	public Date getRequest_date() {
		return request_date;
	}
	public void setRequest_date(Date request_date) {
		this.request_date = request_date;
	}
	public int getContract_status() {
		return contract_status;
	}
	public void setContract_status(int contract_status) {
		this.contract_status = contract_status;
	}
}