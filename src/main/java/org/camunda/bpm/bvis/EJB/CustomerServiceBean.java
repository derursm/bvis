package org.camunda.bpm.bvis.EJB;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.camunda.bpm.bvis.Entities.Customer;

@Stateless
public class CustomerServiceBean {
	@PersistenceContext
	protected EntityManager em;
	
	public Customer create(Customer customer) {
		em.persist(customer);
		return customer;
	}
	
	public Collection<Customer> getAllCustomers() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> rootEntry = cq.from(Customer.class);
		return em.createQuery(cq.select(rootEntry)).getResultList();
	}
	
	public Customer getCustomer(long id) {
		return em.find(Customer.class, id);
	}
}
