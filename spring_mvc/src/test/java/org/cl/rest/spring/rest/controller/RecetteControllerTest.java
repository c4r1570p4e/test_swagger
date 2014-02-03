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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.cl.rest.spring.dao.RecetteDao;
import org.cl.rest.spring.domain.Recette;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Lists;

import fj.data.Option;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:test-servlet-context.xml")
public abstract class RecetteControllerTest {
	
    @Autowired
    private WebApplicationContext wac;
    
    @Autowired
    private RecetteDao recetteDao;

    private MockMvc mockMvc;
    
    private MediaType mediaType;
    
    public RecetteControllerTest(MediaType mediaType) {
    	super();
    	this.mediaType = mediaType;
	}    

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        Mockito.reset(this.recetteDao);
    }
    
    protected ResultActions get_return_empty_list() throws Exception {
    	
    	when(recetteDao.getListRecettes()).thenReturn(new ArrayList<Recette>());
    	
        return this.mockMvc.perform(get("/recettes").accept(mediaType).contentType(mediaType))
          .andExpect(status().isOk())
          .andExpect(content().contentType(mediaType.toString()));
    } 

	protected ResultActions get_return_a_list() throws Exception {

		Recette r1 = createRecette("1", "recette1", 1, 2, "bla bla bla bla");
		Recette r2 = createRecette("2", "recette2", 2, 6, "bla bla bla");
		Recette r3 = createRecette("3", "recette3", 4, 1, "bla bla");

		List<Recette> dataRecettes = Arrays.asList(r1, r2, r3);

		when(recetteDao.getListRecettes()).thenReturn(dataRecettes);
		
        return this.mockMvc.perform(get("/recettes").accept(mediaType).contentType(mediaType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(mediaType.toString()));
	}

	
	protected ResultActions post_create_new_recette(String flux, Recette recette) throws Exception {

		String newId = "123456789";

		when(recetteDao.create(recette)).thenReturn(newId);
		
		ResultActions r = this.mockMvc.perform(post("/recettes")
        .content(flux)
        .accept(mediaType).contentType(mediaType))
        .andExpect(status().isCreated());
		
		verify(recetteDao, times(1)).create(recette);
		
		return r;
	}	
	
	protected void not_post_create_new_recette(String flux, Recette recette) throws Exception {

        this.mockMvc.perform(post("/recettes")
        .content(flux)
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

	
	protected ResultActions find_by_id() throws Exception {

		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");

		when(recetteDao.get("12345")).thenReturn(Option.some(recette));

        return this.mockMvc.perform(get("/recettes/12345").accept(mediaType).contentType(mediaType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(mediaType.toString()));

	}

	protected void not_update(String flux) throws Exception {

        this.mockMvc.perform(put("/recettes/12345").content(flux)
        .accept(mediaType).contentType(mediaType))
        .andExpect(status().isBadRequest());		
	}

	protected void not_update_not_found(String flux, Recette recette) throws Exception {

		when(recetteDao.update(recette)).thenReturn(0);

        this.mockMvc.perform(put("/recettes/12345").content(flux)
        		.accept(mediaType).contentType(mediaType))
        .andExpect(status().isNotFound());		
	}

	protected void update(String flux, Recette recette) throws Exception {

		when(recetteDao.update(recette)).thenReturn(1);

        this.mockMvc.perform(put("/recettes/12345").content(flux)
        		.accept(mediaType).contentType(mediaType))
        .andExpect(status().isOk());

		verify(recetteDao, times(1)).update(recette);
	}
	
	
	protected ResultActions find_empty_list_by_libelle() throws Exception {

		when(recetteDao.findByLibelle(anyString())).thenReturn(new LinkedList<Recette>());
		
        ResultActions r = this.mockMvc.perform(get("/recettes/findByLibelle").param("libellePart", "fraisier")
        		.accept(mediaType).contentType(mediaType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(mediaType.toString()));

		verify(recetteDao, times(1)).findByLibelle(anyString());
		
		return r;
	}

	protected ResultActions find_list_by_libelle() throws Exception {

		Recette recette1 = createRecette("12345", "macarons chocolat", 1, 2, "bla bla bla");
		Recette recette2 = createRecette("67890", "Macarons Pistache", 2, 3, "bla bla bla");

		when(recetteDao.findByLibelle("macarons")).thenReturn(Lists.newArrayList(recette1, recette2));
		
		ResultActions r = this.mockMvc.perform(get("/recettes/findByLibelle").param("libellePart", "macarons")
        		.accept(mediaType).contentType(mediaType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(mediaType.toString()));

		verify(recetteDao, times(1)).findByLibelle("macarons");
		
		return r;
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
	
	protected Recette createRecette(String id, String libelle, int niveau, int temps, String detail) {
		Recette recette = new Recette();
		recette.setId(id);
		recette.setLibelle(libelle);
		recette.setNiveau(niveau);
		recette.setTemps(temps);
		recette.setRecette(detail);

		return recette;
	}    
    
    
    
}
