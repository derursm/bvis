package org.camunda.bpm.bvis.ejb.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.camunda.bpm.bvis.entities.Claim;
import org.camunda.bpm.bvis.entities.ClaimInsurance;
import org.camunda.bpm.bvis.entities.ClaimReview;


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
	
	public ClaimReview createClaimReview(ClaimReview claimReview) {
		em.persist(claimReview);
		em.flush();
		return claimReview;
	}
	
	public ClaimReview getClaimReview(long id) {
		return em.find(ClaimReview.class, id);
	}
	
	public void updateClaimReview(ClaimReview claimReview) {
		em.merge(claimReview);
	}
	
	public void createClaimInsurance(ClaimInsurance insurance) {
		em.persist(insurance);
	}
}
