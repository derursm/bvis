package org.camunda.bpm.bvis.EJB;

import org.camunda.bpm.bvis.Entites.Customer;
import org.camunda.bpm.bvis.Entites.Order;
import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

  public void persistOrder(DelegateExecution delegateExecution) throws ParseException {
    // Create new order instance
    Order order = new Order();
	
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
    order.setCustomer(customer);

    order.setPick_up_date((Date) variables.get("pickUpDate"));
    order.setReturn_date((Date) variables.get("returnDate"));
    //order.setPick_up_store(pickUpStore);
    //order.setReturn_store(returnStore);
    //order.setInsurance_type(insuranceType);
    order.setInquiry_text((String) variables.get("inquiryText"));
    order.setFleet_rental((Boolean) variables.get("fleet"));
    //order.setCars(cars);
    order.setInsurance_ID(0);
    //orderService.create(order);
    
    // Remove no longer needed process variables
    delegateExecution.removeVariables(variables.keySet());

    // Add newly created order id as process variable
    delegateExecution.setVariable("orderId", order.getId());
  }

}