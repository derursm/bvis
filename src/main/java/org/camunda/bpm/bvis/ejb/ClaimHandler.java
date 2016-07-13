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
			party.setClaim(claim);
			// only adds one party for now
			parties.add(party);
		}
 		claim.setInvolvedParties(parties);
		claim.setRentalOrder(order);
		System.out.println("1NUMBER OF INVOLVED PARTIES " + claim.getInvolvedParties().size());
		claimService.createClaim(claim);
		
	    // Remove no longer needed process variables
	    delegateExecution.removeVariables(variables.keySet());

	    // Add newly created claim id as process variable
	    delegateExecution.setVariable("claimID", claim.getClaimID());
	    System.out.println("CREATED CLAIM WITH CLAIM ID: " + claim.getClaimID());
		System.out.println("2NUMBER OF INVOLVED PARTIES " + claim.getInvolvedParties().size());
	}
	
	public boolean informedByCustomer(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		Claim claim = claimService.getClaim((long)variables.get("claimID"));
		System.out.println("3NUMBER OF INVOLVED PARTIES " + claim.getInvolvedParties().size());
		return claim.isReportedByCustomer();
	}
	
	public boolean towingServiceNeeded(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		Claim claim = claimService.getClaim((long)variables.get("claimID"));
		System.out.println("4NUMBER OF INVOLVED PARTIES " + claim.getInvolvedParties().size() + " FOR CLAIM ID " + variables.get("claimID"));
		return claim.isTowingServiceNeeded();
	}
	
	public boolean insertWorkshopBill(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		double repairBill = Double.parseDouble((String)variables.get("repairBill"));
	    Claim claim = claimService.getClaim((long)variables.get("claimID"));
		System.out.println("5NUMBER OF INVOLVED PARTIES " + claim.getInvolvedParties().size() + " FOR CLAIM ID " + variables.get("claimID"));
	    claim.setWorkshopPrice(new BigDecimal(repairBill));
	    claimService.updateClaim(claim);
		System.out.println("6NUMBER OF INVOLVED PARTIES " + claim.getInvolvedParties().size());
	    return true;
	}
	
	public Customer getUser(long claimID) {
		return null;
	}
	
	public void informCustomerAboutDamage(DelegateExecution delegateExecution) {
		
	}
	
	public void sendClaimStateNotification(DelegateExecution delegateExecution) {
		
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
	
	/**
	 * Helper method to decide the flow in the BPMN model for the final customer contact depending on the insurance answer
	 * @param delegateExecution
	 * @return
	 */
	public int rewordDecision(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		// 0 = customer has to pay
		if ((int)variables.get("customerCosts") > 0) return 0;
		// insurance pays
		else if ((int)variables.get("coverageCosts") > 0) return 1;
		// else if insurance does not cover costs and customer does not have to pay -> third party has to pay
		else return 2;
	}
	
	/**
	 * Either inform customer about 
	 * 1) He has to pay
	 * 2) Insurance pays
	 * 3) Third party has to pay
	 * @param delegateExecution
	 */
	public void sendFinalCustomerNotification(DelegateExecution delegateExecution) {
		
	}
}
