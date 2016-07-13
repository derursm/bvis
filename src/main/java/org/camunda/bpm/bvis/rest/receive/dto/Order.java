package org.camunda.bpm.bvis.rest.receive.dto;

import java.util.Date;

public class Order {

	private long order_id;
	private Date request_date;
	private String inquiry_text;
	private int result;
	private double final_price;
	
	public Order() {}
	
	public Order(long orderID, Date requestDate, String inquiryText, int result, double finalPrice) {
		this.order_id = orderID;
		this.request_date = requestDate;
		this.inquiry_text = inquiryText;
		this.result = result;
		this.final_price = finalPrice;
	}
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}

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

	public String getInquiry_text() {
		return inquiry_text;
	}

	public void setInquiry_text(String inquiry_text) {
		this.inquiry_text = inquiry_text;
	}

	public double getFinal_price() {
		return final_price;
	}

	public void setFinal_price(double final_price) {
		this.final_price = final_price;
	}
	
	
}
