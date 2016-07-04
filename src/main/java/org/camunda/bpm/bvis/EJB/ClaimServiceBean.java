package org.camunda.bpm.bvis.EJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.Entities.Claim;

@Stateless
public class ClaimServiceBean {
	@PersistenceContext
	protected EntityManager em;
	
	public Claim createClaim(Claim claim) {
		em.persist(claim);
		em.flush();
		return claim;
	}
	
	public Claim getClaim(long id) {
		return em.find(Claim.class, id);
	}
	
	public void updateClaim(Claim claim) {
		em.merge(claim);
	}
	
	public ClaimReview createClaimReview(Claim claim) {
		em.persist(claim);
		em.flush();
		return claim;
	}
	
	public Claim getClaim(long id) {
		return em.find(Claim.class, id);
	}
	
	public void updateClaim(Claim claim) {
		em.merge(claim);
	}
}
