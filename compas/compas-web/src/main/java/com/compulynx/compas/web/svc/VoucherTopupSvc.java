/**
 * 
 */
package com.compulynx.compas.web.svc;

import java.util.List;
import java.util.logging.Logger;

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

import com.compulynx.compas.bal.impl.CustomerBalImpl;
import com.compulynx.compas.bal.impl.TmsBalImpl;
import com.compulynx.compas.bal.impl.VoucherTopupBalImpl;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.BeneficiaryGroup;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Customer;
import com.compulynx.compas.models.Topup;
import com.compulynx.compas.models.TopupLimit;
import com.compulynx.compas.models.User;

/**
 * @author Anita
 *
 */
@Component
@Path("/vouchertopup")
public class VoucherTopupSvc {
	@Autowired
	VoucherTopupBalImpl	topupBal;


	private static final Logger logger = Logger.getLogger(CustomerSvc.class.getName());
	/**
	 * gets list of vouchers to topup then insert in topup table
	 * @param topup
	 * @return
     */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/topup")
	public Response doTopup(Topup topup) {
		logger.info("Topup##"+topup.programmes);
		CompasResponse response=new CompasResponse();
		response = topupBal.getTopupVouchers(topup);
		return Response.status(200).entity(response).build();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/voucher_download/{pos_id}")
	public Response getVouchers(@PathParam("pos_id")String posId) {
		String test= topupBal.buildDownloadVouchers(posId);
		return Response.status(200).entity(test).build();
	}


	@POST
	@Consumes({"text/plain",MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update_voucher_download_status/")
	public Response updateVoucherStatus(String posId) {
		logger.info("Mac Address Request Received##"+posId);

		CompasResponse response= topupBal.updateVoucherDownloadStatus(posId);
		return Response.status(200).entity(response).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtBnfgrpsForTopup/")
	public Response getBngGrpsForTopup() {
		List<BeneficiaryGroup> bnfGrps= topupBal.GetBnfGrpsForTopup();
		return Response.status(200).entity(bnfGrps).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtBnfgrpsTopuped/")
	public Response getBngGrpsTopuped() {
		List<BeneficiaryGroup> bnfGrps= topupBal.GetTopupedBnfGrps();
		return Response.status(200).entity(bnfGrps).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtBnfgrpsByPrgId/{programmeId}")
	public Response getBnfGrpsByPrgId(@PathParam("programmeId")int programmeId) {
		List<BeneficiaryGroup> bnfGrps= topupBal.GetBnfGrpsByPrgId(programmeId);
		return Response.status(200).entity(bnfGrps).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtBnfgrpsByLocId/{locationId}")
	public Response getBnfGrpsByLocId(@PathParam("locationId")int locationId) {
		List<BeneficiaryGroup> bnfGrps= topupBal.GetBnfGrpsByLocationId(locationId);
		return Response.status(200).entity(bnfGrps).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/topupdetails")
	public Response GetTopupDetails(BeneficiaryGroup bnfGrp) {
		logger.info("get Topup Details to approve##");
		List<BeneficiaryGroup> topups= topupBal.GetTopupDetailsToApprove(bnfGrp);
		
		return Response.status(200).entity(topups).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updTopupStatus/")
	public Response updateTopupStatus(Topup topup) {
		CompasResponse response= topupBal.UpdateTopupStatus(topup);
		return Response.status(200).entity(response).build();
	}//
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtVendorsByLocId/{locationId}")
	public Response getVendorsByLocId(@PathParam("locationId")int locationId) {
		List<Agent> bnfGrps= topupBal.GetAllAgentsByLoc(locationId);
		return Response.status(200).entity(bnfGrps).build();
	}
	
	/**
	 * Topup Limit
	 */
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtLimits")
	public Response GetTopupLimits() {
		List<TopupLimit> limits= topupBal.GetTopupLimits();
		return Response.status(200).entity(limits).build();
	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updLimit/")
	public Response updateTopupLimit(TopupLimit limit) {
		CompasResponse response= topupBal.UpdateLimit(limit);
		return Response.status(200).entity(response).build();
	}//
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtwebusers")
	public Response GetWebUsers() {
		List<User> limits= topupBal.GetWebUsers();
		return Response.status(200).entity(limits).build();
	}
	
}
