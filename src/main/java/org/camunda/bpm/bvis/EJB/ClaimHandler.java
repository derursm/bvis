package org.camunda.bpm.bvis.EJB;

import java.math.BigDecimal;
import java.util.Map;

import javax.ejb.EJB;

//import java.util.logging.Logger;

import javax.ejb.Stateless;
//import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.Entities.Claim;
import org.camunda.bpm.bvis.Entities.ClaimReview;
import org.camunda.bpm.bvis.Entities.ClaimStatus;
import org.camunda.bpm.bvis.Entities.Customer;
import org.camunda.bpm.bvis.Entities.InsuranceAnswer;
import org.camunda.bpm.bvis.Entities.RentalOrder;
import org.camunda.bpm.bvis.rest.send.service.SendClaim;
import org.camunda.bpm.bvis.rest.send.service.SendClaimReview;
//import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;

@Stateless
public class ClaimHandler {
	
	@PersistenceContext
	protected EntityManager em;
	
	@EJB
	private ClaimServiceBean claimService;
	
	// Inject task form available through the camunda cdi artifact
	//@Inject
	//private TaskForm taskForm;
	
	//private static Logger LOGGER = Logger.getLogger(ContractHandler.class.getName());
	
	public void persistClaim(DelegateExecution delegateExecution) {
		
		//Claim claim = new Claim();
		
		/*
		 * ....
		 */
	}
	
	public Customer getUser(long claimID) {
		return null;
	}
	
	public void informUser(long claimID) {
		
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
		int claimID = (Integer)variables.get("claimID");
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
		  if (insuranceDecision == 0) {
			  claim.setClaim_status(ClaimStatus.REJECTED);
			  System.out.println("CLAIM REJECTED");
		  }
		  else if (insuranceDecision == 1) {
			  claim.setClaim_status(ClaimStatus.ACCEPTED);
			  System.out.println("CLAIM ACCEPTED");
		  }
		  else {
			  claim.setClaim_status(ClaimStatus.ADJUSTED);
			  System.out.println("CLAIM ADJUSTED");
		  }
		claimService.updateClaim(claim);
		//return true to trigger continuation
		return true;
	}
	
	public void sendClaimReview(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		int claimReviewID = (Integer)variables.get("claimReviewID");
		ClaimReview claimReview = claimService.getClaimReview(claimReviewID);
		SendClaimReview sender = new SendClaimReview();
		String result = sender.sendClaimReview(claimReview, delegateExecution.getActivityInstanceId());
		System.out.println("SENDING DONE. INSURANCE API RESPONSE: " + result);
		//TODO handle failures		
	}
}
