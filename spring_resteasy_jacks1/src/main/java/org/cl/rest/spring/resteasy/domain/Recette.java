package org.cl.rest.spring.resteasy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.codehaus.jackson.annotate.JsonProperty;

import com.wordnik.swagger.annotations.ApiModel;

@Data
@EqualsAndHashCode(of = { "libelle" })
@ApiModel(value = "Une recette de desserts.", description = "Une recette de dessert, avec sa description, le niveau de dificulté et le temps de realisation nécessaire")
public class Recette {

	@JsonProperty("identifiant")
	private String id;
	@JsonProperty("titre")
	private String libelle;
	@JsonProperty("niveauDeDifficulte")
	private int niveau;
	@JsonProperty("tempsPreparation")
	private int temps;
	private String recette;
	
}
