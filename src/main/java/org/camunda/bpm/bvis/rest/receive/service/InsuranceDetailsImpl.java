package org.camunda.bpm.bvis.rest.receive.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.camunda.bpm.bvis.rest.receive.dto.InsuranceAnswer;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.runtime.Execution;

public class InsuranceDetailsImpl implements InsuranceDetails {

	@Inject
	private BusinessProcess businessProcess;
	
	@Inject
	private RuntimeService runtimeService;
	
	@Override
	public Response receiveAnswer(InsuranceAnswer insuranceAnswer) {
		String processInstanceID = insuranceAnswer.getProcessInstanceIDBVIS();
		System.out.println("INSURANCE ANSWER RECEIVED");
		try {
			businessProcess.associateExecutionById(processInstanceID);
		}
		catch (Exception e) {
			System.out.println("WRONG PROCESS INSTANCE ID RETURNED BY INSURANCE");
			Response response = Response.ok("Wrong process instance ID").build();
			return response;
		}
		Execution execution = runtimeService.createExecutionQuery().
				processInstanceId(processInstanceID).
				activityId("ReceiveTask_InsurancDetails").singleResult();
		// place insurance answer results into runtime execution variables
		runtimeService.setVariable(execution.getId(), "insuranceResult", 
				insuranceAnswer.getOrder().getResult());
		runtimeService.setVariable(execution.getId(), "finalPrice", 
				insuranceAnswer.getOrder().getFinalPrice());
		runtimeService.setVariable(execution.getId(), "orderID", 
				insuranceAnswer.getOrder().getOrderID());
		runtimeService.setVariable(execution.getId(), "inquiryText", 
				insuranceAnswer.getOrder().getInquiryText());
		runtimeService.signal(execution.getId());

		Response response = Response.ok("No errors").build();
		return response;
	}

}
