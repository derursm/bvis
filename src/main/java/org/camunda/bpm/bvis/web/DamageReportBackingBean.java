package org.camunda.bpm.bvis.web;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.camunda.bpm.bvis.ejb.beans.ClaimServiceBean;
import org.camunda.bpm.bvis.ejb.beans.InsuranceServiceBean;
import org.camunda.bpm.bvis.ejb.beans.OrderServiceBean;
import org.camunda.bpm.bvis.web.util.EmailValidator;
import org.camunda.bpm.bvis.web.util.WebUrls;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.cdi.compat.CamundaTaskForm;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Named
@RequestScoped
@ManagedBean(name = "damageReport")
public class DamageReportBackingBean  {

	
	private long orderID;
	private String damageDescription;
	private Date damageDate;
	private String vehicleID;
	private String party1Firstname;
	private String party1Surname;
	private String party1Street;
	private String party1HouseNo;
	private String party1Country;
	private String party1ZIP;
	private String party1Company;
	private String party1Insurance;
	private String party1Phone;
	private String party1EMail;
	private String party1City;
	private Date party1Birthday;
	private boolean reportedByCustomer;
	private boolean towingServiceNeeded;
	
	@EJB
	private OrderServiceBean orderService;
	
	@EJB
	private ClaimServiceBean claimService;
	
	@EJB
	private InsuranceServiceBean insuranceService;	
	
	public Date getParty1Birthday() {
		return party1Birthday;
	}
	public void setParty1Birthday(Date party1Birthday) {
		this.party1Birthday = party1Birthday;
	}
	
	
	public boolean isReportedByCustomer() {
		return reportedByCustomer;
	}
	public void setReportedByCustomer(boolean reportedByCustomer) {
		this.reportedByCustomer = reportedByCustomer;
	}
	public boolean isTowingServiceNeeded() {
		return towingServiceNeeded;
	}
	public void setTowingServiceNeeded(boolean towingServiceNeeded) {
		this.towingServiceNeeded = towingServiceNeeded;
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
	public String getParty1HouseNo() {
		return party1HouseNo;
	}
	public void setParty1HouseNo(String party1HouseNo) {
		this.party1HouseNo = party1HouseNo;
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
	
	public String getParty1City() {
		return party1City;
	}
	public void setParty1City(String party1City) {
		this.party1City = party1City;
	}
	public void reportDamage() {
		if (!orderService.orderExists(orderID)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid order ID"));
		}
		else if (!orderService.vehicleExistsForOrder(orderID, vehicleID)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Specified car not part of the order"));
		}
		else if (!insuranceService.insuranceExists(party1Insurance)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Specified insurance does not exist"));			
		}
		else if (!EmailValidator.validate(party1EMail)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid Email format"));
		}
		else {
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("orderID", orderID);
			variables.put("damageDescription", damageDescription);
			variables.put("damageDate", damageDate);
			variables.put("vehicleID", vehicleID);
			// set to true per default, since this bean method is only invoked if the damage is reported by customer
			reportedByCustomer = true;
			variables.put("reportedByCustomer", reportedByCustomer);
			variables.put("towingServiceNeeded", towingServiceNeeded);
			variables.put("party1Firstname", party1Firstname);
			variables.put("party1Surname", party1Surname);
			variables.put("party1Street", party1Street);
			variables.put("party1HouseNo", party1HouseNo);
			variables.put("party1Country", party1Country);
			variables.put("party1ZIP", party1ZIP);
			variables.put("party1City", party1City);
			variables.put("party1Company", party1Company);
			variables.put("party1Insurance", party1Insurance);
			variables.put("party1Phone", party1Phone);
			variables.put("party1EMail", party1EMail);
			variables.put("party1Birthday", party1Birthday);
			ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtimeService = processEngine.getRuntimeService();
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("claimHandling", variables);

			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect(WebUrls.getUrl(WebUrls.DAMAGE_REPORT_SUBMITTED, false, false));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void reportDamageCamunda() {
		if (!orderService.orderExists(orderID)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid order ID"));
		}
		else if (!orderService.vehicleExistsForOrder(orderID, vehicleID)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Specified car not part of the order"));
		}
		else if (!insuranceService.insuranceExists(party1Insurance)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Specified insurance does not exist"));
		}
		else if (!EmailValidator.validate(party1EMail)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid Email format"));
		}
		else {
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("orderID", orderID);
			variables.put("damageDescription", damageDescription);
			variables.put("damageDate", damageDate);
			variables.put("vehicleID", vehicleID);
			variables.put("reportedByCustomer", reportedByCustomer);
			variables.put("towingServiceNeeded", towingServiceNeeded);
			variables.put("party1Firstname", party1Firstname);
			variables.put("party1Surname", party1Surname);
			variables.put("party1Street", party1Street);
			variables.put("party1HouseNo", party1HouseNo);
			variables.put("party1Country", party1Country);
			variables.put("party1ZIP", party1ZIP);
			variables.put("party1City", party1City);
			variables.put("party1Company", party1Company);
			variables.put("party1Insurance", party1Insurance);
			variables.put("party1Phone", party1Phone);
			variables.put("party1EMail", party1EMail);
			variables.put("party1Birthday", party1Birthday);
			ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtimeService = processEngine.getRuntimeService();
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("claimHandling", variables);

			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("/camunda/app/tasklist/default/#/");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
