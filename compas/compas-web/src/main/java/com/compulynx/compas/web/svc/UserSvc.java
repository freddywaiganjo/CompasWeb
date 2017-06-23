/**
 * 
 */
package com.compulynx.compas.web.svc;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.logging.Logger;

import com.compulynx.compas.bal.impl.UserBalImpl;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Customer;
import com.compulynx.compas.models.SocketRequestVO;
import com.compulynx.compas.models.TmsRetailersDownload;
import com.compulynx.compas.models.TmsTransactionMst;
import com.compulynx.compas.models.TmsUserDownload;
import com.compulynx.compas.models.TransactionVo;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.UserGroup;
import com.compulynx.compas.models.android.AndroidDownloadVo;
import com.compulynx.compas.models.android.AndroidTopup;
import com.compulynx.compas.models.android.AndroidTransaction;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Anita
 *
 */

@Component
@Path("/user")
public class UserSvc {

	@Autowired
	UserBalImpl userBal;
	private static final Logger logger = Logger.getLogger(UserSvc.class
			.getName());

	// @GET
	// @Produces(MediaType.APPLICATION_JSON)
	// @Path("/gtUsers/")
	// public Response GetUsers() {
	// List<User> users = userBal.GetAllUsers();
	// return Response.status(200).entity(users).build();
	// }

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updUser")
	public Response UpdateUser(User user) {
		CompasResponse response = userBal.UpdateUser(user);
		return Response.status(200).entity(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtQuestions/")
	public Response GetQuestions() {
		List<User> questions = userBal.GetQuestions();
		return Response.status(200).entity(questions).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtBranchAgents/{branchId}")
	public Response GetBranchAgents(@PathParam("branchId") int branchId) {
		List<Agent> agents = userBal.GetBranchAgents(branchId);
		return Response.status(200).entity(agents).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtUsers/")
	public Response GetUsers() {
		try {
			List<User> users = userBal.GetAllUsers();
			if (!(users == null)) {
				return Response.status(200).entity(users).build();
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
	@Path("/gtClasses/")
	public Response GetClasses() {
		try {
			List<User> classes = userBal.GetClasses();
			if (!(classes == null)) {
				return Response.status(200).entity(classes).build();
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
	@Path("/gtTmsUsers/{pos_id}")
	public Response GetTmsUsers(@PathParam("pos_id") String pos_id) {

		List<TmsUserDownload> questions = userBal.GetTmsUserDownload(pos_id);
		return Response.status(200).entity(questions).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtTmsRetailers/{pos_id}")
	public Response GetTmsRetailers(@PathParam("pos_id") String pos_id) {

		TmsRetailersDownload questions = userBal.GetTmsRetailerDownload(pos_id);
		return Response.status(200).entity(questions).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ "text/plain", MediaType.APPLICATION_JSON })
	@Path("/updTmsTrans")
	public Response UpdateTmsTrans(String trans) {
		String request = "";
		System.out.println("response" + trans);
		JsonElement jelement = new JsonParser().parse(trans);
		JsonArray jArray = jelement.getAsJsonArray();
		List<TmsTransactionMst> transList = new ArrayList<TmsTransactionMst>();

		for (int i = 0; i < jArray.size(); i++) {
			JsonObject jobject = (JsonObject) jArray.get(i);
			TmsTransactionMst tr = new Gson().fromJson(jobject,
					TmsTransactionMst.class);

			transList.add(tr);
		}

		System.out.println("size##=" + transList.size());
		System.out.println("sizeTransList##="
				+ transList.get(0).commodities.size());

		//
		String response = userBal.UplodTmsTrans(transList);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updAndroidTrans")
	public Response UpdateAndroidTrans(List<TmsTransactionMst> trans) {

		System.out.println("size##=" + trans.size());
		System.out
				.println("sizeTransList##=" + trans.get(0).commodities.size());

		//
		String response = userBal.UplodTmsTrans(trans);
		return Response.status(200).entity(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtGroupsByUserType/{userTypeId}")
	public Response GetGroupsByUserTypes(@PathParam("userTypeId") int userTypeId) {
		List<UserGroup> agents = userBal.GetGroupsByUserType(userTypeId);
		return Response.status(200).entity(agents).build();
	}

	@POST
	// @Consumes({"text/plain",MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/downloadVo")
	public Response GetAndroidDownloadData(String macAddress) {
		logger.info("Mac Address Request Received From Mobile$$$" + macAddress);

		AndroidDownloadVo downloadVo = userBal
				.GetAndroidDownloadData(macAddress);
		if (downloadVo != null) {
			return Response.status(200).entity(downloadVo).build();
		} else
			return Response.status(202).entity(null).build();
	}

	@POST
	// @Consumes({"text/plain",MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/downloadBnf")
	public Response DownloadBeneficiaries(String macAddress) {
		logger.info("Dwonloading beneficiaries$$");

		AndroidDownloadVo downloadVo = userBal.GetAndroidBeneficiary(macAddress);
		if (downloadVo != null) {
			return Response.status(200).entity(downloadVo).build();
		} else
			return Response.status(202).entity(null).build();
	}

	@POST
	// @Consumes({"text/plain",MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/downloadTopup")
	public Response DownloadTopupDeatils(String macAddress) {
		logger.info("Dwonloading TopupDetails$$");
		logger.info("MacAddress Received from POS$$" + macAddress);
		String str[] = macAddress.split("#");
		CompasResponse response= new CompasResponse();
		if (str[0].equalsIgnoreCase("200")) {
			 response = userBal
					.updateVoucherDownloadStatus(str[1]);
			return Response.status(200).entity(response).build();
		} else {
			AndroidDownloadVo downloadVo = userBal
					.GetAndroidTopupDetails(str[1]);
			if (downloadVo != null) {
				return Response.status(200).entity(downloadVo).build();
			}
		}

		return Response.status(202).entity(response).build();

	}

	@POST
	@Consumes({ "text/plain", MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/downloadTopupStatus")
	public Response DownloadTopupStatus(String status) {
		logger.info("Updating download status##" + status);
		System.out.println(status + "Received from POS");
		if (status.equalsIgnoreCase("200")) {

		} else {

		}
		return Response.status(202).entity(null).build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtUserById/{userId}")
	public Response GetUserById(@PathParam("userId") int userId) {

		try {

			User user = userBal.GetUserById(userId);

			if (!(user == null)) {

				return Response.status(200).entity(user).build();

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
	@Path("/gtLocationAgents/{locationId}")
	public Response GetLocationAgents(@PathParam("locationId") int locationId){
		List<Agent> agents = userBal.GetLocationAgents(locationId);
		return Response.status(200).entity(agents).build();
	}
}
