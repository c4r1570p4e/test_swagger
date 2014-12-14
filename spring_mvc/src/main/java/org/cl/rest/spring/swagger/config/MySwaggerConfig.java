package org.cl.rest.spring.swagger.config;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.paths.RelativeSwaggerPathProvider;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;

@Configuration
@EnableSwagger
public class MySwaggerConfig {

	@Autowired
	private ServletContext servletContext;

	private SpringSwaggerConfig springSwaggerConfig;

	@Autowired
	public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
		this.springSwaggerConfig = springSwaggerConfig;
	}

	@Bean
	public SwaggerSpringMvcPlugin customImplementation() {

		ApiInfo apiInfo = new ApiInfo("Les Apis de Christophe", "Permet de gérer mes recettes de dessert", null,
				"clannoy@norsys.fr", null, null);

		RelativeSwaggerPathProvider mySwaggerPathProvider = new RelativeSwaggerPathProvider(servletContext) {
			@Override
			protected String applicationPath() {
				return super.applicationPath() + "/rest";
			}
		};

		return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo)
				.pathProvider(mySwaggerPathProvider);
	}

}
