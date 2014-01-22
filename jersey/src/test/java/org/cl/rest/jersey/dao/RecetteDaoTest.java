package org.cl.rest.jersey.dao;

import static org.fest.assertions.Assertions.assertThat;

import javax.inject.Inject;

import lombok.Setter;

import org.cl.rest.jersey.domain.Recette;
import org.cl.util.weld.test.WeldJUnit4Runner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import fj.data.Option;

@RunWith(WeldJUnit4Runner.class)
public class RecetteDaoTest {

	@Inject
	@Setter
	private RecetteDao recetteDao;

	private Recette recette1;
	private Recette recette2;

	@Before
	public void init() {
		((RecetteDaoImpl) recetteDao).deleteAll();
	}

	@Test
	public void test_on_empty_list() {
		assertThat(recetteDao.getListRecettes()).isEmpty();
	}

	@Test
	public void test_get_list() {
		createjeuDeTest();
		assertThat(recetteDao.getListRecettes()).contains(recette1, recette2);
	}

	@Test
	public void test_create_recette() {
		Recette recette = new Recette();
		recette.setLibelle("Ma nouvelle recette");
		recette.setNiveau(2);
		recette.setRecette("qfibqi iy isdbibqisbcibsdqcn iusqb icsqbdci sdqijbcibsivsib gvbsbg ibsiugb sbibsib fibs iuzgb");
		recette.setTemps(2);

		String id = recetteDao.create(recette);

		assertThat(recetteDao.get(id).some()).isSameAs(recette);
	}

	@Test(expected = NullPointerException.class)
	public void test_create_recette_fail() {

		recetteDao.create(null);

	}

	@Test
	public void test_get_recette() {

		createjeuDeTest();

		Option<Recette> oRecette = recetteDao.get(recette1.getId());

		assertThat(oRecette.isSome()).isTrue();
		assertThat(oRecette.some()).isSameAs(recette1);

		assertThat(recetteDao.get("AAAA").isNone()).isTrue();

	}

	@Test(expected = NullPointerException.class)
	public void test_get_recette_fail() {

		createjeuDeTest();

		recetteDao.get(null);
	}

	@Test
	public void test_update() {

		createjeuDeTest();

		String newRe7 = "recette modifie";

		Recette recette = recetteDao.get(recette1.getId()).some();

		assertThat(recette.getRecette()).isNotEqualTo(newRe7);

		Recette newRecette = new Recette();
		newRecette.setId(recette.getId());
		newRecette.setLibelle(recette.getLibelle());
		newRecette.setNiveau(12);
		newRecette.setTemps(5);
		newRecette.setRecette(newRe7);

		recetteDao.update(newRecette);

		Recette recetteReloaded = recetteDao.get(recette1.getId()).some();

		assertThat(recetteReloaded).isSameAs(newRecette);
		assertThat(recetteReloaded.getRecette()).isEqualTo(newRe7);

	}

	@Test(expected = IllegalArgumentException.class)
	public void test_update_fail_not_exist() {

		createjeuDeTest();

		recette1.setId("AAAAAAA");

		recetteDao.update(recette1);

	}

	@Test(expected = IllegalArgumentException.class)
	public void test_update_fail_no_id() {

		createjeuDeTest();

		recette1.setId(null);

		recetteDao.update(recette1);

	}

	@Test(expected = NullPointerException.class)
	public void test_update_fail_null() {

		createjeuDeTest();

		recetteDao.update(null);

	}

	private void createjeuDeTest() {

		recette1 = new Recette();
		recette1.setLibelle("Maccarons pistache");
		recette1.setNiveau(3);
		recette1.setTemps(3);
		recette1.setRecette("Prendre des macarrons et des pistaches : on obtient des macarrons à la pistache");
		recetteDao.create(recette1);

		recette2 = new Recette();
		recette2.setLibelle("Maccarons chocolat");
		recette2.setNiveau(3);
		recette2.setTemps(3);
		recette2.setRecette("Prendre des macarrons et des chocolat : on obtient des macarrons au chocolat");
		recetteDao.create(recette2);

	}

}
