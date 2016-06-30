package org.camunda.bpm.bvis.Web;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

import org.camunda.bpm.bvis.EJB.ContractHandler;
import org.camunda.bpm.bvis.Entities.Car;
import org.camunda.bpm.bvis.Entities.RentalOrder;
import org.camunda.bpm.engine.cdi.BusinessProcess;

@Named
@ConversationScoped
@ManagedBean(name = "editOrder")
public class OrderEditBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// Inject the BusinessProcess to access the process variables
	@Inject
	private BusinessProcess businessProcess;

	@Inject
	private ContractHandler contractHandler;

	// Caches the RentalOrder during the conversation
	private RentalOrder rentalOrder;

	public RentalOrder getRentalOrder() {
		if (rentalOrder == null) {
			// Load the order entity from the database if not already cached
			rentalOrder = contractHandler.getOrder((Long) businessProcess.getVariable("orderId"));
		}
		System.out.println("Cars: " + rentalOrder.getCars());
		return rentalOrder;
	}
	
	public Long getCarID() {
		if(rentalOrder.getCars().isEmpty()) {
			return null;
		}
		else {
			return rentalOrder.getCars().iterator().next().getId();
		}
	}

	public void submitForm() throws IOException {
		// Persist updated order entity and complete task form
		contractHandler.updateOrder(rentalOrder);
	}
}
