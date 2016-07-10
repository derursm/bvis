package org.camunda.bpm.bvis.web;

import java.io.IOException;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.camunda.bpm.bvis.ejb.beans.CustomerServiceBean;
import org.camunda.bpm.bvis.web.util.WebUrls;

@RequestScoped
@ManagedBean(name = "login")
public class LoginBackingBean {

	@ManagedProperty(value="#{webSession}")
	private WebSession sessionBean;
	
	@EJB
	private CustomerServiceBean customerService;
	
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setSessionBean(WebSession sessionBean) {
		this.sessionBean = sessionBean;
	}
	
	public void login() {
		long customerID = sessionBean.login(username, password);
		if (customerID == -1) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid credentials"));
		}
		else {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect(WebUrls.getUrl(WebUrls.INDEX, false, false));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
