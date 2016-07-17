package org.camunda.bpm.bvis.rest.receive.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.camunda.bpm.bvis.rest.receive.dto.Decision;
import org.camunda.bpm.bvis.rest.receive.dto.InsuranceClaimDecision;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.runtime.Execution;

public class ClaimDecisionImpl implements ClaimDecision{

	@Inject
	private BusinessProcess businessProcess;
	
	@Inject
	private RuntimeService runtimeService;
	
	@Override
	public Response receiveAnswer(InsuranceClaimDecision insuranceClaimDecision) {
		String processInstanceID = insuranceClaimDecision.getProcessinstance_id_bvis();
		System.out.println("INSURANCE ANSWER RECEIVED");
		
		try {
			businessProcess.associateExecutionById(processInstanceID);
		}
		catch (Exception e) {
			System.out.println("WRONG PROCESS INSTANCE ID RETURNED BY INSURANCE");
			System.out.println("PROCESS ID: " + processInstanceID);
			Response response = Response.ok("Wrong process instance ID").build();
			return response;
		}
		Execution execution = runtimeService.createExecutionQuery().
				processInstanceId(processInstanceID).activityId("ReceiveTask_InsuranceClaimDecision").
				singleResult();
		Decision decision = insuranceClaimDecision.getDecision();
		runtimeService.setVariable(execution.getId(), "insuranceDecision", 
				 decision.getInsurance_decision());
		runtimeService.setVariable(execution.getId(), "coverageCosts", 
				 decision.getCoverage_costs());
		runtimeService.setVariable(execution.getId(), "customerCosts", 
				 decision.getCustomer_costs());
		runtimeService.setVariable(execution.getId(), "insuranceAnswerDescription", 
				 decision.getDescription());
		runtimeService.setVariable(execution.getId(), "processIDCapitol", 
				 insuranceClaimDecision.getProcessinstance_id_capitol());

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
