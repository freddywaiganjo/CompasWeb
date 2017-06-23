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

import com.compulynx.compas.bal.impl.MerchantBalImpl;
import com.compulynx.compas.bal.impl.OrganizationBalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.Organization;

/**
 * @author Anita
 *
 */
@Component
@Path("/merchant")
public class MerchantSvc {
	@Autowired
	MerchantBalImpl merchantBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMerchants/")
	public Response GetMerchants() {
		try {
			List<Merchant> merchants = merchantBal.GetMerchants();
			if (!(merchants == null)) {
				return Response.status(200).entity(merchants).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updMerchant/")
	public Response UpdateMerchant(Merchant merchant) {
		try {
			CompasResponse response = merchantBal.UpdateMerchant(merchant);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
}
