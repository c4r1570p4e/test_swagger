package org.cl.rest.jersey.controller;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import lombok.Setter;

import org.cl.rest.jersey.dao.RecetteDao;
import org.cl.rest.jersey.domain.Recette;

import com.google.common.base.Strings;

@Singleton
@Path("recettes")
@Produces({ "application/json" })
public class WebController {

	@Context
	private UriInfo uriInfo;

	@Inject
	@Setter
	private RecetteDao recetteDao;

	public WebController() {
		super();
	}

	@GET
	public List<Recette> getRecettes() {
		return recetteDao.getListRecettes();
	}

	@POST
	@Consumes({ "application/json" })
	public Response createNewRecette(Recette recette) {
		
		if(recette == null || Strings.isNullOrEmpty(recette.getLibelle())) {
			return Response.status(Status.BAD_REQUEST).build(); 
		}

		String id = recetteDao.create(recette);

		URI recetteUri = uriInfo.getAbsolutePathBuilder().path(id).build();

		return Response.created(recetteUri).build();

	}
}
