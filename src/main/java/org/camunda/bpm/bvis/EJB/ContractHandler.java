package org.camunda.bpm.bvis.EJB;

import org.apache.commons.io.IOUtils;
import org.camunda.bpm.bvis.Entities.Car;
import org.camunda.bpm.bvis.Entities.Customer;
import org.camunda.bpm.bvis.Entities.InsuranceType;
import org.camunda.bpm.bvis.Entities.PickUpLocation;
import org.camunda.bpm.bvis.Entities.RentalOrder;
import org.camunda.bpm.bvis.Util.SendHTMLEmail;
import org.camunda.bpm.bvis.rest.send.service.SendInquiry;
import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.Map;

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
	 
	  // Inject task form available through the Camunda cdi artifact
	  @Inject
	  private TaskForm taskForm;
	  
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
    rentalOrder.setPick_up_date((Date) variables.get("pickUpDate"));
    rentalOrder.setReturn_date((Date) variables.get("returnDate"));
    
    Long pickUpLocationId = (Long.parseLong((String)variables.get("pickUpLoc")));
    Long returnStoreId = (Long.parseLong((String)variables.get("returnStore")));
    
    rentalOrder.setPickUpStore((PickUpLocation) locationService.getPickUpLocation(pickUpLocationId));
    rentalOrder.setReturnStore((PickUpLocation) locationService.getPickUpLocation(returnStoreId));
    
    InsuranceType insuranceType = InsuranceType.valueOf((String) variables.get("insuranceType"));
    rentalOrder.setInsurance_type((InsuranceType) insuranceType);
    
    rentalOrder.setInquiryText((String) variables.get("inquiryText"));
    rentalOrder.setFleetRental((Boolean) variables.get("fleet"));
    rentalOrder.setClerkComments("");
    rentalOrder.setApproveStatus(false);
    
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

  public RentalOrder getOrder(Long orderId) {
	    // Load order entity from database
	    return orderService.getOrder(orderId);
	  }
  
  public void updateOrder(RentalOrder rentalOrder) {
	    // Merge detached order entity with current persisted state
	  orderService.updateOrder(rentalOrder);
	  
	    try {
	      // Complete user task from
	      taskForm.completeTask();
	    } catch (IOException e) {
	      // Rollback both transactions on error
	      throw new RuntimeException("Cannot complete task", e);
	    }
	  }
  
  //General send email method. State states the content of the email. Email information is caught from
  //process variables 
  public void sendOrderStateNotification(DelegateExecution delegateExecution)throws ParseException{
	  RentalOrder order;
	  Customer customer;
	  Car car;
	  Collection<Car> cars;
	  String surname, subject, text, from, email, state, path, pickupLocation, returnLocation,
	  insurancePac, carModel, rentalEnd, rentalStart, orderId_str, clerkComment, towingAddress;
	  Long orderId;
	  boolean isFleetRental;
	  //tbc..
	  	  
  	  // Get all process variables
      Map<String, Object> variables = delegateExecution.getVariables();
      orderId = (long) variables.get("orderId");
      state = variables.get("state").toString();
      order = orderService.getOrder(orderId);
      customer = order.getCustomer();
      cars = order.getCars(); 
      
      surname = "surname";
      email = "email";
      from = "from";
      pickupLocation = "pickupLocation"; //order.getPick_up_store().getStoreName() + order.getPick_up_store().getCity();
      returnLocation = "returnLocation"; //order.getReturn_store().getStoreName() + order.getReturn_store().getCity();
      carModel = "carmodel"; //order.getCars();
      rentalStart = "rentalStart";
      rentalEnd = "rentalEnd";
      orderId_str = "orderId";
      towingAddress = "towingAddress";
      insurancePac = "insurancePac";
      
      //Get rental information
      isFleetRental = order.isFleetRental();
      orderId_str = orderId.toString();
      surname = customer.getSurname();
      email = customer.getEmail();
      
      from = "bvis@bvis.com";
      rentalStart = order.getPick_up_date().toString();
      rentalEnd = order.getReturn_date().toString();
      pickupLocation = order.getPickUpStore().getHTMLContactDetails();    
      returnLocation = order.getReturnStore().getHTMLContactDetails();
      clerkComment = order.getClerkComments();
      carModel = "";
      for (Car loop_car : cars){
    	  carModel += loop_car.getHTMLCarDetails() + "<br>";
      }
      
      
      subject = "";
      
      path = "/templates/";
      
      //built email template path by state
      switch(state){
  			case "canc_fleet": path += "canc_fleet.txt"; subject = "We are sorry... (No. " + orderId_str + ")" ; break;
  			case "canc_single": path += "canc_single.txt"; subject = "We are sorry... (No. " + orderId_str + ")" ; break;
  			case "conf_req": path += "conf_req.txt"; subject = "Booking reservation (No. " + orderId_str + ")" ; break;
  			case "rej_el": path += "rej_el.txt"; subject = "We are sorry... (No. " + orderId_str + ")" ; break;
  			case "rej_ins": path += "rej_ins.txt"; subject = "We are sorry... (No. " + orderId_str + ")" ; break;
  			case "send_cont": path += "send_cont.txt"; subject = "Congratulation! (No. " + orderId_str + ")" ; break;
  			case "send_sorrow": path += "send_sorrow.txt"; subject = "Good bye (No. " + orderId_str + ")" ; break;
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
      text = String.format(text, surname, carModel, pickupLocation, rentalStart, returnLocation, rentalEnd, orderId_str, towingAddress, insurancePac);  
      } catch( IllegalFormatException e){
    	 subject = "illegal conversion ";    	 
    	 email = "bvis@bvis.com";
      }
      SendHTMLEmail.main(subject, text , from, email);
  }
  
  public void sendInquiryToCapitol(DelegateExecution delegateExecution) {
	  SendInquiry sender = new SendInquiry();
	  sender.sendInquiry((Integer)(delegateExecution.getVariable("orderID")), delegateExecution.getActivityInstanceId());
  }
}


