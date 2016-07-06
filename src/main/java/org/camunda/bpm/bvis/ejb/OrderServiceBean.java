package org.camunda.bpm.bvis.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.entities.RentalOrder;

@Stateless
public class OrderServiceBean {
	@PersistenceContext
	protected EntityManager em;
	
	public RentalOrder create(RentalOrder rentalOrder) {
		em.persist(rentalOrder);
		em.flush();
		return rentalOrder;
	}
	
	public RentalOrder getOrder(long id) {
		return em.find(RentalOrder.class, id);
	}
	
	public void updateOrder(RentalOrder rentalOrder) {
		em.merge(rentalOrder);
	}
}
