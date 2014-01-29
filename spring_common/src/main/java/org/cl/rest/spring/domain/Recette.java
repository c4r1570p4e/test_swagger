package org.cl.rest.spring.domain;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@XmlRootElement
@EqualsAndHashCode(of = { "libelle" })
public class Recette {

	private String id;
	private String libelle;
	private int niveau;
	private int temps;
	private String recette;

}
