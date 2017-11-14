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

import com.compulynx.compas.bal.impl.DeviceBalImpl;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.License;

/**
 * @author Anita
 *
 */
@Component
@Path("/device")
public class DeviceSvc {

	@Autowired
	DeviceBalImpl deviceBal;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtDevices")
	public Response GetDevices() {
			List<Device> devices = deviceBal.GetAllDevicesInfo();
			return Response.status(200).entity(devices).build();
	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updDevice")
	public Response UpdateDevice(Device device) {
			CompasResponse response = deviceBal.UpdateDeviceInfo(device);
			return Response.status(200).entity(response).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtIssueDevices/{classId},{merchantId}")
	public Response GetIssueDevices(@PathParam("classId") int classId,
			@PathParam("merchantId") int merchantId) {
			List<Device> issueDevices = deviceBal.GetAllIssueDevicesInfo(classId,merchantId);
			return Response.status(200).entity(issueDevices).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updIssueDevice")
	public Response UpdateIssueDevice(Device device) {
			CompasResponse response = deviceBal.UpdateIssueDeviceInfo(device);
			return Response.status(200).entity(response).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveAgents/{branchId}")
	public Response GetActiveAgents(@PathParam("branchId") int branchId) {
			List<Agent> agents = deviceBal.GetActiveAgents(branchId);
			return Response.status(200).entity(agents).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveDevices")
	public Response GetActiveDevices() {
			List<Device> devices = deviceBal.GetActiveDevicesInfo();
			return Response.status(200).entity(devices).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtLicense")
	public Response GetLicense() {
			List<License> devices = deviceBal.GetLicense();
			return Response.status(200).entity(devices).build();
	}

}
