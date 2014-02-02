package org.cl.rest.spring.rest.controller;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.cl.rest.spring.dao.RecetteDao;
import org.cl.rest.spring.domain.Recette;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.matchers.EndsWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Lists;

import fj.data.Option;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:test-servlet-context.xml")
public class RecetteControllerTest {
	
    @Autowired
    private WebApplicationContext wac;
    
    @Autowired
    private RecetteDao recetteDao;

    @Autowired
    private ObjectMapper jsonObjectMapper;
    
    private MockMvc mockMvc;
    
    private MediaType mediaType = MediaType.APPLICATION_JSON;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        Mockito.reset(this.recetteDao);
    }
    
    @Test
    public void should_get_return_empty_list() throws Exception {
    	
    	when(recetteDao.getListRecettes()).thenReturn(new ArrayList<Recette>());
    	
        this.mockMvc.perform(get("/recettes").accept(mediaType).contentType(mediaType))
          .andExpect(status().isOk())
          .andExpect(content().contentType(mediaType.toString()))
          .andExpect(jsonPath("$").isArray())
          .andExpect(jsonPath("$[0]").doesNotExist());
    } 

	@Test
	public void should_get_return_a_list() throws Exception {

		Recette r1 = createRecette("1", "recette1", 1, 2, "bla bla bla bla");
		Recette r2 = createRecette("2", "recette2", 2, 6, "bla bla bla");
		Recette r3 = createRecette("3", "recette3", 4, 1, "bla bla");

		List<Recette> dataRecettes = Arrays.asList(r1, r2, r3);

		when(recetteDao.getListRecettes()).thenReturn(dataRecettes);
		
        this.mockMvc.perform(get("/recettes").accept(mediaType).contentType(mediaType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(mediaType.toString()))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").value("1"))
        .andExpect(jsonPath("$[0].libelle").value("recette1"))
        .andExpect(jsonPath("$[1].id").value("2"))
        .andExpect(jsonPath("$[1].libelle").value("recette2"))
        .andExpect(jsonPath("$[2].id").value("3"))
        .andExpect(jsonPath("$[2].libelle").value("recette3"));		

	}

	
	@Test
	public void should_post_create_new_recette() throws Exception {

		Recette recette = createRecette(null, "Le fraisier", 2, 4, "recette secrette");
		String newId = "123456789";

		when(recetteDao.create(recette)).thenReturn(newId);
		
        this.mockMvc.perform(post("/recettes")
        .content(jsonObjectMapper.writeValueAsString(recette))
        .accept(mediaType).contentType(mediaType))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", new EndsWith("/recettes/123456789")))
        .andExpect(content().string(""));
		
		verify(recetteDao, times(1)).create(recette);
	}	
	
	@Test
	public void should_not_post_create_new_recette() throws Exception {

		Recette recette = new Recette();

        this.mockMvc.perform(post("/recettes")
        .content(jsonObjectMapper.writeValueAsString(recette))
        .accept(mediaType).contentType(mediaType))
        .andExpect(status().is(400))
        .andExpect(content().string(""));
		
		verifyZeroInteractions(recetteDao);

	}	
	
	@Test
	public void should_not_find_by_id() throws Exception {

		when(recetteDao.get(anyString())).thenReturn(Option.fromNull((Recette) null));
		
        this.mockMvc.perform(get("/recettes/12345").accept(mediaType).contentType(mediaType))
        .andExpect(status().isNotFound());
        
		verify(recetteDao, times(1)).get(anyString());
		
	}

	@Test
	public void should_find_by_id() throws Exception {

		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");

		when(recetteDao.get("12345")).thenReturn(Option.some(recette));

        this.mockMvc.perform(get("/recettes/12345").accept(mediaType).contentType(mediaType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(mediaType.toString()))
        .andExpect(jsonPath("$.id").value("12345"))
        .andExpect(jsonPath("$.libelle").value("fraisier"));			

	}

	@Test
	public void should_not_update() throws Exception {

		Recette recette = createRecette("1", "fraisier", 3, 2, "Avec une genoise et des fraises...");

        this.mockMvc.perform(put("/recettes/12345").content(jsonObjectMapper.writeValueAsString(recette))
        		.accept(mediaType).contentType(mediaType))
        .andExpect(status().isBadRequest());		

		recette = createRecette(null, "fraisier", 3, 2, "Avec une genoise et des fraises...");

        this.mockMvc.perform(put("/recettes/12345").content(jsonObjectMapper.writeValueAsString(recette))
        		.accept(mediaType).contentType(mediaType))
        .andExpect(status().isBadRequest());		
	}

	@Test
	public void should_not_update_not_found() throws Exception {

		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");

		when(recetteDao.update(recette)).thenReturn(0);

        this.mockMvc.perform(put("/recettes/12345").content(jsonObjectMapper.writeValueAsString(recette))
        		.accept(mediaType).contentType(mediaType))
        .andExpect(status().isNotFound());		
	}

	@Test
	public void should_update() throws Exception {

		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");
		when(recetteDao.update(recette)).thenReturn(1);

        this.mockMvc.perform(put("/recettes/12345").content(jsonObjectMapper.writeValueAsString(recette))
        		.accept(mediaType).contentType(mediaType))
        .andExpect(status().isOk());

		verify(recetteDao, times(1)).update(recette);
	}
	
	@Test
	public void should_find_empty_list_by_libelle() throws Exception {

		when(recetteDao.findByLibelle(anyString())).thenReturn(new LinkedList<Recette>());
		
        this.mockMvc.perform(get("/recettes/findByLibelle").param("libellePart", "fraisier")
        		.accept(mediaType).contentType(mediaType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(mediaType.toString()))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0]").doesNotExist());

		verify(recetteDao, times(1)).findByLibelle(anyString());
	}

	@Test
	public void should_find_list_by_libelle() throws Exception {

		Recette recette1 = createRecette("12345", "macarons chocolat", 1, 2, "bla bla bla");
		Recette recette2 = createRecette("67890", "Macarons Pistache", 2, 3, "bla bla bla");

		when(recetteDao.findByLibelle("macarons")).thenReturn(Lists.newArrayList(recette1, recette2));
		
        this.mockMvc.perform(get("/recettes/findByLibelle").param("libellePart", "macarons")
        		.accept(mediaType).contentType(mediaType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(mediaType.toString()))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").value("12345"))	
        .andExpect(jsonPath("$[0].libelle").value("macarons chocolat"))
        .andExpect(jsonPath("$[1].id").value("67890"))	
        .andExpect(jsonPath("$[1].libelle").value("Macarons Pistache"));

		verify(recetteDao, times(1)).findByLibelle("macarons");
	}

	@Test
	public void should_delete() throws Exception {

		when(recetteDao.delete("1")).thenReturn(1);

		this.mockMvc.perform(delete("/recettes/1").accept(mediaType)).andExpect(status().isOk());

		verify(recetteDao, times(1)).delete("1");
	}

	@Test
	public void should_not_delete() throws Exception {

		when(recetteDao.delete("1")).thenReturn(0);

		this.mockMvc.perform(delete("/recettes/1").accept(mediaType)).andExpect(status().isNotFound());

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
