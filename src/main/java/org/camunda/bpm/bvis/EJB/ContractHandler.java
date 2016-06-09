package org.camunda.bpm.bvis.EJB;

import org.camunda.bpm.bvis.Entites.Car;
import org.camunda.bpm.bvis.Entites.Customer;
import org.camunda.bpm.bvis.Entites.InsuranceType;
import org.camunda.bpm.bvis.Entites.RentalOrder;
import org.camunda.bpm.bvis.Entites.PickUpLocation;
import org.camunda.bpm.bvis.Web.OrderCreateBean;
import org.camunda.bpm.bvis.Util.SendHTMLEmail;
import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@Named
public class ContractHandler {

	 @EJB
	 private OrderServiceBean orderService;
	
	 @EJB
	 private CustomerServiceBean customerService;
	 
	 @EJB
	 private PickUpLocationServiceBean locationService;
	 
	 @EJB
	 private CarServiceBean carService;
	 
	 private static SendHTMLEmail sendMail;


  public void persistOrder(DelegateExecution delegateExecution) throws ParseException {
    // Create new order instance
    RentalOrder rentalOrder = new RentalOrder();
	
    // Get all process variables
    Map<String, Object> variables = delegateExecution.getVariables();
    Customer customer = new Customer();
    
    customer.setFirstname((String) variables.get("customerFirstname"));
    customer.setSurname((String) variables.get("customerSurname"));
    customer.setEmail((String) variables.get("customerEmail"));
    customer.setPhoneNumber((String) variables.get("customerPhoneNumber"));
    
    customer.setDateOfBirth((Date) variables.get("customerDateOfBirth"));
    customer.setStreet((String) variables.get("customerStreet"));
    customer.setHouseNumber((String) variables.get("customerHouseNumber"));
    customer.setPostcode((String) variables.get("customerPostcode"));
    customer.setCity((String) variables.get("customerCity"));
    customer.setCountry((String) variables.get("customerCountry"));
    customer.setCompany((Boolean) variables.get("customerCompany"));
    customer.setEligibility(false);
    
    customerService.create(customer);
    
    // Set order attributes
    rentalOrder.setCustomer(customer);
    rentalOrder.setPick_up_date((Date) variables.get("pickUpDate"));
    rentalOrder.setReturn_date((Date) variables.get("returnDate"));
    
    Long pickUpLocationId = (Long.parseLong((String)variables.get("pickUpLocation")));
    Long returnStoreId = (Long.parseLong((String)variables.get("returnStore")));
    
    rentalOrder.setPick_up_store((PickUpLocation) variables.get(locationService.getPickUpLocation(pickUpLocationId)));
    rentalOrder.setReturn_store((PickUpLocation) variables.get(locationService.getPickUpLocation(returnStoreId)));
    
    InsuranceType insuranceType = InsuranceType.valueOf((String) variables.get("insuranceType"));
    rentalOrder.setInsurance_type((InsuranceType) insuranceType);
    
    rentalOrder.setInquiry_text((String) variables.get("inquiryText"));
    rentalOrder.setFleet_rental((Boolean) variables.get("fleet"));
    
    Long carId = (Long.parseLong((String)variables.get("car")));
    Car car = carService.getCar(carId);
    Collection<Car> cars = new ArrayList<Car>();
    cars.add(car);
    
    rentalOrder.setCars((Collection<Car>) cars);
    rentalOrder.setInsurance_ID(0);
   
    orderService.create(rentalOrder);
    
    // Remove no longer needed process variables
    delegateExecution.removeVariables(variables.keySet());

    // Add newly created order id as process variable
    delegateExecution.setVariable("orderId", rentalOrder.getId());   
  }

  public void sendOrderStateNotification(DelegateExecution delegateExecution)throws ParseException{
	  long orderId;
	  RentalOrder order;
	  Customer customer;
	  String surname, subject, text, from, email;
	  //tbc..
	  
	  
	  
  	  // Get all process variables
      Map<String, Object> variables = delegateExecution.getVariables();
      orderId = (long) variables.get("orderId");
      order = orderService.getOrder(orderId);
      customer = order.getCustomer();
      
      surname = customer.getSurname();
      email = customer.getEmail();
      subject = "Reservierungsbestätigung";
      from = "bvis@bvis.com";
      text = "Sehr geehrter Herr/Frau " + surname + 
    			"! Thank you for your request. We are processing as soon as possible.";      
      sendMail.main(subject, text , from, email);
  }
}