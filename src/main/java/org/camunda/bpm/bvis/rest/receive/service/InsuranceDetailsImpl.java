package org.camunda.bpm.bvis.rest.receive.service;

import javax.inject.Inject;

import org.camunda.bpm.bvis.rest.receive.dto.InsuranceAnswer;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;

public class InsuranceDetailsImpl implements InsuranceDetails {

	@Inject
	private BusinessProcess businessProcess;
	
	@Inject
	private RuntimeService runtimeService;
	
	@Override
	public void receiveAnswer(InsuranceAnswer insuranceAnswer) {
		String processInstanceID = insuranceAnswer.getProcessInstanceIDBVIS();
		System.out.println("Insurance answer received");
		System.out.println("ASSOCIATE WITH CORRECT EXECUTION");
		businessProcess.associateExecutionById(processInstanceID);
		System.out.println("ASSOCIATION SUCCESSFUL");
		System.out.println("GET EXECUTION");
		Execution execution = runtimeService.createExecutionQuery().
				processInstanceId(processInstanceID).
				activityId("ReceiveTask_InsurancDetails").singleResult();
		System.out.println("RESUME PROCESS");
		runtimeService.signal(execution.getId());
		System.out.println("RESUME PROCESS SUCCESSFUL");
	}

}
