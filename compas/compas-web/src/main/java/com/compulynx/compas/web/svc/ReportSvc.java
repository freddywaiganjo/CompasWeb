//package org.compulynx.shp.web.svc;
//
//import java.util.List;
//
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import org.compulynx.shp.bal.impl.ReportsBalImpl;
//import org.compulynx.shp.models.Cover;
//import org.compulynx.shp.models.Provider;
//import org.compulynx.shp.models.transactions.TransMemberInfo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//@Path("/report")
//public class ReportSvc {
//
//	@Autowired
//	ReportsBalImpl reportBal;
//
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/gtTransMembers/{brokerId}")
//	public Response GetTransMemberList(@PathParam("brokerId") int brokerId) {
//		try {
//			List<TransMemberInfo> members = reportBal.GetTransMemberList(brokerId);
//			if (!(members == null)) {
//				return Response.status(200).entity(members).build();
//			} else {
//				return Response.status(201).entity(null).build();
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return Response.status(404).entity(null).build();
//		}
//	}
//	
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/gtTransProviders/{brokerId}")
//	public Response GetProvidersByBrokerId(@PathParam("brokerId") int brokerId) {
//		try {
//			List<Provider> providers = reportBal.GetProvidersByBrokerId(brokerId);
//			if (!(providers == null)) {
//				return Response.status(200).entity(providers).build();
//			} else {
//				return Response.status(201).entity(null).build();
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return Response.status(404).entity(null).build();
//		}
//	}
//	
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/gtTransCovers/{brokerId}")
//	public Response GetCoversByBrokerId(@PathParam("brokerId") int brokerId) {
//		try {
//			List<Cover> covers = reportBal.GetCoversByBrokerId(brokerId);
//			if (!(covers == null)) {
//				return Response.status(200).entity(covers).build();
//			} else {
//				return Response.status(201).entity(null).build();
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return Response.status(404).entity(null).build();
//		}
//	}
//}
