package org.cl.rest.spring.jersey.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.wordnik.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@EqualsAndHashCode(of = { "libelle" })
public class Recette {

	@ApiModelProperty(value = "id de la recette", notes="Cet id est affecté par l'API lors de la creation de la recette")
	private String id;
	@ApiModelProperty(value = "Le libelle de la recette", required = true)
	@XmlElement(name="titre")
	private String libelle;
	@ApiModelProperty(value = "Le niveau de difficulté de la recette", required=false, allowableValues="1, 2, 3, 4, 5")
	@XmlElement(name="niveauDifficulte")
	private int niveau;
	@ApiModelProperty(value = "Le temps nécessaire à la réalisation de la recette en minutes", required=false)
	private int temps;
	@ApiModelProperty(value = "Les étapes necessaires à la réalisation de la recette", required=false)
	private String recette;
	
	@XmlTransient
	private String trucInterne;

}
