package org.cl.rest.spring.jersey.controller;

import static javax.ws.rs.client.Entity.entity;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.reset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.cl.rest.spring.jersey.config.test.MyTestApplication;
import org.cl.rest.spring.jersey.dao.RecetteDao;
import org.cl.rest.spring.jersey.domain.Recette;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import fj.data.Option;

public abstract class RecetteControllerTest extends JerseyTest {

	protected MediaType mediaType = null;
	
	@Autowired
	private RecetteDao recetteDao;

	public RecetteControllerTest(MediaType mediaType) {
		super();
		this.mediaType = mediaType;
	}	
	
	@Override
	protected Application configure() {
		return new MyTestApplication(this);
	}

	@Override
	protected void configureClient(ClientConfig config) {
		config.register(JacksonFeature.class);
		super.configureClient(config);
	}
	
	@Before
	public void init() {
		reset(recetteDao);
	}
	
	@Test
	public void should_get_return_empty_list() {
		when(recetteDao.getListRecettes()).thenReturn(new ArrayList<Recette>());
		Recette[] recettes = target("recettes").request(mediaType).get(Recette[].class);
		assertThat(recettes).isEmpty();
	}

	@Test
	public void should_get_return_a_list() {

		Recette r1 = createRecette("1", "recette1", 1, 2, "bla bla bla bla");
		Recette r2 = createRecette("2", "recette2", 2, 6, "bla bla bla");
		Recette r3 = createRecette("3", "recette3", 4, 1, "bla bla");

		List<Recette> dataRecettes = Arrays.asList(r1, r2, r3);

		when(recetteDao.getListRecettes()).thenReturn(dataRecettes);
		Recette[] recettes = target("recettes").request(mediaType).get(Recette[].class);

		assertThat(recettes).containsOnly(r1, r2, r3);
	}

	@Test
	public void should_post_create_new_recette() {

		Recette recette = createRecette(null, "Le fraisier", 2, 4, "recette secrette");
		String newId = "123456789";

		when(recetteDao.create(recette)).thenReturn(newId);
		target("recettes").request(mediaType).post(entity(recette,mediaType));
		verify(recetteDao, times(1)).create(recette);
	}

	@Test
	public void should_not_post_create_new_recette() {

		Recette recette = new Recette();

		Response response = target("recettes").request(mediaType).post(entity(recette,mediaType));
		
		assertThat(response.getStatus()).isEqualTo(400);
		verifyZeroInteractions(recetteDao);
	}

	@Test
	public void should_not_find_by_id() {

		when(recetteDao.get(anyString())).thenReturn(Option.fromNull((Recette) null));

		try {
			target("recettes/12345").request(mediaType).get(Recette.class);
			fail();
		} catch (WebApplicationException e) {
			assertThat(e.getResponse().getStatus()).isEqualTo(Status.NOT_FOUND.getStatusCode());
			verify(recetteDao, times(1)).get(anyString());
		}
	}

	@Test
	public void should_find_by_id() {

		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");

		when(recetteDao.get("12345")).thenReturn(Option.some(recette));

		Recette found = target("recettes/12345").request(mediaType).get(Recette.class);

		assertThat(found).isEqualTo(recette);
	}

	@Test
	public void should_not_update() {

		Recette recette = createRecette("1", "fraisier", 3, 2, "Avec une genoise et des fraises...");

		Response response = target("recettes/12345").request(mediaType).put(entity(recette,mediaType));
		assertThat((response).getStatus()).isEqualTo(Status.BAD_REQUEST.getStatusCode());

		recette = createRecette(null, "fraisier", 3, 2, "Avec une genoise et des fraises...");

		response = target("recettes/12345").request(mediaType).put(entity(recette,mediaType));
		assertThat((response).getStatus()).isEqualTo(Status.BAD_REQUEST.getStatusCode());
	}

	@Test
	public void should_not_update_not_found() {

		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");

		when(recetteDao.update(recette)).thenReturn(0);

		Response response = target("recettes/12345").request(mediaType).put(entity(recette,mediaType));
		assertThat((response).getStatus()).isEqualTo(Status.NOT_FOUND.getStatusCode());
	}

	@Test
	public void should_update() {

		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");
		when(recetteDao.update(recette)).thenReturn(1);

		Response response = target("recettes/12345").request(mediaType).put(entity(recette,mediaType));
		assertThat(response.getStatus()).isEqualTo(Status.NO_CONTENT.getStatusCode());

		verify(recetteDao, times(1)).update(recette);
	}

	@Test
	public void should_find_empty_list_by_libelle() {

		when(recetteDao.findByLibelle(anyString())).thenReturn(new LinkedList<Recette>());
		Recette[] recettes = target("recettes/findByLibelle").queryParam("libellePart", "fraisier")
				.request(mediaType).get(Recette[].class);
		assertThat(recettes.length).isEqualTo(0);

		verify(recetteDao, times(1)).findByLibelle(anyString());
	}

	@Test
	public void should_find_list_by_libelle() {

		Recette recette1 = createRecette("12345", "macarons chocolat", 1, 2, "bla bla bla");
		Recette recette2 = createRecette("67890", "Macarons Pistache", 2, 3, "bla bla bla");

		when(recetteDao.findByLibelle("macarons")).thenReturn(Lists.newArrayList(recette1, recette2));
		Recette[] recettes = target("recettes/findByLibelle").queryParam("libellePart", "macarons")
				.request(mediaType).get(Recette[].class);
		assertThat(recettes.length).isEqualTo(2);
		assertThat(recettes).containsOnly(recette1, recette2);

		verify(recetteDao, times(1)).findByLibelle("macarons");
	}

	@Test
	public void should_delete() {

		when(recetteDao.delete("1")).thenReturn(1);
		Response response = target("recettes/1").request(mediaType).delete();
		assertThat(response.getStatus()).isEqualTo(Status.NO_CONTENT.getStatusCode());
		verify(recetteDao, times(1)).delete("1");
	}

	@Test
	public void should_not_delete() {

		when(recetteDao.delete("1")).thenReturn(0);

		Response response = target("recettes/1").request(mediaType).delete();
		assertThat(response.getStatus()).isEqualTo(Status.NOT_FOUND.getStatusCode());

		verify(recetteDao, times(1)).delete("1");

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

}
