package org.camunda.bpm.bvis.bvis;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

public class RestExample2AdderResourceImpl implements RestExample2AdderResource {

	public Response redirectToSampleData() {
		RestExample2AdderRequest adderRequest = new RestExample2AdderRequest(42, 23);
		return Response.seeOther(adderRequestUri(adderRequest)).build();
	}
	
	private URI adderRequestUri(RestExample2AdderRequest adderRequest) {
		return UriBuilder.fromResource(RestExample2AdderResource.class)
				.segment("" + adderRequest.getFirst())
				.segment("" + adderRequest.getSecond()).build();
	}

	public Response addTwoNumbers(RestExample2AdderRequest adderRequest) {
		Response response = Response.ok(new RestExample2AdderResult(adderRequest)).build();
		response.getMetadata().add("Location", adderRequestUri(adderRequest));
		return response;
	}

	public RestExample2AdderResult addTwoNumbers(final int first, final int second) {
		return new RestExample2AdderResult(first, second);
	}

}
