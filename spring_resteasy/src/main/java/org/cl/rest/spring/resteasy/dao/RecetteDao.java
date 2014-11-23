package org.cl.rest.spring.resteasy.dao;

import java.util.List;

import org.cl.rest.spring.resteasy.domain.Recette;

import fj.data.Option;

public interface RecetteDao {

	List<Recette> getListRecettes();

	String create(Recette recette);

	int update(Recette recette);

	Option<Recette> get(String id);

	List<Recette> findByLibelle(String libelle);

	int delete(String id);

}
