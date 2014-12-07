package org.cl.rest.spring.jersey.domain;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@XmlRootElement
@EqualsAndHashCode(of = { "libelle" })
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@ApiModel(value = "Une recette de desserts.", description = "Une recette de dessert, avec sa description, le niveau de dificulté et le temps de realisation nécessaire")
public class Recette {

	@ApiModelProperty(value = "id de la recette", notes="Cet id est affecté par l'API lors de la creation de la recette")
	private String id;
	@ApiModelProperty(value = "Le libelle de la recette", required = true)
	@JsonProperty("titre")
	private String libelle;
	@ApiModelProperty(value = "Le niveau de difficulté de la recette", required=false, allowableValues="1, 2, 3, 4, 5")
	@JsonProperty("niveauDifficulte")
	private int niveau;
	@ApiModelProperty(value = "Le temps nécessaire à la réalisation de la recette en minutes", required=false)
	private int temps;
	@ApiModelProperty(value = "Les étapes necessaires à la réalisation de la recette", required=false)
	private String recette;
	
	@JsonIgnore
	private String trucInterne;

}
