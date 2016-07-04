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
		runtimeService.signal(execution.getId());

		Response response = Response.ok("No errors").build();
		return response;
	}
}
