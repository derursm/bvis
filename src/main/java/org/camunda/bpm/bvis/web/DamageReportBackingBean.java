package org.camunda.bpm.bvis.web;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.camunda.bpm.bvis.ejb.ClaimServiceBean;
import org.camunda.bpm.bvis.ejb.OrderServiceBean;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.cdi.compat.CamundaTaskForm;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.ocpsoft.rewrite.annotation.Rule;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Named
@ConversationScoped
@ManagedBean(name = "damageReport")
public class DamageReportBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long orderID;
	private String damageDescription;
	private Date damageDate;
	private String vehicleID;
	private String party1Firstname;
	private String party1Surname;
	private String party1Street;
	private String party1StreetNo;
	private String party1Country;
	private String party1ZIP;
	private String party1Company;
	private String party1Insurance;
	private String party1Phone;
	private String party1EMail;
	private Date party1Birthday;
	
	@EJB
	private OrderServiceBean orderService;
	
	@EJB
	private ClaimServiceBean claimService;
	
	@Inject 
	private CamundaTaskForm taskForm;
	
	
	public Date getParty1Birthday() {
		return party1Birthday;
	}
	public void setParty1Birthday(Date party1Birthday) {
		this.party1Birthday = party1Birthday;
	}
	
	
	public long getOrderID() {
		return orderID;
	}
	public void setOrderID(long orderID) {
		this.orderID = orderID;
	}
	public String getDamageDescription() {
		return damageDescription;
	}
	public void setDamageDescription(String damageDescription) {
		this.damageDescription = damageDescription;
	}
	public Date getDamageDate() {
		return damageDate;
	}
	public void setDamageDate(Date damageDate) {
		this.damageDate = damageDate;
	}
	public String getVehicleID() {
		return vehicleID;
	}
	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}
	public String getParty1Firstname() {
		return party1Firstname;
	}
	public void setParty1Firstname(String party1Firstname) {
		this.party1Firstname = party1Firstname;
	}
	public String getParty1Surname() {
		return party1Surname;
	}
	public void setParty1Surname(String party1Surname) {
		this.party1Surname = party1Surname;
	}
	public String getParty1Street() {
		return party1Street;
	}
	public void setParty1Street(String party1Street) {
		this.party1Street = party1Street;
	}
	public String getParty1StreetNo() {
		return party1StreetNo;
	}
	public void setParty1StreetNo(String party1StreetNo) {
		this.party1StreetNo = party1StreetNo;
	}
	public String getParty1Country() {
		return party1Country;
	}
	public void setParty1Country(String party1Country) {
		this.party1Country = party1Country;
	}
	public String getParty1ZIP() {
		return party1ZIP;
	}
	public void setParty1ZIP(String party1zip) {
		party1ZIP = party1zip;
	}
	public String getParty1Company() {
		return party1Company;
	}
	public void setParty1Company(String party1Company) {
		this.party1Company = party1Company;
	}
	public String getParty1Insurance() {
		return party1Insurance;
	}
	public void setParty1Insurance(String party1Insurance) {
		this.party1Insurance = party1Insurance;
	}
	public String getParty1Phone() {
		return party1Phone;
	}
	public void setParty1Phone(String party1Phone) {
		this.party1Phone = party1Phone;
	}
	public String getParty1EMail() {
		return party1EMail;
	}
	public void setParty1EMail(String party1eMail) {
		party1EMail = party1eMail;
	}
	
	public void reportDamage() {
		if (!orderService.orderExists(orderID)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid order ID"));
		}
		else if (!orderService.vehicleExistsForOrder(orderID, vehicleID)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Specified car not part of the order"));
		}
		else if (!claimService.insuranceExists(party1Insurance)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Specified insurance does not exist"));			
		}
		else {
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("claimID", "1");
			ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
			//RepositoryService repositoryService = processEngine.getRepositoryService();
			//repositoryService.createDeployment().addClasspathResource("Claim Handling.bpmn20.xml").deploy();
			RuntimeService runtimeService = processEngine.getRuntimeService();
			System.out.println("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("claimHandling", variables);
			System.out.println("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());
		}
	}
	
}