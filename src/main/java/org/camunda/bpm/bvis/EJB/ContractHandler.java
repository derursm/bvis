package org.camunda.bpm.bvis.EJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.Entites.Customer;
import org.camunda.bpm.bvis.Entites.Insurance;
import org.camunda.bpm.bvis.Entites.Order;

@Stateless
public class ContractHandler {
	
	@PersistenceContext
	protected EntityManager em;

	public Order getOrder(long orderID) {
		
		return null;
	}
	
	public void adjustOrder(long orderId) {
		
	}
	
	public void handleRequirements() {
		
	}
	
	public void transmitDetailsToInsurance() {
		
	}
	
	public void informInsurance() {
		
	}
	
	public void finalizeContracting() {
		
	}
	
	public Insurance getInsurance(long orderID) {
		
		return null;
	}
	
	public Customer getUser(long orderID) {
		
		return null;
	}
}
