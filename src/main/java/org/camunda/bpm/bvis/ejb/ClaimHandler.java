package org.camunda.bpm.bvis.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.Map;

import javax.ejb.EJB;

//import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
//import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.ejb.beans.CarServiceBean;
import org.camunda.bpm.bvis.ejb.beans.ClaimServiceBean;
import org.camunda.bpm.bvis.ejb.beans.InsuranceServiceBean;
import org.camunda.bpm.bvis.ejb.beans.OrderServiceBean;
import org.camunda.bpm.bvis.entities.Car;
import org.camunda.bpm.bvis.entities.Claim;
import org.camunda.bpm.bvis.entities.ClaimInsurance;
import org.camunda.bpm.bvis.entities.ClaimReview;
import org.camunda.bpm.bvis.entities.ClaimStatus;
import org.camunda.bpm.bvis.entities.Customer;
import org.camunda.bpm.bvis.entities.Insurance;
import org.camunda.bpm.bvis.entities.InsuranceAnswer;
import org.camunda.bpm.bvis.entities.InvolvedParty;
import org.camunda.bpm.bvis.entities.RentalOrder;
import org.camunda.bpm.bvis.rest.send.service.SendClaim;
import org.camunda.bpm.bvis.rest.send.service.SendClaimReview;
import org.camunda.bpm.bvis.util.SendHTMLEmail;
import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.cdi.jsf.TaskForm;
//import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;



@Stateless
@Named
public class ClaimHandler {
	
	@PersistenceContext
	protected EntityManager em;
	
	
	// Inject task form available through the Camunda cdi artifact
	@Inject
	private TaskForm taskForm;

	@Inject
	private BusinessProcess businessProcess;
	
	@EJB
	private CarServiceBean carService;
	@EJB
	private ClaimServiceBean claimService;
	@EJB
	private InsuranceServiceBean insuranceService;
	@EJB
	private OrderServiceBean orderService;
	
	// Inject task form available through the camunda cdi artifact
	//@Inject
	//private TaskForm taskForm;
	
	//private static Logger LOGGER = Logger.getLogger(ContractHandler.class.getName());
	
	public void persistClaim(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		Claim claim = new Claim();
		
		// Check whether car exist has been done already
		Car car = carService.getCarByVehicleIdentificationNumber((String)variables.get("vehicleID"));
		claim.setCar(car);
		claim.setClaim_status(ClaimStatus.NOT_SEND_YET);
		claim.setClaimDescription((String)variables.get("damageDescription"));
		claim.setDamageDate((Date)variables.get("damageDate"));
		claim.setTowingServiceNeeded((Boolean)variables.get("towingServiceNeeded")); 
		claim.setReportedByCustomer((Boolean)variables.get("reportedByCustomer"));
		RentalOrder order = orderService.getOrder(Long.parseLong(variables.get("orderID")+""));
		Insurance insurance = order.getInsurance();
		claim.setInsurance(insurance);
		
		// check if there is a party involved
		ArrayList<InvolvedParty> parties = new ArrayList<InvolvedParty>();
		InvolvedParty party = new InvolvedParty();
		if (variables.get("party1Surname") != null) {
			party.setCity((String)variables.get("party1City"));
			party.setCompany((String)variables.get("party1Company"));
			party.setCountry((String)variables.get("party1Country"));
			party.setDate_of_birth((Date)variables.get("party1Birthday"));
			party.setEmail((String)variables.get("party1EMail"));
			party.setFirstname((String)variables.get("party1Firstname"));
			party.setPhone_number((String)variables.get("party1Phone"));
			party.setPostcode((String)variables.get("party1ZIP"));
			party.setStreet((String)variables.get("party1Street"));
			party.setSurname((String)variables.get("party1Surname"));
			party.setHouse_number((String)variables.get("party1HouseNo"));
			// find claim insurance data
			ClaimInsurance claimInsurance = insuranceService.getClaimInsuranceByName((String)variables.get("party1Insurance"));
			party.setClaimInsurance(claimInsurance);
			// set claim because somehow hibernate gets the mapping from
			party.setClaim(claim);
			// only adds one party for now
			parties.add(party);
		}
 		claim.setInvolvedParties(parties);
		claim.setRentalOrder(order);
		claimService.createClaim(claim);
		
	    // Remove no longer needed process variables
	    delegateExecution.removeVariables(variables.keySet());

	    // Add newly created claim id as process variable
	    delegateExecution.setVariable("claimID", claim.getClaimID());
	    System.out.println("CREATED CLAIM WITH CLAIM ID: " + claim.getClaimID());
	}
	
	public boolean informedByCustomer(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		Claim claim = claimService.getClaim((long)variables.get("claimID"));
		System.out.println("Claim informed by customer?:" + claim.isReportedByCustomer());
		return claim.isReportedByCustomer();
	}
	
	public boolean towingServiceNeeded(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		Claim claim = claimService.getClaim((long)variables.get("claimID"));
		return claim.isTowingServiceNeeded();
	}
	
	public boolean insertWorkshopBill(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		double repairBill = (double)variables.get("repairBill");
	    Claim claim = claimService.getClaim((long)variables.get("claimID"));
	    claim.setWorkshopPrice(new BigDecimal(repairBill));
	    claimService.updateClaim(claim);
	    return true;
	}
	
	public Customer getUser(long claimID) {
		return null;
	}
	
	public void informCustomerAboutDamage(DelegateExecution delegateExecution) {
		
	}
	
	   public void sendClaimStateNotification(DelegateExecution delegateExecution)throws ParseException{
           
	          //RentalOrder order;
	          Customer customer;
	          Claim claim;
	          Car car;
	          RentalOrder order;
	          
	          String surname, subject, text, textCss, from, email, state, path, pathCss, pickupLocation, returnLocation,
	          insurancePac, carModel, rentalEnd, rentalStart, claimId_str, orderId_str, clerkComment, towingAddress, vehicleIdent;
	          Long claimId, orderId;
	          //tbc..
	              
	          // Get all process variables
	          Map<String, Object> variables = delegateExecution.getVariables();
	          claimId = (long) variables.get("claimID");
	          state = variables.get("state").toString();
	          claim = claimService.getClaim(claimId);
	          car = claim.getCar();
	          order = claim.getRentalOrder();
	          customer = order.getCustomer();
	               
	                
	          surname = "surname";
	          email = "email";
	          from = "from";
	          pickupLocation = "pickupLocation"; //order.getPick_up_store().getStoreName() + order.getPick_up_store().getCity();
	          returnLocation = "returnLocation"; //order.getReturn_store().getStoreName() + order.getReturn_store().getCity();
	          carModel = "carmodel"; //order.getCars();
	          rentalStart = "rentalStart";
	          rentalEnd = "rentalEnd";
	          claimId_str = "claimId";
	          towingAddress = "towingAddress";
	          insurancePac = "insurancePac";
	          vehicleIdent = " ";
	            
	          
	          surname = customer.getSurname();
	          email = customer.getEmail();
	          from = "bvis@bvis.com";
	          
	          //rentalStart = order.getPick_up_date().toString();
	          //rentalEnd = order.getReturn_date().toString();
	          //pickupLocation = order.getPickUpStore().getHTMLContactDetails();    
	          //returnLocation = order.getReturnStore().getHTMLContactDetails();
	          clerkComment = order.getClerkComments();
	          carModel = car.getHTMLCarDetails();         
	                  
	          //Get rental information
	          claimId_str = claimId.toString();
	          vehicleIdent = car.getVehicleIdentificationNumber();
	          //check for special recepient, else take customer mail
	          try{
	              email = (String) variables.get("receiver");
	          } catch (NullPointerException  e){
	              email = customer.getEmail();
	          }
	          
	          orderId = order.getId();
	          orderId_str = orderId.toString();
	          
	          from = "bvis@bvis.com";
	                      
	          subject = "";
	          
	          path = "/templates/";
	          pathCss = "/templates/css.txt";
	          
	          System.out.println("Email sent status: " + state);        
	          
	          //built email template path by state
	          switch(state){
	                case "inf_costs": path += "inf_costs.txt"; subject = "Claim settled (Claim No. " + claimId_str + ")" ; break;
	                case "inf_damage": path += "inf_damage.txt"; subject = "Damage found (Claim No. " + claimId_str + ")" ; break;
	                case "init_repair": 
	                    if(claim.isTowingServiceNeeded()){
	                        path += "init_repair_with_towing.txt";
	                    }else {
	                        path += "init_repair.txt";
	                    }
	                    subject = "New order (Claim No. " + claimId_str + ")" ; break;                          
	                case "init_towing": path += "init_towing.txt"; subject = "New order (Claim No. " + claimId_str + ")" ; break;
	                case "send_conf": path += "send_conf.txt"; subject = "Claim settled (Claim No. " + claimId_str + ")" ; break;
	                case "send_inv": path += "send_inv.txt"; subject = "Invoice (Claim No. " + claimId_str + ")" ; break;
	          }   
	                          
	          InputStream in = this.getClass().getResourceAsStream(path);
	          InputStream inCss = this.getClass().getResourceAsStream(pathCss);
	          try{
	              text = IOUtils.toString(in, "UTF-8");  
	               textCss = IOUtils.toString(inCss, "UTF-8"); 
	          } catch (IOException e) {
	              text = "error in file reading. path: " + path;
	              textCss= "";
	              email = "bvis@bvis.com";
	          } catch (NullPointerException  e){
	              text = "null pointer file reading. path: " + path;
	              textCss= "";
	              email = "bvis@bvis.com";
	          } 
	          
	          try{
	          text = String.format(text, surname, carModel, pickupLocation, rentalStart, returnLocation, rentalEnd, orderId_str, towingAddress, insurancePac, vehicleIdent);  
	          } catch( IllegalFormatException e){   
	             subject = "illegal conversion ";      
	             email = "bvis@bvis.com";          
	          }
	          SendHTMLEmail.main(subject, textCss+text , from, email);
	      }
	public void informTowingService(DelegateExecution delegateExecution) {
		
	}
	
	public Claim getClaim(long ClaimID) {
		return null;
	}
	
	public void initiateRepair(long claimID) {
		
	}
	
	public void sendClaimDetails(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		long claimID = (long)variables.get("claimID");
		Claim claim = claimService.getClaim(claimID);
		System.out.println("INITIATING SENDING CLAIM FOR ORDER " + claim.getRentalOrder().getOrderID());
		SendClaim sender = new SendClaim();
		System.out.println("Sending inquiry with vehicle identification number: " + claim.getRentalOrder().getCars().
				iterator().next().getVehicleIdentificationNumber());
		String result = sender.sendClaim(claim, delegateExecution.getProcessInstanceId());
		System.out.println("SENDING DONE. INSURANCE API RESPONSE: " + result);
		//TODO handle failures		
	}

	public boolean handleInsuranceReponse(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		long claimID = (Long)variables.get("claimID");
		Claim claim = claimService.getClaim(claimID);
		claim.setCostsCoverage(new BigDecimal((Double)variables.get("coverageCosts")));
		claim.setCustomerCosts(new BigDecimal((Double)variables.get("customerCosts")));
		int insuranceDecision = ((Integer)variables.get("insuranceDecision"));
		variables.remove("insuranceDecision");
		  if (insuranceDecision == 0) {
			  claim.setClaim_status(ClaimStatus.REJECTED);
			  System.out.println("CLAIM REJECTED");
			  variables.put("insuranceDecision", ClaimStatus.REJECTED.toString());
		  }
		  else if (insuranceDecision == 1) {
			  claim.setClaim_status(ClaimStatus.ACCEPTED);
			  System.out.println("CLAIM ACCEPTED");
			  variables.put("insuranceDecision", ClaimStatus.ACCEPTED.toString());
		  }
		  else {
			  claim.setClaim_status(ClaimStatus.ADJUSTED);
			  System.out.println("CLAIM ADJUSTED");
			  variables.put("insuranceDecision", ClaimStatus.ADJUSTED.toString());
		  }
		claimService.updateClaim(claim);
		//return true to trigger continuation
		return true;
	}
	
	public void sendClaimReview(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		ClaimReview claimReview = new ClaimReview();
		Claim claim = claimService.getClaim((long)variables.get("claimID"));
		claimReview.setClaim(claim);
		claimReview.setRemarks((String)variables.get("remarks"));
		claimReview.setClaimStatus(Integer.parseInt(variables.get("bvisAnswer")+""));
		claimReview.setProcessIDCapitol((String)variables.get("processIDCapitol"));
		SendClaimReview sender = new SendClaimReview();
		String result = sender.sendClaimReview(claimReview, delegateExecution.getProcessInstanceId());
		System.out.println("SENDING DONE. INSURANCE API RESPONSE: " + result);
		//TODO handle failures		
	}
	
	public boolean claimDecisionAccepted(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		if (Integer.parseInt(variables.get("bvisAnswer")+"") == 1) return true;
		else return false;
	}
	
	/**
	 * Helper method to decide the flow in the BPMN model for the final customer contact depending on the insurance answer
	 * @param delegateExecution
	 * @return
	 */
	public int reworkDecision(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		System.out.println("Customer costs: " + variables.get("customerCosts"));
		System.out.println("Costs covered: " + variables.get("coverageCosts"));
		// 0 = customer has to pay
		if (Double.parseDouble(variables.get("customerCosts")+"") > 0) return 0;
		// insurance pays
		else if (Double.parseDouble(variables.get("coverageCosts")+"") > 0) return 1;
		// else if insurance does not cover costs and customer does not have to pay -> third party has to pay
		else return 2;
	}
	
	// Create contract and send to user's email
    public void sendInvoice(DelegateExecution delegateExecution) {
        System.out.println("Start creating claim pdf");
        RentalOrder order;
        Customer customer;
        Claim claim;
        Collection<Car> cars;
        String surname, email, insurancePac, date,
                street, city, orderId_str;
        BigDecimal workshopPrice, customerPrice, customerCosts, costsCoverage;
        Long claimID, orderId;
        Integer insurance_decision;
        // Get all process variables
        Map<String, Object> variables = delegateExecution.getVariables();
        claimID = (long) variables.get("claimID");
        claim = claimService.getClaim(claimID);
        order = claim.getRentalOrder();
        orderId = order.getOrderID();
        customer = order.getCustomer();
        cars = order.getCars();
        workshopPrice = claim.getWorkshopPrice();
        customerCosts = claim.getCustomerCosts();
        costsCoverage = claim.getCostsCoverage();
        insurance_decision = claim.getInsurance_decision();
        
        surname = "surname";
        email = "email";
        orderId_str = "orderId";
        insurancePac = "insurancePac";
        workshopPrice = BigDecimal.valueOf(0);
        customerCosts = BigDecimal.valueOf(0);
        costsCoverage = BigDecimal.valueOf(0);
        
        orderId_str = orderId.toString();
        surname = customer.getSurname();
        street = customer.getStreet() + " " + customer.getHouseNumber();
        city = customer.getPostcode() + " " + customer.getCity();
        email = customer.getEmail();
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        date = formatter.format(today);
        // surname, email, pickupLocation, returnLocation,
        // insurancePac, carModel, rentalEnd, rentalStart, orderId_str, date,
        // type, street, city;
        // double totalPrice, insurancePrice, rentalPrice;
        // Long orderId;
        final String[][] rentalData = { { "Your insurance:", insurancePac }, 
                { "Repair costs:", workshopPrice.toString() },
                { "Costs covered by insurance:", costsCoverage.toString() },
                { "Billing amount:", customerCosts.toString()}
                };
        try {
            Document document = new Document();
            ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
            PdfWriter pdf = null;
            pdf = PdfWriter.getInstance(document, baosPDF);
            document.open();
            Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC);
            Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
            Paragraph pa0 = new Paragraph("Order ID: " + orderId_str, paragraphFont);
            pa0.setAlignment(Element.ALIGN_RIGHT);
            pa0.setFont(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
            document.add(pa0);
            Chunk chunk = new Chunk("BVIS Car Rental", chapterFont);
            document.add(chunk);
            Paragraph pa1 = new Paragraph("Claim Invoice", paragraphFont);
            pa1.setSpacingAfter(15);
            document.add(pa1);
            List list = new List();
            list.setListSymbol("");
            list.add(new ListItem("Mr/Mrs " + surname));
            list.add(new ListItem(street));
            list.add(new ListItem(city));
            Paragraph pa2 = new Paragraph();
            pa2.add(list);
            pa2.setSpacingAfter(20);
            document.add(pa2);
            Paragraph datePar = new Paragraph(date, paragraphFont);
            datePar.setAlignment(Element.ALIGN_RIGHT);
            document.add(datePar);
            document.add(new Paragraph("Dear Mr/Mrs " + surname + ",", paragraphFont));
            Paragraph pa3 = new Paragraph(
                    "we are kindly asking to settle the bill regarding your claim:",
                    paragraphFont);
            pa3.setSpacingAfter(10);
            document.add(pa3);
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(80);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.setWidths(new int[] { 5, 10 });
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.addCell(rentalData[0][0]);
            table.addCell(rentalData[0][1]);
            table.addCell(rentalData[1][0]);
            table.addCell(rentalData[1][1]);
            table.addCell(rentalData[2][0]);
            table.addCell(rentalData[2][1]);
            table.addCell(rentalData[3][0]);
            table.addCell(rentalData[3][1]);
            
            Paragraph pa4 = new Paragraph();
            pa4.add(table);
            pa4.setSpacingAfter(15);
            document.add(pa4);
            document.add(new Paragraph("Thank you!", paragraphFont));
            document.close();
            pdf.close();
            SendHTMLEmail.mainAtt("Claim Invoice (Order no." + orderId_str + ")",
                    "Dear Mr/Mrs " + surname + ". <br> Please find attached your claim invoice.", "bvis@test.de", email,
                    baosPDF, "bvis_claim_invoice" + surname);
        } catch (DocumentException e) {
            System.out.println("Claim DocumentException");
        }
    }
}
