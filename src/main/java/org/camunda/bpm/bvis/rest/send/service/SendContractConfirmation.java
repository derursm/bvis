package org.camunda.bpm.bvis.rest.send.service;


import javax.faces.bean.ManagedBean;
import javax.inject.Named;

import org.camunda.bpm.bvis.entities.RentalOrder;
import org.camunda.bpm.bvis.rest.send.dto.ContractConfirmationDTO;
import org.camunda.bpm.bvis.rest.send.dto.ContractConfirmationOrder;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

@ManagedBean
@Named
public class SendContractConfirmation {

	private static final String BASE_URI = "http://camunda-capitol.uni-muenster.de/partner-interface/";
	
	public String sendContractConfirmation(RentalOrder entityOrder, String processInstanceIDBVIS, 
			String processInstanceIDCapitol, int contractStatus) {
		ContractConfirmationDTO contractConfirmation = new ContractConfirmationDTO();
		contractConfirmation.setProcessinstance_id_capitol(processInstanceIDBVIS);
		contractConfirmation.setProcessinstance_id_capitol(processInstanceIDCapitol);
		ContractConfirmationOrder order = new ContractConfirmationOrder();
		order.setOrder_id(entityOrder.getOrderID());
		order.setRequest_date(entityOrder.getRequestDate());
		order.setContract_status(contractStatus);
		contractConfirmation.setOrder(order);
		
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
		ResteasyWebTarget target = new ResteasyClientBuilder().build().target(BASE_URI);
		SendContractConfirmationClient senderClient = target.proxy(SendContractConfirmationClient.class);
		String result = senderClient.sendClaim(contractConfirmation);
		return result;
	}
}
