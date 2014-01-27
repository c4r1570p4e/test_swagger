package org.cl.rest.jersey.controller;

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

import fj.data.Option;

@Singleton
@Path("recettes")
@Produces({ "application/json" })
public class RecetteController {

	@Context
	private UriInfo uriInfo;

	@Inject
	@Setter
	private RecetteDao recetteDao;

	public RecetteController() {
		super();
	}

	@GET
	public List<Recette> getRecettes() {
		return recetteDao.getListRecettes();
	}

	@POST
	@Consumes({ "application/json" })
	public Response createNewRecette(Recette recette) {

		if (recette == null || Strings.isNullOrEmpty(recette.getLibelle())) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		String id = recetteDao.create(recette);

		URI recetteUri = uriInfo.getAbsolutePathBuilder().path(id).build();

		return Response.created(recetteUri).build();

	}

	@PUT
	@Path("{id}")
	@Consumes({ "application/json" })
	public Response updateRecette(@PathParam("id") String id, Recette recette) {

		if (id == null || recette == null || Strings.isNullOrEmpty(recette.getId())
				|| Strings.isNullOrEmpty(recette.getLibelle())) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		if (!id.equals(recette.getId())) {
			return Response.status(Status.BAD_REQUEST).entity("Impossible de modifier l'id d'une recette").build();
		}

		int nbMaj = recetteDao.update(recette);

		if (nbMaj == 1) {
			return Response.ok().build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@GET
	@Path("{id}")
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
	@Path("findByLibelle")
	public List<Recette> findRecettesByLibelle(@QueryParam("libellePart") String libellePart) {
		return recetteDao.findByLibelle(libellePart);
	}

	@DELETE
	@Path("{id}")
	public Response deleteRecette(@PathParam("id") String id) {

		if (Strings.isNullOrEmpty(id)) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		if (this.recetteDao.delete(id) == 1) {
			return Response.status(Status.OK).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}	
	
}
