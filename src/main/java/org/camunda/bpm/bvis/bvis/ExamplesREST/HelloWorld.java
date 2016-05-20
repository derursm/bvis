package org.camunda.bpm.bvis.bvis.ExamplesREST;

/*
 * Simple Rest Service Example
 * athor: vit
 */

import javax.ws.rs.*;

@Path("tutorial")
public class HelloWorld {
	
	@GET
	@Path("helloworld")
	public String helloworld(){
		return "Hello World!";
	}
}
