package org.camunda.bpm.bvis.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import org.camunda.bpm.bvis.entities.ClaimReview;
import org.camunda.bpm.bvis.entities.ClaimStatus;
import org.camunda.bpm.bvis.entities.Customer;
import org.camunda.bpm.bvis.entities.Insurance;
import org.camunda.bpm.bvis.entities.InsuranceAnswer;
import org.camunda.bpm.bvis.entities.InvolvedParty;
import org.camunda.bpm.bvis.entities.RentalOrder;
import org.camunda.bpm.bvis.rest.send.service.SendClaim;
import org.camunda.bpm.bvis.rest.send.service.SendClaimReview;
import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.cdi.jsf.TaskForm;
//import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;

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
		RentalOrder order = orderService.getOrder((long)variables.get("orderID"));
		Insurance insurance = order.getInsurance();
		claim.setInsurance(insurance);
		InvolvedParty party = new InvolvedParty();
		party.setCity((String)variables.get("party1City"));
		party.setCompany((String)variables.get("party1Company"));
		party.setCountry((String)variables.get("party1Country"));
		party.setDate_of_birth((Date)variables.get("party1Birthday"));
		party.setEmail((String)variables.get("Party1EMail"));
		party.setFirstname((String)variables.get("party1Firstname"));
		party.setPhone_number((String)variables.get("party1Phone"));
		party.setPostcode((String)variables.get("party1ZIP"));
		party.setStreet((String)variables.get("party1Street"));
		party.setStreetNo((String)variables.get("party1StreetNo"));
		party.setSurname((String)variables.get("party1Surname"));
		// only adds one party for now
		ArrayList<InvolvedParty> parties = new ArrayList<InvolvedParty>();
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
		return claim.isReportedByCustomer();
	}
	
	public boolean towingServiceNeeded(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		Claim claim = claimService.getClaim((long)variables.get("claimID"));
		return claim.isTowingServiceNeeded();
	}
	
	public boolean insertWorkshopBill(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		double repairBill = Double.parseDouble((String)variables.get("repairBill"));
	    Claim claim = claimService.getClaim((Long)variables.get("claimID"));
	    claim.setWorkshopPrice(new BigDecimal(repairBill));
	    claimService.updateClaim(claim);
	    return true;
	}
	
	public Customer getUser(long claimID) {
		return null;
	}
	
	public void informUser(long claimID) {
		
	}
	
	public void sendClaimStateNotification(DelegateExecution delegateExecution) {
		
	}
	
	public void informTowingService(long claimID) {
		
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
		SendClaim sender = new SendClaim();
		String result = sender.sendClaim(claim, delegateExecution.getActivityInstanceId());
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
		claimReview.setClaimStatus((int)variables.get("bvisAnswer"));
		claimReview.setProcessIDCapitol((String)variables.get("processIDCapitol"));
		SendClaimReview sender = new SendClaimReview();
		String result = sender.sendClaimReview(claimReview, delegateExecution.getActivityInstanceId());
		System.out.println("SENDING DONE. INSURANCE API RESPONSE: " + result);
		//TODO handle failures		
	}
	
	public boolean claimDecisionAccepted(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		if ((int)variables.get("bvisAnswer") == 1) return true;
		else return false;
	}
}
