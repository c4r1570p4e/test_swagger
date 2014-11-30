package org.cl.rest.spring.resteasy.controller;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;




import org.cl.rest.spring.resteasy.dao.RecetteDao;
import org.cl.rest.spring.resteasy.domain.Recette;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.spring.SpringResourceFactory;
import org.jboss.resteasy.specimpl.ResteasyUriBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;

import fj.data.Option;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/testApplicationContext.xml")
public class RecetteControllerTest {

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private RecetteDao recetteDao;

	@Autowired
	private RecetteController recetteController;

	private Dispatcher dispatcher;

	private UriInfo uriInfo;

	private ResteasyUriBuilder uriBuilder;

	@Before
	public void init() {
		
		Mockito.reset(recetteDao);
		
		dispatcher = MockDispatcherFactory.createDispatcher();

		SpringResourceFactory noDefaults = new SpringResourceFactory("recetteController", appContext,
				RecetteController.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);

		uriBuilder = new ResteasyUriBuilder();
		uriBuilder.path("/recettes");
		uriInfo = mock(UriInfo.class);
		when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);

		recetteController.setUriInfo(uriInfo);
	}

	@Test
	public void can_get_all() throws Exception {

		Recette r1 = createRecette("1", "recette1", 1, 2, "bla bla bla bla");
		Recette r2 = createRecette("2", "recette2", 2, 6, "bla bla bla");
		Recette r3 = createRecette("3", "recette3", 4, 1, "bla bla");

		when(recetteDao.getListRecettes()).thenReturn(Arrays.asList(r1, r2, r3));

		MockHttpRequest request = MockHttpRequest.get("/recettes");
		MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		assertThat(response.getStatus()).isEqualTo(200);

		String json = response.getContentAsString();
		String expected = "[{\"id\":\"1\",\"libelle\":\"recette1\",\"niveau\":1,\"recette\":\"bla bla bla bla\",\"temps\":2},{\"id\":\"2\",\"libelle\":\"recette2\",\"niveau\":2,\"recette\":\"bla bla bla\",\"temps\":6},{\"id\":\"3\",\"libelle\":\"recette3\",\"niveau\":4,\"recette\":\"bla bla\",\"temps\":1}]";
		JSONAssert.assertEquals(expected, json, true);

	}

	private Recette createRecette(String id, String libelle, int niveau, int temps, String detail) {
		Recette recette = new Recette();
		recette.setId(id);
		recette.setLibelle(libelle);
		recette.setNiveau(niveau);
		recette.setTemps(temps);
		recette.setRecette(detail);

		return recette;
	}

	@Test
	public void can_get_return_empty_list() throws Exception {

		when(recetteDao.getListRecettes()).thenReturn(new ArrayList<Recette>());

		MockHttpRequest request = MockHttpRequest.get("/recettes");
		MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		assertThat(response.getStatus()).isEqualTo(200);

		String json = response.getContentAsString();
		String expected = "[]";
		JSONAssert.assertEquals(expected, json, true);
	}

	@Test
	public void can_find_by_id() throws Exception {

		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");

		when(recetteDao.get("12345")).thenReturn(Option.some(recette));

		MockHttpRequest request = MockHttpRequest.get("/recettes/12345");
		MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		assertThat(response.getStatus()).isEqualTo(200);

		String json = response.getContentAsString();
		String expected = "{\"id\":\"12345\",\"libelle\":\"fraisier\",\"niveau\":3,\"recette\":\"Avec une genoise et des fraises...\",\"temps\":2}";
		JSONAssert.assertEquals(expected, json, true);

	}

	@Test
	public void can_find_by_id_return_empty() throws Exception {


		when(recetteDao.get("12345")).thenReturn(Option.<Recette>none());

		MockHttpRequest request = MockHttpRequest.get("/recettes/12345");
		MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		assertThat(response.getStatus()).isEqualTo(404);
	}
	
	
	@Test
	public void can_find_list_by_libelle() throws Exception {

		Recette recette1 = createRecette("12345", "macarons chocolat", 1, 2, "bla bla bla");
		Recette recette2 = createRecette("67890", "Macarons Pistache", 2, 3, "bla bla bla");

		when(recetteDao.findByLibelle("macarons")).thenReturn(Lists.newArrayList(recette1, recette2));

		MockHttpRequest request = MockHttpRequest.get("/recettes/findByLibelle?libellePart=macarons");
		MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		assertThat(response.getStatus()).isEqualTo(200);

		String json = response.getContentAsString();
		
		String expected = "[{\"id\":\"12345\",\"libelle\":\"macarons chocolat\",\"niveau\":1,\"temps\":2},{\"id\":\"67890\",\"libelle\":\"Macarons Pistache\",\"niveau\":2,\"temps\":3}]";
		JSONAssert.assertEquals(expected, json, false);

		verify(recetteDao, times(1)).findByLibelle("macarons");
	}

	@Test
	public void can_find_list_by_libelle_return_empty_list() throws Exception {


		when(recetteDao.findByLibelle("macarons")).thenReturn(new ArrayList<Recette>());

		MockHttpRequest request = MockHttpRequest.get("/recettes/findByLibelle?libellePart=macarons");
		MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		assertThat(response.getStatus()).isEqualTo(200);

		String json = response.getContentAsString();
		String expected = "[]";
		JSONAssert.assertEquals(expected, json, false);

		verify(recetteDao, times(1)).findByLibelle("macarons");
	}
	
	
	@Test
	public void can_delete() throws Exception {

		when(recetteDao.delete("1")).thenReturn(1);

		MockHttpRequest request = MockHttpRequest.delete("/recettes/1");
		MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		assertThat(response.getStatus()).isEqualTo(204);

		verify(recetteDao, times(1)).delete("1");
	}

	@Test
	public void can_t_delete() throws Exception {

		when(recetteDao.delete("1")).thenReturn(0);

		MockHttpRequest request = MockHttpRequest.delete("/recettes/1");
		MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		assertThat(response.getStatus()).isEqualTo(404);

		verify(recetteDao, times(1)).delete("1");
	}
	
	@Test
	public void can_update() throws Exception {

		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");
		when(recetteDao.update(recette)).thenReturn(1);

		String json = "{\"id\":12345,\"libelle\":\"fraisier\",\"niveau\":3,\"recette\":\"Avec une genoise et des fraises...\",\"temps\":2}";

		MockHttpRequest request = MockHttpRequest.put("/recettes/12345").contentType(MediaType.APPLICATION_JSON)
				.content(json.getBytes());
		MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		assertThat(response.getStatus()).isEqualTo(204);

		verify(recetteDao, times(1)).update(recette);
	}

	@Test
	public void can_t_update() throws Exception {

		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");
		when(recetteDao.update(recette)).thenReturn(0);

		String json = "{\"id\":12345,\"libelle\":\"fraisier\",\"niveau\":3,\"recette\":\"Avec une genoise et des fraises...\",\"temps\":2}";

		MockHttpRequest request = MockHttpRequest.put("/recettes/12345").contentType(MediaType.APPLICATION_JSON)
				.content(json.getBytes());
		MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		assertThat(response.getStatus()).isEqualTo(404);

		verify(recetteDao, times(1)).update(recette);
	}
	
	
	@Test
	public void should_post_create_new_recette() throws Exception {

		Recette recette = createRecette(null, "Le fraisier", 2, 4, "recette secrette");
		String newId = "123456789";

		when(recetteDao.create(recette)).thenReturn(newId);

		String json = "{\"libelle\":\"Le fraisier\",\"niveau\":2,\"recette\":\"recette secrette\",\"temps\":4}";

		MockHttpRequest request = MockHttpRequest.post("/recettes").contentType(MediaType.APPLICATION_JSON)
				.content(json.getBytes());
		MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		assertThat(response.getStatus()).isEqualTo(201);
		assertThat(response.getOutputHeaders().get("LOCATION").get(0).toString()).isEqualTo("recettes/123456789");

		verify(recetteDao, times(1)).create(recette);
	}

}
