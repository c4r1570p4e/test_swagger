package org.cl.rest.spring.rest.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.cl.rest.spring.domain.Recette;
import org.junit.Test;
import org.mockito.internal.matchers.EndsWith;
import org.springframework.http.MediaType;

public class RecetteControllerXmlTest extends RecetteControllerTest {
	
	private Marshaller marshaller;
	
	public RecetteControllerXmlTest() throws JAXBException {
		super(MediaType.APPLICATION_XML);
		JAXBContext jc = JAXBContext.newInstance(RecetteCollection.class, Recette.class);
		marshaller = jc.createMarshaller();
	}

	@Test
	public void should_find_by_id() throws Exception {
		find_by_id().andExpect(xpath("/recette/id").string("12345")).andExpect(
				xpath("/recette/libelle").string("fraisier"));
	}
	
	@Test
	public void should_get_return_a_list() throws Exception {

		get_return_a_list()
        .andExpect(xpath("/recettes/recette[1]/id").string("1"))
        .andExpect(xpath("/recettes/recette[1]/libelle").string("recette1"))
        .andExpect(xpath("/recettes/recette[2]/id").string("2"))
        .andExpect(xpath("/recettes/recette[2]/libelle").string("recette2"))
        .andExpect(xpath("/recettes/recette[3]/id").string("3"))
        .andExpect(xpath("/recettes/recette[3]/libelle").string("recette3"));		
	}		

	@Test
	public void should_find_list_by_libelle() throws Exception {

		find_list_by_libelle()
        .andExpect(xpath("/recettes/recette[1]/id").string("12345"))	
        .andExpect(xpath("/recettes/recette[1]/libelle").string("macarons chocolat"))
        .andExpect(xpath("/recettes/recette[2]/id").string("67890"))	
        .andExpect(xpath("/recettes/recette[2]/libelle").string("Macarons Pistache"));
	}	

    @Test
    public void should_get_return_empty_list() throws Exception {
    	
    	get_return_empty_list()
        .andExpect(xpath("recettes/recette[1]").doesNotExist());
    } 	
    
	@Test
	public void should_find_empty_list_by_libelle() throws Exception {
		find_empty_list_by_libelle()
        .andExpect(xpath("recettes/recette[1]").doesNotExist());
	}     
	
	@Test
	public void should_post_create_new_recette() throws Exception {
	
		Recette recette = createRecette(null, "Le fraisier", 2, 4, "recette secrette");
		
		post_create_new_recette(toXml(recette), recette)
        .andExpect(header().string("Location", new EndsWith("/recettes/123456789")))
        .andExpect(content().string(""));
	}		
	
	@Test
	public void should_update() throws Exception {
		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");
		update(toXml(recette),recette);
	}		
	
	private String toXml(Recette recette) throws JAXBException {
		StringWriter sw = new StringWriter();
		marshaller.marshal(recette, sw);
		return sw.toString();
	}
	
	@Test
	public void should_not_update_not_found() throws Exception {
		Recette recette = createRecette("12345", "fraisier", 3, 2, "Avec une genoise et des fraises...");
		not_update_not_found(toXml(recette),recette);
	}
	
	@Test
	public void should_not_post_create_new_recette() throws Exception {
		Recette recette = new Recette();
		not_post_create_new_recette(toXml(recette),recette);
	}
	
	@Test
	public void should_not_update() throws Exception {

		Recette recette = createRecette("1", "fraisier", 3, 2, "Avec une genoise et des fraises...");
		not_update(toXml(recette));

		recette = createRecette(null, "fraisier", 3, 2, "Avec une genoise et des fraises...");
		not_update(toXml(recette));
	}		
	
}
