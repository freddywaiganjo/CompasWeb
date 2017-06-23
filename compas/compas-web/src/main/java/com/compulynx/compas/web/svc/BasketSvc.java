/**
 * 
 */
package com.compulynx.compas.web.svc;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.impl.BasketBalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Basket;

/**
 * @author Anita
 *
 */
@Component
@Path("/basket")
public class BasketSvc {
	@Autowired
	BasketBalImpl basketBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtBaskets/")
	public Response GetBaskets() {
		List<Basket> baskets = basketBal.GetAllBaskets();
		return Response.status(200).entity(baskets).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updBasket")
	public Response UpdatePartner(Basket basket) {
		CompasResponse response = basketBal.UpdateBasket(basket);
		return Response.status(200).entity(response).build();
	}
}
