package org.camunda.bpm.bvis.web;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.camunda.bpm.bvis.ejb.beans.CustomerServiceBean;
import org.camunda.bpm.bvis.entities.Customer;
import org.camunda.bpm.bvis.web.util.EmailValidator;
import org.camunda.bpm.bvis.web.util.WebUrls;

@Named
@RequestScoped
@ManagedBean(name = "registration")
public class RegistrationBackingBean {

	private String firstname;
	private String surname;
	private String email;
	private String phoneNumber;
	private String street;
	private String houseNumber;
	private String postcode;
	private String city;
	private String country;
	private Date dateOfBirth;
	private boolean company;
	private String companyName;
	
	private String password;
	private String username;
	
	@EJB
	private CustomerServiceBean customerService;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public boolean isCompany() {
		return company;
	}
	public void setCompany(boolean company) {
		this.company = company;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public void register() {
		if (customerService.getCustomerByUsername(username) != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Username already taken"));
		}
		else if (customerService.getCustomerByEmail(email) != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Email already taken"));
		}
		else if (!EmailValidator.validate(email)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid Email format"));
		}
		else {
			Customer customer = new Customer();
			customer.setCity(city);
			customer.setCompany(company);
			customer.setCompanyName(companyName);
			customer.setCountry(country);
			customer.setDateOfBirth(dateOfBirth);
			customer.setEmail(email);
			customer.setFirstname(firstname);
			customer.setHouseNumber(houseNumber);
			customer.setPassword(password);
			customer.setPhoneNumber(phoneNumber);
			customer.setPostcode(postcode);
			customer.setStreet(street);
			customer.setSurname(surname);
			customer.setUsername(username);
			customerService.create(customer);
			System.out.println("CREATED CUSTOMER WITH ID: " + customer.getCustomerID());
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect(WebUrls.getUrl(WebUrls.REGISTRATION_SUCCESSFUL,false, false));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
