package org.cl.rest.jersey.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Default;
import javax.inject.Singleton;

import org.cl.rest.jersey.domain.Recette;
import org.jboss.weld.exceptions.IllegalArgumentException;

import fj.data.Option;

@Singleton
@Default
public class RecetteDaoImpl implements RecetteDao {

	private int index = 0;

	private Map<String, Recette> recettes = new ConcurrentHashMap<String, Recette>();

	public RecetteDaoImpl() {
		super();
	}

	@Override
	public List<Recette> getListRecettes() {
		return new LinkedList<Recette>(recettes.values());
	}

	@Override
	public String create(Recette recette) {

		int i = index++;

		recette.setId(String.valueOf(i));
		recettes.put(String.valueOf(i), recette);
		return String.valueOf(i);
	}

	@Override
	public int update(Recette recette) {

		if (recette.getId() == null) {
			throw new IllegalArgumentException("id null");
		}

		if (!recettes.containsKey(recette.getId())) {
			return 0;
		}

		recettes.put(recette.getId(), recette);

		return 1;

	}

	@Override
	public Option<Recette> get(String id) {

		Recette recette = recettes.get(id);

		return Option.fromNull(recette);

	}

	public void deleteAll() {
		recettes.clear();
	}

	@Override
	public List<Recette> findByLibelle(String libellePart) {

		List<Recette> retour = new LinkedList<Recette>();

		if (libellePart == null) {
			return retour;
		}

		for (Recette recette : recettes.values()) {

			if (recette.getLibelle() != null && recette.getLibelle().toUpperCase().contains(libellePart.toUpperCase())) {
				retour.add(recette);
			}
		}

		return retour;
	}

	@Override
	public int delete(String id) {

		if (recettes.remove(id) != null) {
			return 1;
		} else {
			return 0;
		}
	}

}
