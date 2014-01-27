package org.cl.rest.jersey.controller;

import javax.ws.rs.core.MediaType;

public class RecetteControllerXMLTest extends RecetteControllerTest {

	public RecetteControllerXMLTest() throws Exception {
		super();
		this.mediaType = MediaType.APPLICATION_XML_TYPE;
	}

}
