package org.cl.rest.spring.jersey.domain;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@XmlRootElement
@EqualsAndHashCode(of = { "libelle" })
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class Recette {

	private String id;
	
	@JsonProperty("titre")
	private String libelle;
	
	@JsonProperty("niveauDifficulte")
	private int niveau;
	
	private int temps;
	
	private String recette;
	
	@JsonIgnore
	private String trucInterne;

}
