package org.camunda.bpm.bvis.rest.send.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.camunda.bpm.bvis.rest.send.dto.ContractDetailsDTO;

@Path("contractdetails")
public interface SendInquiryClient {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String sendInquiry(ContractDetailsDTO contractDetails);
}