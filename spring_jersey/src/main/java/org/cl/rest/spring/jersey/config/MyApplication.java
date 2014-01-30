package org.cl.rest.spring.jersey.config;

import org.cl.rest.spring.jersey.controller.RecetteController;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class MyApplication extends ResourceConfig {

	
	public MyApplication() {
		
		register(RequestContextFilter.class);
		register(RecetteController.class);
		register(JacksonFeature.class);
		
	}
	
}
