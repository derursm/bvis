package org.camunda.bpm.bvis.EJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.Entites.Order;

@Stateless
public class OrderServiceBean {
	@PersistenceContext
	protected EntityManager em;
	
	public Order create(Order order) {
		em.persist(order);
		em.flush();
		return order;
	}
}
