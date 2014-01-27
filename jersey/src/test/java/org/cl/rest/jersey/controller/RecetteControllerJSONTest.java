package org.cl.rest.jersey.controller;

import javax.ws.rs.core.MediaType;

public class RecetteControllerJSONTest extends RecetteControllerTest {

	public RecetteControllerJSONTest() throws Exception {
		super();
		this.mediaType = MediaType.APPLICATION_JSON_TYPE;
	}

}
