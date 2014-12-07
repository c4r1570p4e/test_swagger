package org.cl.rest.spring.jersey.config.test;

import org.cl.rest.spring.jersey.dao.RecetteDao;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

	@Bean
	public RecetteDao recetteDao() {
		return Mockito.mock(RecetteDao.class);
	}

}
