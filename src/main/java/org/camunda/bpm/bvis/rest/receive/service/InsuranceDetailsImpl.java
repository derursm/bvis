package org.camunda.bpm.bvis.rest.receive.service;

import org.camunda.bpm.bvis.rest.receive.dto.InsuranceAnswer;

public class InsuranceDetailsImpl implements InsuranceDetails {

	@Override
	public void receiveAnswer(InsuranceAnswer insuranceAnswer) {
		System.out.println("Insurance answer received");
		System.out.println(insuranceAnswer.getProcessInstanceIDBVIS());
		System.out.println(insuranceAnswer.getProcessInstanceIDCapitol());
		
	}

}
