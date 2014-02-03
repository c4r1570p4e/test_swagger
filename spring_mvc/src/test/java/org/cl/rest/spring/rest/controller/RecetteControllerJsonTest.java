package org.cl.rest.spring.rest.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.cl.rest.spring.domain.Recette;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.mockito.internal.matchers.EndsWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class RecetteControllerJsonTest extends RecetteControllerTest {

    @Autowired
    private ObjectMapper jsonObjectMapper;	
	
	public RecetteControllerJsonTest() {
		super(MediaType.APPLICATION_JSON);
	}

	@Test
	public void should_find_by_id() throws Exception {
		find_by_id().andExpect(jsonPath("$.id").value("12345"))
        .andExpect(jsonPath("$.libelle").value("fraisier"));
	}	
	
	@Test
	public void should_get_return_a_list() throws Exception {

		get_return_a_list()
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").value("1"))
        .andExpect(jsonPath("$[0].libelle").value("recette1"))
        .andExpect(jsonPath("$[1].id").value("2"))
        .andExpect(jsonPath("$[1].libelle").value("recette2"))
        .andExpect(jsonPath("$[2].id").value("3"))
        .andExpect(jsonPath("$[2].libelle").value("recette3"));		
	}	
	
	@Test
	public void should_find_list_by_libelle() throws Exception {

		find_list_by_libelle()
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").value("12345"))
        .andExpect(jsonPath("$[0].libelle").value("macarons chocolat"))
        .andExpect(jsonPath("$[1].id").value("67890"))
        .andExpect(jsonPath("$[1].libelle").value("Macarons Pistache"));
	}
	
    @Test
    public void should_get_return_empty_list() throws Exception {
    	
    	get_return_empty_list()
    	.andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0]").doesNotExist());
    } 	
    
	@Test
	public void should_find_empty_list_by_libelle() throws Exception {
		find_empty_list_by_libelle()
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0]").doesNotExist());
	}    
	
	@Test
	public void should_post_create_new_recette() throws Exception {
	
		Recette recette = createRecette(null, "Le fraisier", 2, 4, "recette secrette");
		
		post_create_new_recette(jsonObjectMapper.writeValueAsString(recette), recette)
        .andExpect(header().string("Location", new EndsWith("/recettes/123456789")))
        .andExpect(content().string(""));
	}		
	
	@Test
	public void should_update() throws Exception {
		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");
		update(jsonObjectMapper.writeValueAsString(recette),recette);
	}	
	
	@Test
	public void should_not_update_not_found() throws Exception {
		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");
		not_update_not_found(jsonObjectMapper.writeValueAsString(recette),recette);
	}	
	
	@Test
	public void should_not_post_create_new_recette() throws Exception {
		Recette recette = new Recette();
		not_post_create_new_recette(jsonObjectMapper.writeValueAsString(recette),recette);
	}		
	
	@Test
	public void should_not_update() throws Exception {

		Recette recette = createRecette("1", "fraisier", 3, 2, "Avec une genoise et des fraises...");
		not_update(jsonObjectMapper.writeValueAsString(recette));

		recette = createRecette(null, "fraisier", 3, 2, "Avec une genoise et des fraises...");
		not_update(jsonObjectMapper.writeValueAsString(recette));
	}	
	
}
