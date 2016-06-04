package org.camunda.bpm.bvis.EJB;

import org.camunda.bpm.bvis.Entites.Car;
import org.camunda.bpm.bvis.Entites.Customer;
import org.camunda.bpm.bvis.Entites.InsuranceType;
import org.camunda.bpm.bvis.Entites.Order;
import org.camunda.bpm.bvis.Entites.PickUpLocation;
import org.camunda.bpm.bvis.Web.OrderCreateBean;
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
    
    Long pickUpLocationId = (Long.parseLong((String)variables.get("pickUpLocation")));
    Long returnStoreId = (Long.parseLong((String)variables.get("returnStore")));
    
    order.setPick_up_store((PickUpLocation) variables.get(locationService.getPickUpLocation(pickUpLocationId)));
    order.setReturn_store((PickUpLocation) variables.get(locationService.getPickUpLocation(returnStoreId)));
    order.setInsurance_type((InsuranceType) InsuranceType.TYPE1);
    order.setInquiry_text((String) variables.get("inquiryText"));
    order.setFleet_rental((Boolean) variables.get("fleet"));
    
    Long carId = (Long.parseLong((String)variables.get("car")));
    Car car = carService.getCar(carId);
    
    Collection<Car> cars = new ArrayList<Car>();
    cars.add(car);
    
    order.setCars((Collection<Car>) cars);
    order.setInsurance_ID(0);
   
    //orderService.create(order);
    
    // Remove no longer needed process variables
    delegateExecution.removeVariables(variables.keySet());

    // Add newly created order id as process variable
    delegateExecution.setVariable("orderId", order.getId());
  }

}