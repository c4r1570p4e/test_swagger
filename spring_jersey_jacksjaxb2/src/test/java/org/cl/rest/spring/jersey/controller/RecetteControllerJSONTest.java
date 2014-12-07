package org.cl.rest.spring.jersey.controller;

import javax.ws.rs.core.MediaType;

public class RecetteControllerJSONTest extends RecetteControllerTest {

	public RecetteControllerJSONTest() throws Exception {
		super(MediaType.APPLICATION_JSON_TYPE);
	}

}
