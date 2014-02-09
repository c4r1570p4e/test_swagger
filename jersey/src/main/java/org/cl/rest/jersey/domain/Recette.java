package org.cl.rest.jersey.domain;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@Data
@XmlRootElement
@EqualsAndHashCode(of = { "libelle" })
@ApiModel(value = "Une recette de desserts.", description = "Une recette de dessert, avec sa description, le niveau de dificulté et le temps de realisation nécessaire")
public class Recette {

	@ApiModelProperty(value = "id de la recette", notes="Cet id est affecté par l'API lors de la creation de la recette")
	private String id;
	@ApiModelProperty(value = "Le libelle de la recette", required = true)
	private String libelle;
	@ApiModelProperty(value = "Le niveau de difficulté de la recette", required=false, allowableValues="1, 2, 3, 4, 5")
	private int niveau;
	@ApiModelProperty(value = "Le temps nécessaire à la réalisation de la recette en minutes", required=false)
	private int temps;
	@ApiModelProperty(value = "Les étapes necessaires à la réalisation de la recette", required=false)
	private String recette;

}
