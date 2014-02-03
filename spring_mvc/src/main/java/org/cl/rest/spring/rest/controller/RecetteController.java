package org.cl.rest.spring.rest.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cl.rest.spring.dao.RecetteDao;
import org.cl.rest.spring.domain.Recette;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;

import fj.data.Option;

@Controller
@RequestMapping(value = "/recettes", produces = { "application/json", "application/xml" })
public class RecetteController {

	@Autowired
	private RecetteDao recetteDao;

	public RecetteController() {
		super();
	}

	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	public List<Recette> getRecettes() {
		return recetteDao.getListRecettes();
	}

	@RequestMapping(method = RequestMethod.GET, produces = {"application/xml" })
	@ResponseBody
	public RecetteCollection getRecettesJaxb() {
		return new RecetteCollection(getRecettes());
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = { "application/json", "application/xml" })
	@ResponseBody
	public void createNewRecette(@RequestBody Recette recette, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		if (recette == null || Strings.isNullOrEmpty(recette.getLibelle())) {
			response.sendError(HttpStatus.BAD_REQUEST.value());
			return;
		}

		String id = recetteDao.create(recette);

		response.addHeader("Location", getCreatedUri(request, id));
		response.setStatus(HttpStatus.CREATED.value());
		return;

	}

	private String getCreatedUri(HttpServletRequest request, String id) {
		return request.getRequestURL() + "/" + id;
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public Recette getRecette(@PathVariable String id, HttpServletResponse response) throws IOException {

		if (Strings.isNullOrEmpty(id)) {
			response.sendError(HttpStatus.BAD_REQUEST.value());
			return null;
		}

		Option<Recette> recette = this.recetteDao.get(id);

		if (recette.isSome()) {
			return recette.some();
		} else {
			response.sendError(HttpStatus.NOT_FOUND.value());
			return null;
		}
	}

	@RequestMapping(value = "{id}", method = RequestMethod.PUT, consumes = { "application/json", "application/xml" })
	@ResponseBody
	public void updateRecette(@PathVariable String id, @RequestBody Recette recette, HttpServletResponse response)
			throws IOException {

		if (id == null || recette == null || Strings.isNullOrEmpty(recette.getId())
				|| Strings.isNullOrEmpty(recette.getLibelle())) {
			response.sendError(HttpStatus.BAD_REQUEST.value());
			return;
		}

		if (!id.equals(recette.getId())) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), "Impossible de modifier l'id d'une recette");
			return;
		}

		int nbMaj = recetteDao.update(recette);

		if (nbMaj == 1) {
			response.setStatus(HttpStatus.OK.value());
			return;
		} else {
			response.sendError(HttpStatus.NOT_FOUND.value());
			return;
		}
	}

	@RequestMapping(value = "findByLibelle", method = RequestMethod.GET, consumes = { "application/json" })
	@ResponseBody
	public List<Recette> findRecettesByLibelle(@RequestParam("libellePart") String libellePart) {
		return recetteDao.findByLibelle(libellePart);
	}
	
	@RequestMapping(value = "findByLibelle", method = RequestMethod.GET, consumes = { "application/xml" })
	@ResponseBody
	public RecetteCollection findRecettesByLibelleJaxb(@RequestParam("libellePart") String libellePart) {
		return new RecetteCollection(findRecettesByLibelle(libellePart));
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteRecette(@PathVariable String id, HttpServletResponse response) throws IOException {

		if (Strings.isNullOrEmpty(id)) {
			response.sendError(HttpStatus.BAD_REQUEST.value());
			return;
		}

		if (!(this.recetteDao.delete(id) == 1)) {
			response.sendError(HttpStatus.NOT_FOUND.value());
			return;
		}
	}

}
