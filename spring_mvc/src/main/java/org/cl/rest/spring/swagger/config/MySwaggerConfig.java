package org.cl.rest.spring.swagger.config;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.TRACE;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.paths.RelativeSwaggerPathProvider;
import com.mangofactory.swagger.paths.SwaggerPathProvider;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;
import com.wordnik.swagger.model.ResponseMessage;

@Configuration
@EnableSwagger
public class MySwaggerConfig {

	private SpringSwaggerConfig springSwaggerConfig;

	@Autowired
	private ServletContext servletContext;

	/**
	 * Required to autowire SpringSwaggerConfig
	 */
	@Autowired
	public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
		this.springSwaggerConfig = springSwaggerConfig;
	}

	/**
	 * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc
	 * framework - allowing for multiple swagger groups i.e. same code base
	 * multiple swagger resource listings.
	 */
	@Bean
	public SwaggerSpringMvcPlugin customImplementation() {

		ApiInfo apiInfo = new ApiInfo("Les Apis de Christophe", "Permet de gérer mes recettes de dessert", null,
				"clannoy@norsys.fr", null, null);

		return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo);
	}

	@Bean
	public Map<RequestMethod, List<ResponseMessage>> defaultResponseMessages() {
		LinkedHashMap<RequestMethod, List<ResponseMessage>> responses = newLinkedHashMap();
		responses.put(GET, new ArrayList<ResponseMessage>());
		responses.put(PUT, new ArrayList<ResponseMessage>());
		responses.put(POST, new ArrayList<ResponseMessage>());
		responses.put(DELETE, new ArrayList<ResponseMessage>());
		responses.put(PATCH, new ArrayList<ResponseMessage>());
		responses.put(TRACE, new ArrayList<ResponseMessage>());
		responses.put(OPTIONS, new ArrayList<ResponseMessage>());
		responses.put(HEAD, new ArrayList<ResponseMessage>());
		return responses;
	}

	@Bean
	public SwaggerPathProvider defaultSwaggerPathProvider() {

		RelativeSwaggerPathProvider mySwaggerPathProvider = new RelativeSwaggerPathProvider(servletContext) {
			@Override
			protected String applicationPath() {
				return super.applicationPath() + "/rest";
			}
		};

		return mySwaggerPathProvider;
	}
}
