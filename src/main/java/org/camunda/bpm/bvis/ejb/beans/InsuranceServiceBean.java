package org.camunda.bpm.bvis.ejb.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.camunda.bpm.bvis.entities.Car;
import org.camunda.bpm.bvis.entities.ClaimInsurance;
import org.camunda.bpm.bvis.entities.Insurance;

/**
 * 
 * Handles insurance companies (ClaimInsurance) as well as insurance policies (Insurance)
 *
 */
@Stateless
public class InsuranceServiceBean {
	@PersistenceContext
	protected EntityManager em;
	
	public Insurance createInsurance(Insurance insurance) {
		em.persist(insurance);
		em.flush();
		return insurance;
	}
	
	public ClaimInsurance createClaimInsurance(ClaimInsurance claimInsurance) {
		em.persist(claimInsurance);
		em.flush();
		return claimInsurance;
	}
	
	public Insurance getInsurance(long id) {
		Insurance insurance = em.find(Insurance.class, id);
		return insurance;
	}
	
	public ClaimInsurance getClaimInsurance(long id) {
		ClaimInsurance insurance = em.find(ClaimInsurance.class, id);
		return insurance;
	}	
	
	public ClaimInsurance getClaimInsuranceByName(String name) {
		final String querystring = "SELECT i FROM ClaimInsurance i WHERE i.company = :name";
		TypedQuery<ClaimInsurance> query = em.createQuery(querystring, ClaimInsurance.class);
		query.setParameter("name", name);
		return query.getResultList().get(0);
	}
	
	public void updateClaimInsurance(ClaimInsurance insurance) {
		em.merge(insurance);
	}
	
	public boolean insuranceExists(String name) {
		final String querystring = "SELECT i FROM ClaimInsurance i WHERE i.company = :name";
		TypedQuery<ClaimInsurance> query = em.createQuery(querystring, ClaimInsurance.class);
		query.setParameter("name", name);
		return (query.getResultList().size() > 0);
	}
}
