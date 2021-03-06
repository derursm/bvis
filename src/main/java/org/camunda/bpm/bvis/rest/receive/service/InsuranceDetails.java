package org.camunda.bpm.bvis.rest.receive.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.camunda.bpm.bvis.rest.receive.dto.InsuranceAnswer;

@Path("insurancedetails")
public interface InsuranceDetails {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveAnswer(InsuranceAnswer insuranceAnswer);
}
