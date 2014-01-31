package org.cl.rest.jersey.controller;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.cl.rest.jersey.dao.RecetteDao;
import org.cl.rest.jersey.domain.Recette;
import org.cl.util.jersey.inject.InjectableProviderContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

import fj.data.Option;

public abstract class RecetteControllerTest extends JerseyTest {

	private static AppDescriptor APP_DESCRIPTOR;
	
	static {

		InjectableProviderContext.init(Mockito.mock(RecetteDao.class));

		WebAppDescriptor.Builder webAppDescriptor = new WebAppDescriptor.Builder("org.cl").initParam(
				"com.sun.jersey.api.json.POJOMappingFeature", "true");

		ClientConfig cc = new DefaultClientConfig();
		cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

		webAppDescriptor.clientConfig(cc);

		APP_DESCRIPTOR = webAppDescriptor.build();
	}

	protected MediaType mediaType = null;
	
	private WebResource webResource = resource();

	private RecetteDao recetteDao = InjectableProviderContext.getInjectableObjectForClass(RecetteDao.class);

	public RecetteControllerTest(MediaType mediaType) throws Exception {
		super(APP_DESCRIPTOR);
		this.mediaType = mediaType;
	}

	@Before
	public void reInitMock() {
		reset(this.recetteDao);
	}

	@Test
	public void should_get_return_empty_list() {
		when(recetteDao.getListRecettes()).thenReturn(new ArrayList<Recette>());
		Recette[] recettes = webResource.path("recettes").accept(mediaType).get(Recette[].class);
		assertThat(recettes).isEmpty();
	}

	@Test
	public void should_get_return_a_list() {

		Recette r1 = createRecette("1", "recette1", 1, 2, "bla bla bla bla");
		Recette r2 = createRecette("2", "recette2", 2, 6, "bla bla bla");
		Recette r3 = createRecette("3", "recette3", 4, 1, "bla bla");

		List<Recette> dataRecettes = Arrays.asList(r1, r2, r3);

		when(recetteDao.getListRecettes()).thenReturn(dataRecettes);
		Recette[] recettes = webResource.path("recettes").accept(mediaType).get(Recette[].class);

		assertThat(recettes).containsOnly(r1, r2, r3);
	}

	@Test
	public void should_post_create_new_recette() {

		Recette recette = createRecette(null, "Le fraisier", 2, 4, "recette secrette");
		String newId = "123456789";

		when(recetteDao.create(recette)).thenReturn(newId);
		webResource.path("recettes").accept(mediaType).type(mediaType).post(recette);
		verify(recetteDao, times(1)).create(recette);
	}

	@Test
	public void should_not_post_create_new_recette() {

		Recette recette = new Recette();

		try {
			webResource.path("recettes").accept(mediaType).type(mediaType)
					.post(recette);
			fail();
		} catch (UniformInterfaceException e) {
			assertThat(e.getResponse().getStatus()).isEqualTo(400);
			verifyZeroInteractions(recetteDao);
		}

	}

	@Test
	public void should_not_find_by_id() {

		when(recetteDao.get(anyString())).thenReturn(Option.fromNull((Recette) null));

		try {
			webResource.path("recettes/12345").accept(mediaType).get(Recette.class);
			fail();
		} catch (UniformInterfaceException e) {
			assertThat(e.getResponse().getStatus()).isEqualTo(Status.NOT_FOUND.getStatusCode());
			verify(recetteDao, times(1)).get(anyString());
		}
	}

	@Test
	public void should_find_by_id() {

		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");

		when(recetteDao.get("12345")).thenReturn(Option.some(recette));

		Recette found = webResource.path("recettes/12345").accept(mediaType).get(Recette.class);

		assertThat(found).isEqualTo(recette);
	}

	@Test
	public void should_not_update() {

		Recette recette = createRecette("1", "fraisier", 3, 2, "Avec une genoise et des fraises...");

		try {
			webResource.path("recettes/12345").accept(mediaType).type(mediaType)
					.put(recette);
			fail();
		} catch (UniformInterfaceException e) {
			assertThat(e.getResponse().getStatus()).isEqualTo(Status.BAD_REQUEST.getStatusCode());
		}

		recette = createRecette(null, "fraisier", 3, 2, "Avec une genoise et des fraises...");

		try {
			webResource.path("recettes/12345").accept(mediaType).type(mediaType)
					.put(recette);
			fail();
		} catch (UniformInterfaceException e) {
			assertThat(e.getResponse().getStatus()).isEqualTo(Status.BAD_REQUEST.getStatusCode());
		}
	}

	@Test
	public void should_not_update_not_found() {

		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");

		when(recetteDao.update(recette)).thenReturn(0);

		try {
			webResource.path("recettes/12345").accept(mediaType).type(mediaType)
					.put(recette);
			fail();
		} catch (UniformInterfaceException e) {
			assertThat(e.getResponse().getStatus()).isEqualTo(Status.NOT_FOUND.getStatusCode());
		}
	}

	@Test
	public void should_update() {

		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");
		when(recetteDao.update(recette)).thenReturn(1);

		webResource.path("recettes/12345").accept(mediaType).type(mediaType)
				.put(recette);

		verify(recetteDao, times(1)).update(recette);
	}

	@Test
	public void should_find_empty_list_by_libelle() {

		when(recetteDao.findByLibelle(anyString())).thenReturn(new LinkedList<Recette>());
		Recette[] recettes = webResource.path("recettes/findByLibelle").queryParam("libellePart", "fraisier")
				.accept(mediaType).get(Recette[].class);
		assertThat(recettes.length).isEqualTo(0);

		verify(recetteDao, times(1)).findByLibelle(anyString());
	}

	@Test
	public void should_find_list_by_libelle() {

		Recette recette1 = createRecette("12345", "macarons chocolat", 1, 2, "bla bla bla");
		Recette recette2 = createRecette("67890", "Macarons Pistache", 2, 3, "bla bla bla");

		when(recetteDao.findByLibelle("macarons")).thenReturn(Lists.newArrayList(recette1, recette2));
		Recette[] recettes = webResource.path("recettes/findByLibelle").queryParam("libellePart", "macarons")
				.accept(mediaType).get(Recette[].class);
		assertThat(recettes.length).isEqualTo(2);
		assertThat(recettes).containsOnly(recette1, recette2);

		verify(recetteDao, times(1)).findByLibelle("macarons");
	}

	@Test
	public void should_delete() {

		when(recetteDao.delete("1")).thenReturn(1);
		webResource.path("recettes/1").accept(mediaType).delete();
		verify(recetteDao, times(1)).delete("1");
	}

	@Test
	public void should_not_delete() {

		when(recetteDao.delete("1")).thenReturn(0);
		try {
			webResource.path("recettes/1").accept(mediaType).delete();
			fail();
		} catch (UniformInterfaceException e) {
			assertThat(e.getResponse().getStatus()).isEqualTo(Status.NOT_FOUND.getStatusCode());
		}

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
