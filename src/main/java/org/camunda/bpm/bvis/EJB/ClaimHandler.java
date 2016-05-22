package org.camunda.bpm.bvis.EJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.Entites.Claim;
import org.camunda.bpm.bvis.Entites.Customer;

@Stateless
public class ClaimHandler {
	
	@PersistenceContext
	protected EntityManager em;
	
	public Customer getUser(long claimID) {
		return null;
	}
	
	public void informUser(long userID) {
		
	}
	
	public Claim getClaim(long ClaimID) {
		return null;
	}
	
	public void initiateRepair(Claim claim) {
		
	}
	
	public void sendClaimDetails(Claim claim) {
		
	}

}
