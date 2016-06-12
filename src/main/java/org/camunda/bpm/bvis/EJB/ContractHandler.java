package org.camunda.bpm.bvis.EJB;

import org.apache.commons.io.IOUtils;
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
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.IllegalFormatException;
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
	 
	// private static SendHTMLEmail sendMail;


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
//    rentalOrder.setPick_up_date((Date) variables.get("pickUpDate"));
//    rentalOrder.setReturn_date((Date) variables.get("returnDate"));
    
    Long pickUpLocationId = (Long.parseLong((String)variables.get("pickUpLocation")));
    Long returnStoreId = (Long.parseLong((String)variables.get("returnStore")));
    
    rentalOrder.setPick_up_store((PickUpLocation) variables.get(locationService.getPickUpLocation(pickUpLocationId)));
    rentalOrder.setReturn_store((PickUpLocation) variables.get(locationService.getPickUpLocation(returnStoreId)));
    
    InsuranceType insuranceType = InsuranceType.valueOf((String) variables.get("insuranceType"));
    rentalOrder.setInsurance_type((InsuranceType) insuranceType);
    
    rentalOrder.setInquiryText((String) variables.get("inquiryText"));
    rentalOrder.setFleetRental((Boolean) variables.get("fleet"));
    
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

  //General send email method. State states the content of the email. Email information is caught from
  //process variables 
  public void sendOrderStateNotification(DelegateExecution delegateExecution)throws ParseException{
	  long orderId;
	  RentalOrder order;
	  Customer customer;
	  String surname, subject, text, from, email, state, path, pickupLocation, returnLocation, insurancePac, carModel;
	  Date rentalStart, rentalEnd;
	  //tbc..
	  	  
  	  // Get all process variables
      Map<String, Object> variables = delegateExecution.getVariables();
      orderId = (long) variables.get("orderId");
      state = variables.get("state").toString();
      order = orderService.getOrder(orderId);
      customer = order.getCustomer();
            
      //Get rental information
      surname = customer.getSurname();
      email = customer.getEmail();
      from = "bvis@bvis.com";
//      rentalStart = order.getPick_up_date();
//      rentalEnd = order.getReturn_date();
      pickupLocation = ""; //order.getPick_up_store().getStoreName() + order.getPick_up_store().getCity();
      returnLocation = ""; //order.getReturn_store().getStoreName() + order.getReturn_store().getCity();
      carModel = "Dummy"; //order.getCars();
      
      subject = "";
      
      path = "/templates/";
      
      //built email template path by state
      switch(state){
  			//case "canc_fleet": path += "canc_fleet.txt"; subject = "Booking reservation (" + String.valueOf(orderId) + ")" ; break;
  			case "canc_single": path += "canc_single.txt"; subject = " ...("; // + String.valueOf(orderId) + ")" ; break;
  			case "conf_req": path += "conf_req.txt"; subject = "Booking reservation (" + String.valueOf(orderId) + ")" ; break;
  			//case "rej_el": path += "rej_el.txt"; subject = "... (" + String.valueOf(orderId) + ")" ; break;
  			//case "rej_ins": path += "rej_ins.txt"; subject = "... (" + String.valueOf(orderId) + ")" ; break;
  			//case "send_cont": path += "send_cont.txt"; subject = "... (" + String.valueOf(orderId) + ")" ; break;
  			//case "send_sorrow": path += "send_sorrow.txt"; subject = "... (" + String.valueOf(orderId) + ")" ; break;
      }	  
             		  
      InputStream in = this.getClass().getResourceAsStream(path);
    
      try{
    	  text = IOUtils.toString(in, "UTF-8");     	  
      } catch (IOException e) {
    	  text = "error in file reading. path: " + path;
    	  email = "bvis@bvis.com";
      } catch (NullPointerException  e){
    	  text = "null pointer file reading. path: " + path;
    	  email = "bvis@bvis.com";
      } 
      
      try{
      text = String.format(text, surname, carModel, pickupLocation, returnLocation, String.valueOf(orderId));  
      } catch( IllegalFormatException e){
    	 text = "illegal conversion ";
    	 email = "bvis@bvis.com";
      }
      SendHTMLEmail.main(subject, text , from, email);
  }
}


