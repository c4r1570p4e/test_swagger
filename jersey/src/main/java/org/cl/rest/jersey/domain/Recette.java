package org.cl.rest.jersey.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "libelle" })
public class Recette {

	private String id;
	private String libelle;
	private int niveau;
	private int temps;
	private String recette;

}