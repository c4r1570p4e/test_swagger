package org.cl.rest.spring.resteasy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Data
@EqualsAndHashCode(of = { "libelle" })
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class Recette {

	private String id;
	private String libelle;
	private int niveau;
	private int temps;
	private String recette;
	
}
