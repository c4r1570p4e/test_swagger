package org.cl.rest.spring.jersey.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@EqualsAndHashCode(of = { "libelle" })
public class Recette {

	private String id;

	@XmlElement(name="titre")
	private String libelle;
	
	@XmlElement(name="niveauDifficulte")
	private int niveau;
	
	private int temps;
	
	private String recette;
	
	@XmlTransient
	private String trucInterne;

}
