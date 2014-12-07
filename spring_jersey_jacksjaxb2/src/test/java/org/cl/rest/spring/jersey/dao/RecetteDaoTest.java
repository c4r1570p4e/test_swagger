package org.cl.rest.spring.jersey.dao;

import static org.fest.assertions.Assertions.assertThat;

import org.cl.rest.spring.jersey.dao.RecetteDao;
import org.cl.rest.spring.jersey.dao.RecetteDaoImpl;
import org.cl.rest.spring.jersey.domain.Recette;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fj.data.Option;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testConfig.xml")
public class RecetteDaoTest {

	@Autowired
	private RecetteDao recetteDao;

	private Recette recette1;
	private Recette recette2;

	@Before
	public void init() {
		((RecetteDaoImpl) recetteDao).deleteAll();
	}

	@Test
	public void should_return_empty_list() {
		assertThat(recetteDao.getListRecettes()).isEmpty();
	}

	@Test
	public void should_return_list() {
		createjeuDeTest();
		assertThat(recetteDao.getListRecettes()).contains(recette1, recette2);
	}

	@Test
	public void should_create_recette() {
		Recette recette = new Recette();
		recette.setLibelle("Ma nouvelle recette");
		recette.setNiveau(2);
		recette.setRecette("qfibqi iy isdbibqisbcibsdqcn iusqb icsqbdci sdqijbcibsivsib gvbsbg ibsiugb sbibsib fibs iuzgb");
		recette.setTemps(2);

		String id = recetteDao.create(recette);

		assertThat(recetteDao.get(id).some()).isSameAs(recette);
	}

	@Test(expected = NullPointerException.class)
	public void should_not_create_null_recette() {

		recetteDao.create(null);

	}

	@Test
	public void should_return_recette_by_id() {

		createjeuDeTest();

		Option<Recette> oRecette = recetteDao.get(recette1.getId());

		assertThat(oRecette.isSome()).isTrue();
		assertThat(oRecette.some()).isSameAs(recette1);

		assertThat(recetteDao.get("AAAA").isNone()).isTrue();

	}

	@Test(expected = NullPointerException.class)
	public void should_not_get_recette_with_null_id() {

		createjeuDeTest();

		recetteDao.get(null);
	}

	@Test
	public void should_update() {

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

		assertThat(recetteDao.update(newRecette)).isEqualTo(1);

		Recette recetteReloaded = recetteDao.get(recette1.getId()).some();

		assertThat(recetteReloaded).isSameAs(newRecette);
		assertThat(recetteReloaded.getRecette()).isEqualTo(newRe7);

	}

	@Test
	public void should_not_update_non_existant_recette() {

		createjeuDeTest();

		recette1.setId("AAAAAAA");

		assertThat(recetteDao.update(recette1)).isEqualTo(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void should_not_update_with_no_id() {

		createjeuDeTest();

		recette1.setId(null);
		recetteDao.update(recette1);

	}

	@Test(expected = NullPointerException.class)
	public void should_not_update_null() {

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

	@Test
	public void should_not_find_by_libelle() {

		createjeuDeTest();

		assertThat(recetteDao.findByLibelle("fraisier")).isEmpty();
		assertThat(recetteDao.findByLibelle(null)).isEmpty();
	}

	@Test
	public void should_find_by_libelle() {

		createjeuDeTest();

		assertThat(recetteDao.findByLibelle("maccarons")).containsOnly(recette1, recette2);
		assertThat(recetteDao.findByLibelle("chocolat")).containsOnly(recette2);
		assertThat(recetteDao.findByLibelle("Maccarons pistache")).containsOnly(recette1);

	}

	@Test
	public void should_delete() {

		createjeuDeTest();

		assertThat(recetteDao.getListRecettes().size()).isEqualTo(2);
		assertThat(recetteDao.delete(recette1.getId())).isEqualTo(1);
		assertThat(recetteDao.getListRecettes()).containsExactly(recette2);

	}

	@Test
	public void should_not_delete() {

		createjeuDeTest();

		assertThat(recetteDao.getListRecettes().size()).isEqualTo(2);
		assertThat(recetteDao.delete("0")).isEqualTo(0);
		assertThat(recetteDao.getListRecettes()).containsOnly(recette1, recette2);

	}

}
