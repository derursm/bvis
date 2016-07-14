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
		String processInstanceID = insuranceAnswer.getProcessinstance_id_bvis();
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
				insuranceAnswer.getOrder().getFinal_price());
		runtimeService.setVariable(execution.getId(), "orderID", 
				insuranceAnswer.getOrder().getOrder_id());
		runtimeService.setVariable(execution.getId(), "inquiryText", 
				insuranceAnswer.getOrder().getInquiry_text());
		runtimeService.setVariable(execution.getId(), "processIdCapitol", 
				insuranceAnswer.getProcessinstance_id_capitol());
		
		// dirty fix using multithreading as else signalling the process to continue would block this method
		new Thread(new ContinueProcess(runtimeService, execution)).start();

		Response response = Response.ok("No errors").build();
		System.out.println("RETURNING RESPONSE TO INSURANCE");
		return response;
	}
	
	private class ContinueProcess implements Runnable {
		private RuntimeService runtimeService;
		private Execution execution;
		
		public ContinueProcess(RuntimeService runtimeService, Execution execution) {
			this.runtimeService = runtimeService;
			this.execution = execution;
		}
		
		public ContinueProcess() {}
		@Override
		public void run() {
			runtimeService.signal(execution.getId());
		}
		
	}
}
