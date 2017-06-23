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

import com.compulynx.compas.bal.impl.ServiceBalImpl;
import com.compulynx.compas.models.ClientRequest;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Currency;
import com.compulynx.compas.models.Params;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.ServiceDtlVO;
import com.compulynx.compas.models.Subservice;
import com.compulynx.compas.models.Uom;
import com.compulynx.compas.models.Voucher;

/**
 * @author Anita
 *
 */
@Component
@Path("/service")
public class ServiceSvc {

	@Autowired
	ServiceBalImpl	serviceBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtServices/")
	public Response GetServices() {
		List<Service> services = serviceBal.GetAllServices();
		return Response.status(200).entity(services).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtParams/")
	public Response GetParams() {
		List<Params> params = serviceBal.GetAllParams();
		return Response.status(200).entity(params).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updService")
	public Response UpdateService(Service service) {
		CompasResponse response = serviceBal.UpdateService(service);
		return Response.status(200).entity(response).build();
	}

	

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updParam")
	public Response UpdateParam(Params param) {
		CompasResponse response = serviceBal.UpdateParam(param);
		return Response.status(200).entity(response).build();
	}


	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/sync")
	public Response GetSubServices(ClientRequest request) {
		System.out.println("Fetch SubServices###"+request.key+"###"+request.page);
		if (request.key.equalsIgnoreCase("subservices")) {
			
			List<Subservice> services = serviceBal.getAllSubservices(request.page);
			if (services != null) {
				System.out.println("SubServices###");
				
				return Response.status(200).entity(services).build();
			}
		}
		if (request.key.equalsIgnoreCase("params")) {
			List<Params> params = serviceBal.getParams(0,request.page);
			if (params != null) {
				return Response.status(200).entity(params).build();
			}
		}
		if (request.key.equalsIgnoreCase("servicedtls")) {
			List<ServiceDtlVO> services = serviceBal.getAllServiceDtls(request.page);
			if (services != null) {
				return Response.status(200).entity(services).build();
			}
		}
		return null;
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtCurrencies/")
	public Response GetCurrencies() {
		List<Currency> types = serviceBal.GetCurrencies();
		return Response.status(200).entity(types).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtUoms/")
	public Response GetUoms() {
		List<Uom> uoms = serviceBal.GetUoms();
		return Response.status(200).entity(uoms).build();
	}//
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtVoucherServices/{programmeId},{locationId}")
	public Response GetVoucherServices(@PathParam("programmeId") int programmeId,@PathParam("locationId") int locationId) {
		List<Voucher> uoms = serviceBal.GetVoucherServices(programmeId,locationId);
		return Response.status(200).entity(uoms).build();
	}
	

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updPriceConfig")
	public Response UpdatePriceConfig(Programme prg) {
		CompasResponse response = serviceBal.UpdatePriceConfig(prg);
		return Response.status(200).entity(response).build();
	}
}
