package org.camunda.bpm.bvis.ejb.beans;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.camunda.bpm.bvis.entities.ClaimInsurance;
import org.camunda.bpm.bvis.entities.Customer;

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
	
	public Customer getCustomerByUsernameAndPassword(String username, String password) {
		final String querystring = "SELECT c FROM Customer c WHERE c.username = :username AND c.password = :password";
		TypedQuery<Customer> query = em.createQuery(querystring, Customer.class);
		query.setParameter("username", username);
		query.setParameter("password", password);
		if (query.getResultList().size() == 0) return null;
		else return query.getResultList().get(0);
	}
	
	public Customer getCustomerByUsername(String username) {
		final String querystring = "SELECT c FROM Customer c WHERE c.username = :username";
		TypedQuery<Customer> query = em.createQuery(querystring, Customer.class);
		query.setParameter("username", username);
		if (query.getResultList().size() == 0) return null;
		else return query.getResultList().get(0);
	}
	
	public Customer getCustomerByEmail(String email) {
		final String querystring = "SELECT c FROM Customer c WHERE c.email = :email";
		TypedQuery<Customer> query = em.createQuery(querystring, Customer.class);
		query.setParameter("email", email);
		if (query.getResultList().size() == 0) return null;
		else return query.getResultList().get(0);
	}
}
