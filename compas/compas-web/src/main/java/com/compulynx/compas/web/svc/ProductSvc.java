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

import com.compulynx.compas.bal.impl.ProductBalImpl;
import com.compulynx.compas.models.AgeGroups;
import com.compulynx.compas.models.BeneficiaryGroup;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Product;
import com.compulynx.compas.models.Zone;


/**
 * @author shree
 *
 */
@Component
@Path("/product")
public class ProductSvc {
	@Autowired
	ProductBalImpl productBal;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtProducts/")
	public Response GetProducts() {
		try {
			List<Product> merchants = productBal.GetProducts();
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
	@Path("/updProduct/")
	public Response UpdateProduct(Product product) {
		try {
			CompasResponse response = productBal.UpdateProduct(product);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtBnfGrps")
	public Response GetBnfGrps() {
		try {
			List<BeneficiaryGroup> merchants = productBal.GetBnfGroups();
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
	@Path("/updBnfGrp/")
	public Response UpdateBnfGrp(BeneficiaryGroup bnfGrp) {
		try {
			CompasResponse response = productBal.UpdateBnfGroup(bnfGrp);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtAgeGroups/")
	public Response GetAgeGroups() {
		try {
			List<AgeGroups> agegroups = productBal.GetAgeGroups();
			if (!(agegroups == null)) {
				return Response.status(200).entity(agegroups).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtAgeGroupsByBnfGrp/{bnfgrpId}")
	public Response GetAgeGroupsByBnfGrp(@PathParam("bnfgrpId") int bnfgrpId) {
		try {
			List<Location> agegroups = productBal.GetAgeGroupsByBnfGrp(bnfgrpId);
			if (!(agegroups == null)) {
				return Response.status(200).entity(agegroups).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}//
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updLocAgeGrpDtl/")
	public Response UpdateLocAgeGrp(BeneficiaryGroup bnfGrp) {
		try {
			CompasResponse response = productBal.UpdateLocAgeGrp(bnfGrp);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
}
