/**
 * 
 */
package com.compulynx.compas.web.svc;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.impl.ProgrammeBalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Voucher;

/**
 * @author Anita
 *
 */
@Component
@Path("/programme")
public class ProgrammeSvc {
	
	@Autowired
	ProgrammeBalImpl programmeBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtProgrammes/")
	public Response GetPartners() {
		List<Programme> programmes = programmeBal.GetAllProgrammes();
		return Response.status(200).entity(programmes).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updProgramme")
	public Response UpdatePartner(Programme programme) {
		CompasResponse response = programmeBal.UpdateProgramme(programme);
		return Response.status(200).entity(response).build();
	}
	
	
}
