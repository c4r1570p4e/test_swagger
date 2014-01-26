package org.cl.rest.jersey.dao;

import java.util.List;

import org.cl.rest.jersey.domain.Recette;

import fj.data.Option;

public interface RecetteDao {

	List<Recette> getListRecettes();

	String create(Recette recette);

	int update(Recette recette);

	Option<Recette> get(String id);
	
	List<Recette> findByLibelle(String libelle);
	
	

}
