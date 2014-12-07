package org.cl.rest.jersey.resource;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import lombok.Setter;

import org.cl.rest.jersey.dao.RecetteDao;
import org.cl.rest.jersey.domain.Recette;

import com.google.common.base.Strings;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import fj.data.Option;

@Singleton
@Path("recettes")
@Produces({ "application/json","application/xml" })
@Api(value = "/recettes", description = "Réferentiel de recettes de deserts")
public class RecetteResource {

	@Context
	private UriInfo uriInfo;

	@Inject
	@Setter
	private RecetteDao recetteDao;

	public RecetteController() {
		super();
	}

	@GET
	@ApiOperation
		(value = "Liste les recettes", notes = "Retrourne la liste de toutes les recettes de dessert.", 
		response=Recette.class, responseContainer="List")
	public List<Recette> getRecettes() {
		return recetteDao.getListRecettes();
	}

	@POST
	@Consumes({ "application/json","application/xml" })
	@ApiOperation(value="Créé une nouvelle recette.", notes="Permet de créer une nouvelle recette, l'id de la recette est affecté par l'API.")
	@ApiResponses(value = { 
		@ApiResponse(code = 400, message = "Si il n'y a pas de recette passée, ou si la recette n'a pas de libelle"),
		@ApiResponse(code = 201, message = "La recette a été enregistré. L'url de la ressource crée est retourné dans le header 'location'.")
		})
	public Response createNewRecette(@ApiParam(value="Recette à créer") Recette recette) {

		if (recette == null || Strings.isNullOrEmpty(recette.getLibelle())) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		String id = recetteDao.create(recette);

		URI recetteUri = uriInfo.getAbsolutePathBuilder().path(id).build();

		return Response.created(recetteUri).build();

	}

	@PUT
	@Path("/{id}")
	@Consumes({ "application/json","application/xml" })
	@ApiOperation(value="Mise à jour d'une recette.", 
		notes="Met à jour une recette. Il n'est pas possible de modifier l'identifiant de la recette.")
	@ApiResponses(value={
			@ApiResponse(code=204,message="La recette a été mise à jour."),
			@ApiResponse(code=400,message="Il n'y a pas de recette passé en body de la request, ou l'id ou le libelle sont absent, ou tentative de modification de l'id"),
			@ApiResponse(code=404,message="La recette à modifier n'existe pas.")
	})
	public Response updateRecette(@PathParam("id") String id, @ApiParam("recette modifiée") Recette recette) {

		if (id == null || recette == null || Strings.isNullOrEmpty(recette.getId())
				|| Strings.isNullOrEmpty(recette.getLibelle())) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		if (!id.equals(recette.getId())) {
			return Response.status(Status.BAD_REQUEST).entity("Impossible de modifier l'id d'une recette").build();
		}

		int nbMaj = recetteDao.update(recette);

		if (nbMaj == 1) {
			return Response.status(Status.NO_CONTENT).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@GET
	@Path("/{id}")
	@ApiOperation(value = "Pour obtenir une recette à partir de son id.", response = Recette.class)
	@ApiResponses({ 
		@ApiResponse(code = 400, message = "L'id passé est vide"), 
		@ApiResponse(code = 404, message = "Il n'y a pas de recette pour l'identifiant demandé") 
		})
	public Response getRecette(@PathParam("id") String id) {

		if (Strings.isNullOrEmpty(id)) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		Option<Recette> recette = this.recetteDao.get(id);

		if (recette.isSome()) {
			return Response.ok(recette.some()).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@GET
	@Path("/findByLibelle")
	@ApiOperation(value="Recherche les recettes par libelle", 
				notes =  "Recherche toute les recettes dont le libellé contient 'libellePart' passé en paramètre",
				response=Recette.class,
				responseContainer="List"
				)
	public List<Recette> findRecettesByLibelle(@QueryParam("libellePart") String libellePart) {
		return recetteDao.findByLibelle(libellePart);
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation(value="Suppression d'une recette", notes="supprime la recette par son id")
	@ApiResponses({
		 @ApiResponse(code=400, message="L'id de la recette est null ou vide."),
		 @ApiResponse(code=404, message="Il n'y a pas de recette correspondant cet id"),
		 @ApiResponse(code=204, message="La recette a été supprimé")
	})
	public Response deleteRecette(@PathParam("id") String id) {

		if (Strings.isNullOrEmpty(id)) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		if (this.recetteDao.delete(id) == 1) {
			return Response.status(Status.NO_CONTENT).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}	
	
}
