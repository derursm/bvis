package org.camunda.bpm.bvis.EJB;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.Entites.Claim;
import org.camunda.bpm.bvis.Entites.Customer;
import org.camunda.bpm.bvis.Entites.RentalOrder;
import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;

@Stateless
public class ClaimHandler {
	
	@PersistenceContext
	protected EntityManager em;
	
	// Inject task form available through the camunda cdi artifact
	@Inject
	private TaskForm taskForm;
	
	private static Logger LOGGER = Logger.getLogger(ContractHandler.class.getName());
	
	public void persistClaim(DelegateExecution delegateExecution) {
		
		Claim claim = new Claim();
		
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
	
	public void sendClaimDetails(Claim claim) {
		
	}

}
