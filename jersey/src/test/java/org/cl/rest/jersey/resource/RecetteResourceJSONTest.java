package org.cl.rest.jersey.resource;

import javax.ws.rs.core.MediaType;

public class RecetteResourceJSONTest extends RecetteResourceTest {

	public RecetteResourceJSONTest() throws Exception {
		super(MediaType.APPLICATION_JSON_TYPE);
	}

}
