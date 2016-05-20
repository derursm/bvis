package org.camunda.bpm.bvis.bvis.ExamplesREST;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

public class AdderResourceImpl implements AdderResource {

	public Response redirectToSampleData() {
		AdderRequest adderRequest = new AdderRequest(42, 23);
		return Response.seeOther(adderRequestUri(adderRequest)).build();
	}
	
	private URI adderRequestUri(AdderRequest adderRequest) {
		return UriBuilder.fromResource(AdderResource.class)
				.segment("" + adderRequest.getFirst())
				.segment("" + adderRequest.getSecond()).build();
	}

	public Response addTwoNumbers(AdderRequest adderRequest) {
		Response response = Response.ok(new AdderResult(adderRequest)).build();
		response.getMetadata().add("Location", adderRequestUri(adderRequest));
		return response;
	}

	public AdderResult addTwoNumbers(final int first, final int second) {
		return new AdderResult(first, second);
	}

}
