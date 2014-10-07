package org.cl.rest.spring.mvc.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cl.rest.spring.mvc.dao.RecetteDao;
import org.cl.rest.spring.mvc.domain.Recette;
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
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import fj.data.Option;

@Controller
@RequestMapping(value = "/recettes", produces = { "application/json" })
@Api(value = "/recettes", description = "Réferentiel de recettes de deserts")
@Api(value = "recettes", description = "Réferentiel de recettes de desserts")
public class RecetteController {

	@Autowired
	private RecetteDao recetteDao;

	public RecetteController() {
		super();
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Liste les recettes", notes = "Retourne la liste de toutes les recettes de dessert.", response = Recette.class, responseContainer = "List")
	public List<Recette> getRecettes() {
		return recetteDao.getListRecettes();
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { "application/json"})
	@ResponseBody
	@ApiOperation(value = "Créé une nouvelle recette.", notes = "Permet de créer une nouvelle recette, l'id de la recette est affecté par l'API.")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Si il n'y a pas de recette passée, ou si la recette n'a pas de libelle"),
			@ApiResponse(code = 201, message = "La recette a été enregistré. L'url de la ressource crée est retourné dans le header 'location'.") })
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
	@ApiOperation(value = "Pour obtenir une recette à partir de son id.", response = Recette.class)
	@ApiResponses({ 
		@ApiResponse(code = 400, message = "L'id passé est vide"), 
		@ApiResponse(code = 404, message = "Il n'y a pas de recette pour l'identifiant demandé") 
		})	
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

	@RequestMapping(value = "{id}", method = RequestMethod.PUT, consumes = { "application/json" })
	@ResponseBody
	@ApiOperation(value="Mise à jour d'une recette.", 
	notes="Met à jour une recette. Il n'est pas possible de modifier l'identifiant de la recette.")
	@ApiResponses(value={
			@ApiResponse(code=200,message="La recette a été mise à jour."),
			@ApiResponse(code=400,message="Il n'y a pas de recette passé en body de la request, ou l'id ou le libelle sont absent, ou tentative de modification de l'id"),
			@ApiResponse(code=404,message="La recette à modifier n'existe pas.")
	})		
	public void updateRecette(@PathVariable String id,@ApiParam("recette modifiée") @RequestBody Recette recette, HttpServletResponse response)
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
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return;
		} else {
			response.sendError(HttpStatus.NOT_FOUND.value());
			return;
		}
	}

	@RequestMapping(value = "findByLibelle", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="Recherche les recettes par libelle", 
	notes =  "Recherche toute les recettes dont le libellé contient 'libellePart' passé en paramètre",
	response=Recette.class,
	responseContainer="List"
	)	
	public List<Recette> findRecettesByLibelle(@RequestParam("libellePart") String libellePart) {
		return recetteDao.findByLibelle(libellePart);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value="Suppression d'une recette", notes="supprime la recette par son id")
	@ApiResponses({
		 @ApiResponse(code=400, message="L'id de la recette est null ou vide."),
		 @ApiResponse(code=404, message="Il n'y a pas de recette correspondant cet id"),
		 @ApiResponse(code=200, message="La recette a été supprimé")
	})		
	public void deleteRecette(@PathVariable String id, HttpServletResponse response) throws IOException {

		if (Strings.isNullOrEmpty(id)) {
			response.sendError(HttpStatus.BAD_REQUEST.value());
			return;
		}

		if (!(this.recetteDao.delete(id) == 1)) {
			response.sendError(HttpStatus.NOT_FOUND.value());
			return;
		}
		
		response.setStatus(HttpStatus.NO_CONTENT.value());
		return;		
	}

}
