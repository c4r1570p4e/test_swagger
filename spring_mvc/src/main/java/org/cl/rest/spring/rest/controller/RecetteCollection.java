package org.cl.rest.spring.rest.controller;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cl.rest.spring.domain.Recette;

@XmlRootElement(name="recettes")
public class RecetteCollection {

	@XmlElement(name="recette")
	private List<Recette> list;

	public RecetteCollection() {
	}

	public RecetteCollection(List<Recette> recettes) {
		super();
		this.list = recettes;
	}

}
