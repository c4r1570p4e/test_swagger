package org.cl.rest.jersey.dao;

import java.util.List;

import org.cl.rest.jersey.domain.Recette;

import com.sun.istack.NotNull;

import fj.data.Option;

public interface RecetteDao {

	List<Recette> getListRecettes();

	String create(@NotNull Recette recette);

	void update(@NotNull Recette recette);

	Option<Recette> get(@NotNull String id);

}
