package org.camunda.bpm.bvis.EJB;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.Entites.Customer;
import org.camunda.bpm.bvis.Entites.Insurance;
import org.camunda.bpm.bvis.Entites.Order;
import org.camunda.bpm.bvis.bvis.OrderBusinessLogic;
import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;

@Stateless
public class ContractHandler {
	
	@PersistenceContext
	protected EntityManager em;
	
	// Inject task form available through the camunda cdi artifact
	@Inject
	private TaskForm taskForm;
	
	private static Logger LOGGER = Logger.getLogger(OrderBusinessLogic.class.getName());
	
	public void persistOrder(DelegateExecution delegateExecution) {
		
		Order order = new Order();
		
		/*
		 * ....
		 */
	}
	
	public void sendRentalRequestConfirmation(DelegateExecution delegateExecution) {
		
	}
	
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
