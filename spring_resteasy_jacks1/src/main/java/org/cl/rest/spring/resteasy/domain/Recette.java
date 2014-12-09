package org.cl.rest.spring.resteasy.domain;

import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "libelle" })
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
