package org.camunda.bpm.bvis.bvis;

/*
 * Simple Rest Service Example
 * athor: vit
 */

import javax.ws.rs.*;

@Path("tutorial")
public class RestExample1HelloWorld {
	
	@GET
	@Path("helloworld")
	public String helloworld(){
		return "Hello World!";
	}
}
