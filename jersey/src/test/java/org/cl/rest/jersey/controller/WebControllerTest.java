package org.cl.rest.jersey.controller;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.cl.rest.jersey.dao.RecetteDao;
import org.cl.rest.jersey.domain.Recette;
import org.cl.util.jersey.inject.InjectableProviderContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class WebControllerTest extends JerseyTest {

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

	private WebResource webResource = resource();

	private RecetteDao recetteDao = InjectableProviderContext.getInjectableObjectForClass(RecetteDao.class);

	public WebControllerTest() throws Exception {
		super(APP_DESCRIPTOR);
	}

	@Before
	public void reInitMock() {
		reset(this.recetteDao);
	}

	@Test
	public void should_get_return_empty_list() {
		when(recetteDao.getListRecettes()).thenReturn(new ArrayList<Recette>());
		List recettes = webResource.path("recettes").get(List.class);
		assertThat(recettes).isEmpty();
	}

	@Test
	public void should_get_return_a_list() {

		Recette r1 = createRecette("1", "recette1", 1, 2, "bla bla bla bla");
		Recette r2 = createRecette("2", "recette2", 2, 6, "bla bla bla");
		Recette r3 = createRecette("3", "recette3", 4, 1, "bla bla");

		List<Recette> dataRecettes = Arrays.asList(r1, r2, r3);

		when(recetteDao.getListRecettes()).thenReturn(dataRecettes);
		Recette[] recettes = webResource.path("recettes").get(Recette[].class);

		assertThat(recettes).containsOnly(r1, r2, r3);
	}

	@Test
	public void should_post_create_new_recette() {

		Recette recette = createRecette(null, "Le fraisier", 2, 4, "recette secrette");
		String newId = "123456789";

		when(recetteDao.create(recette)).thenReturn(newId);
		webResource.path("recettes").type(MediaType.APPLICATION_JSON).post(recette);
		verify(recetteDao, times(1)).create(recette);
	}

	@Test
	public void should_not_post_create_new_recette() {

		Recette recette = new Recette();

		try {
			webResource.path("recettes").type(MediaType.APPLICATION_JSON).post(recette);
			fail();
		} catch (UniformInterfaceException e) {
			assertThat(e.getResponse().getStatus()).isEqualTo(400);
			verifyZeroInteractions(recetteDao);
		}

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
