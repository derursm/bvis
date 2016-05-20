package org.camunda.bpm.bvis.bvis;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Interface for adder resource
 */
@Path("add")
public interface RestExample2AdderResource {

	/**
	 * Redirects a client which invokes GET on the resources to a resource with
	 * sample data
	 * 
	 * @return Redirection response
	 */
	@GET
	public Response redirectToSampleData();

	/**
	 * Adds two numbers that are transmitted as a JSON object within the request
	 * body and returns a response that contains the result
	 * 
	 * @param adderRequest {@link RestExample2AdderRequest} object that is deserialised from the provided JSON object
	 * @return {@link RestExample2AdderResult} JSON object
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addTwoNumbers(RestExample2AdderRequest adderRequest);

	/**
	 * Adds two numbers that are transmitted as part of the path template
	 * 
	 * @param first
	 * @param second
	 * @return {@link RestExample2AdderResult} JSON object
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{first}/{second}")
	public RestExample2AdderResult addTwoNumbers(
			@PathParam("first") int first,
			@PathParam("second") int second);

}