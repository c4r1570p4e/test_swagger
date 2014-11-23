package org.cl.rest.spring.mvc.config.test;

import org.cl.rest.spring.mvc.dao.RecetteDao;
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
