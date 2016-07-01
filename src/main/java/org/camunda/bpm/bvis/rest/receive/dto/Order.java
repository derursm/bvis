package org.camunda.bpm.bvis.rest.receive.dto;

public class Order {

	private long orderID;
	private String requestDate;
	private String inquiryText;
	private int result;
	private double finalPrice;
	
	public Order() {}
	
	public Order(long orderID, String requestDate, String inquiryText, int result, double finalPrice) {
		this.orderID = orderID;
		this.requestDate = requestDate;
		this.inquiryText = inquiryText;
		this.result = result;
		this.finalPrice = finalPrice;
	}
	
	
	public long getOrderID() {
		return orderID;
	}
	public void setOrderID(long orderID) {
		this.orderID = orderID;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getInquiryText() {
		return inquiryText;
	}
	public void setInquiryText(String inquiryText) {
		this.inquiryText = inquiryText;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public double getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}
	
	
}
