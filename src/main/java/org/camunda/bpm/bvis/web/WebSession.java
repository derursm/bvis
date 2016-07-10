package org.camunda.bpm.bvis.web;

import java.io.Serializable;

import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.camunda.bpm.bvis.ejb.beans.CustomerServiceBean;
import org.camunda.bpm.bvis.entities.Customer;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;


@ManagedBean
@SessionScoped
public class WebSession implements Serializable {

	private static final long serialVersionUID = 3993393698215789473L;

	private long uid = -1;
	
	boolean loggedIn;
	
	@EJB
	private CustomerServiceBean customerService;
	
	public WebSession() {
	}

	// getter and setter
	
	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}
	
	// methods
	
	public long login(String username, String password) {
		Customer customer = customerService.getCustomerByUsernameAndPassword(username, password);
		if (customer == null) return -1;
		else {
			this.setLoggedIn(true);
			this.setUid(customer.getCustomerID());
			return customer.getCustomerID();
		}
	}
	
	public String login(int uid){
		try{
		setUid(uid);
		setLoggedIn(true);
		}catch(EJBException e){
			// already caught before
			// FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid credentials")); 
			// return Util.getUrl(Pages.LOGIN, false, false);
		}		
		
		return null;
	}
	
	public String logout(){
		setLoggedIn(false);
		setUid(-1);
		HttpSession hs = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		hs.invalidate();
		return null;
	}
	
}
