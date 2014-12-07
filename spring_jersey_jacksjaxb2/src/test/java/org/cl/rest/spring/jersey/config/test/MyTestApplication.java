package org.cl.rest.spring.jersey.config.test;

import org.cl.rest.spring.jersey.config.MyApplication;
import org.glassfish.jersey.test.JerseyTest;

public class MyTestApplication extends MyApplication {

	public MyTestApplication(JerseyTest jerseyTest) {

		super();
		register(jerseyTest);
	}

}
