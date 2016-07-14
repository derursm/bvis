package org.camunda.bpm.bvis.ejb;

import org.apache.commons.io.IOUtils;
import org.camunda.bpm.bvis.ejb.beans.CarServiceBean;
import org.camunda.bpm.bvis.ejb.beans.CustomerServiceBean;
import org.camunda.bpm.bvis.ejb.beans.OrderServiceBean;
import org.camunda.bpm.bvis.ejb.beans.PickUpLocationServiceBean;
import org.camunda.bpm.bvis.entities.Car;
import org.camunda.bpm.bvis.entities.CarPriceMap;
import org.camunda.bpm.bvis.entities.Customer;
import org.camunda.bpm.bvis.entities.Insurance;
import org.camunda.bpm.bvis.entities.InsuranceAnswer;
import org.camunda.bpm.bvis.entities.InsurancePriceMap;
import org.camunda.bpm.bvis.entities.InsuranceType;
import org.camunda.bpm.bvis.entities.PickUpLocation;
import org.camunda.bpm.bvis.entities.RentalOrder;
import org.camunda.bpm.bvis.rest.send.service.SendContractConfirmation;
import org.camunda.bpm.bvis.rest.send.service.SendContractConfirmationClient;
import org.camunda.bpm.bvis.rest.send.service.SendInquiry;
import org.camunda.bpm.bvis.util.SendHTMLEmail;
import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

	@Inject
	private BusinessProcess businessProcess;
	  
	// private static SendHTMLEmail sendMail;

  public void persistOrder(DelegateExecution delegateExecution) throws ParseException {
    // Create new order instance
		System.out.println("Process instance ID " + businessProcess.getProcessInstanceId());
		System.out.println("Execution ID: " + businessProcess.getExecutionId());
    RentalOrder rentalOrder = new RentalOrder();
	
    // Get all process variables
    Map<String, Object> variables = delegateExecution.getVariables();
    Customer customer = new Customer();
    
    customer.setFirstname((String) variables.get("customerFirstname"));
    customer.setSurname((String) variables.get("customerSurname"));
    String customerCompanyName = (String) variables.get("customerCompanyName");
   
    boolean isCompany = true;
    if (customerCompanyName.isEmpty()) {
    	isCompany = false;
    }
    
    customer.setCompanyName(customerCompanyName);
    customer.setCompany(isCompany);
    
    customer.setEmail((String) variables.get("customerEmail"));
    customer.setPhoneNumber((String) variables.get("customerPhoneNumber"));
    System.out.println(variables.get("customerDateOfBirth"));
    customer.setDateOfBirth((Date) variables.get("customerDateOfBirth"));
    customer.setStreet((String) variables.get("customerStreet"));
    customer.setHouseNumber((String) variables.get("customerHouseNumber"));
    customer.setPostcode((String) variables.get("customerPostcode"));
    customer.setCity((String) variables.get("customerCity"));
    customer.setCountry((String) variables.get("customerCountry"));
    customer.setEligibility(false);
   
    customerService.create(customer);
    
    // Set order attributes
    rentalOrder.setCustomer(customer);
    rentalOrder.setRequestDate(new Date());
    
    boolean isFleet = (Boolean) variables.get("fleet");
    if (!isFleet) {
        Date PickUpDate = (Date) variables.get("pickUpDate");
        rentalOrder.setPick_up_date(PickUpDate); 
        Date ReturnDate = (Date) variables.get("returnDate");
        rentalOrder.setReturn_date(ReturnDate);
    
    	Long pickUpLocationId = (Long.parseLong((String)variables.get("pickUpLoc")));
    	Long returnStoreId = (Long.parseLong((String)variables.get("returnStore")));
    
    	rentalOrder.setPickUpStore((PickUpLocation) locationService.getPickUpLocation(pickUpLocationId));
    	rentalOrder.setReturnStore((PickUpLocation) locationService.getPickUpLocation(returnStoreId));
    
    	InsuranceType insuranceType = InsuranceType.valueOf((String) variables.get("insuranceType"));
    	rentalOrder.setInsurance_type((InsuranceType) insuranceType);
    	
        Long carId = (Long.parseLong((String)variables.get("car")));

        Car car = carService.getCar(carId);
        Collection<Car> cars = new ArrayList<Car>();
        cars.add(car);
        
        rentalOrder.setCars((Collection<Car>) cars);
        
        //calculate price for cars
        double price_per_day = 0.0;
        for (Car carX:cars) {
            price_per_day += CarPriceMap.getPrice(carX.getType());
        }
        long time_diff = ReturnDate.getTime() - PickUpDate.getTime();  
        double no_of_days = (double) TimeUnit.DAYS.convert(time_diff, TimeUnit.MILLISECONDS);
        double priceCars = price_per_day * no_of_days;
        
        rentalOrder.setPriceCars(priceCars);    
            
        System.out.println("priceCars: " + priceCars); 
        
        //calculate price for insurance
        double carTypeFactor = InsurancePriceMap.getInsuranceFactor(car.getType());
        double insuranceTypeFactor = InsurancePriceMap.getInsuranceFactor(insuranceType);
        int ps = car.getPs();
        int current_year = Calendar.getInstance().get(Calendar.YEAR);
        int construction_year = car.getConstructionYear();
        int year_diff = current_year - construction_year;
   
        double price_for_insurance = (insuranceTypeFactor * carTypeFactor) + (ps * 0.15) + 20 - Math.pow(1.2, year_diff) ;
        
        System.out.println("expected_price_for_insurance: " + price_for_insurance);
        
        rentalOrder.setPriceInsurance_expected(price_for_insurance);
    }
    rentalOrder.setFleetRental(isFleet);
    rentalOrder.setInquiryText((String) variables.get("inquiryText"));

    rentalOrder.setClerkComments("");
    rentalOrder.setApproveStatus(false);
    
    rentalOrder.setInsurance_ID(0);
    
    // TODO GENERATE A PROPER INSURANCE. THIS IS JUST A DUMMY FOR TESTING PURPOSES
    Insurance insurance = new Insurance();
    insurance.setDeductible(new BigDecimal(1000));
    insurance.setEstimatedCosts(new BigDecimal(20));
    insurance.setPickUpDate((Date)variables.get("pickUpDate"));
    insurance.setReturnDate((Date)variables.get("returnDate"));
    insurance.setOrder(rentalOrder);
    insurance.setType(InsuranceType.partial);
    rentalOrder.setInsurance(insurance);
   
    orderService.create(rentalOrder);
    System.out.println("Cars: " + rentalOrder.getCars());
    
    // Remove no longer needed process variables
    delegateExecution.removeVariables(variables.keySet());

    // Add newly created order id as process variable
    delegateExecution.setVariable("orderId", rentalOrder.getId());
    delegateExecution.setVariable("fleet", rentalOrder.isFleetRental());
    delegateExecution.setVariable("processId", delegateExecution.getActivityInstanceId());
    System.out.println("CREATED ORDER WITH ORDER ID: " + rentalOrder.getId());
  }

  public RentalOrder getOrder(Long orderId) {
	    // Load order entity from database
	    return orderService.getOrder(orderId);
	  }
  
  public boolean isFleetOrder(DelegateExecution delegateExecution) {
	    Map<String, Object> variables = delegateExecution.getVariables();
	    boolean isFleetOrder = (Boolean)variables.get("fleet");
	    return isFleetOrder;  
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
      
      System.out.println("FLEET");
      System.out.println(isFleetRental);
      
      from = "bvis@bvis.com";
      if (!isFleetRental)
      {
    	        rentalStart = order.getPick_up_date().toString();
      rentalEnd = order.getReturn_date().toString();
      pickupLocation = order.getPickUpStore().getHTMLContactDetails();    
      returnLocation = order.getReturnStore().getHTMLContactDetails();
      clerkComment = order.getClerkComments();
      carModel = "";
      for (Car loop_car : cars){
    	  carModel += loop_car.getHTMLCarDetails() + "<br>";
      }
      }
      
      subject = "";
      
      path = "/templates/";
      
      //built email template path by state
      switch(state){
  			case "canc_fleet": path += "canc_fleet.txt"; subject = "We are sorry... (No. " + orderId_str + ")" ; break;
  			case "canc_single": path += "canc_single.txt"; subject = "We are sorry... (No. " + orderId_str + ")" ; break;
  			case "conf_req":
  				if (isFleetRental)
					{ 	path += "conf_req_fleet.txt";
						subject = "Booking reservation (No. " + orderId_str + ")"; 
						break; }
				else if (!isFleetRental)
					{	path += "conf_req_single.txt";
						subject = "Booking reservation (No. " + orderId_str + ")";
						break; }
				break;
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
	  System.out.println("SENDING TO CAPITOL INITIALIZED");
	  SendInquiry sender = new SendInquiry();
	  RentalOrder entityOrder  = orderService.getOrder((Long) businessProcess.getVariable("orderId"));
	  String result = sender.sendInquiry(entityOrder, delegateExecution.getProcessInstanceId());
	  System.out.println("SENDING DONE. INSURANCE API RESPONSE: " + result);
	  //TODO handle failures
  }
  
  public boolean handleInsuranceResponse(DelegateExecution delegateExecution) {
	  Map<String, Object> variables = delegateExecution.getVariables();
	  int orderID = Integer.parseInt(variables.get("orderID").toString());
	  RentalOrder order = orderService.getOrder(orderID);
	  Insurance insurance = order.getInsurance();
	  insurance.setActualCosts(new BigDecimal(Double.
			  parseDouble(variables.get("finalPrice").toString())));
	  insurance.setInquiryText(variables.get("inquiryText").toString());
	  int insuranceAnswer = Integer.parseInt(variables.get("insuranceResult").toString());
	  if (insuranceAnswer == 0) {
		  insurance.setInsuranceAnswer(InsuranceAnswer.REJECTED);
		  System.out.println("INSURANCE REJECTED");
	  }
	  else if (insuranceAnswer == 1) {
		  insurance.setInsuranceAnswer(InsuranceAnswer.ACCEPTED);
		  System.out.println("INSURANCE ACCEPTED");
	  }
	  else {
		  insurance.setInsuranceAnswer(InsuranceAnswer.ADJUSTED);
		  System.out.println("INSURANCE ADJUSTED");
	  }
	  order.setInsurance(insurance);
	  delegateExecution.removeVariables();
	  orderService.updateOrder(order);
	  // return true to trigger continuation
	  return true;
  }
  
  public void sendContractConfirmation(DelegateExecution delegateExecution) {
	  Map<String, Object> variables = delegateExecution.getVariables();
	  int orderID = Integer.parseInt(variables.get("orderID").toString());
	  RentalOrder order = orderService.getOrder(orderID);
	  SendContractConfirmation client = new SendContractConfirmation();
	  String processInstanceIDBVIS = (String)variables.get("processId");
	  String processInstanceIDCapitol = (String)variables.get("processIdCapitol");
	  client.sendContractConfirmation(order, processInstanceIDBVIS, processInstanceIDCapitol, 1); //TODO insert proper contract status
	  
  }
  
  public void sendReminder() {
	  //TODO insert business logic
	  //TODO create form
  }
}


