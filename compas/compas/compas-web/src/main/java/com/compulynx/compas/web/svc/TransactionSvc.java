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
import org.springframework.transaction.config.TxNamespaceHandler;

import com.compulynx.compas.bal.impl.TransactionBalImpl;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.SafComTxns;
import com.compulynx.compas.models.Transaction;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 *
 */
@Component
@Path("/transaction")
public class TransactionSvc {
	@Autowired
	TransactionBalImpl transactionBal;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updTransaction")
	public Response UpdateTransaction(List<Transaction> txn) {
		//System.out.println("txns");
			CompasResponse response = transactionBal.UpdateTransaction(txn);
			return Response.status(200).entity(response).build();
	}
	

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updSafComTxn")
	public Response UpdateSafComTxn(SafComTxns txn) {
		//System.out.println("txns");
			CompasResponse response = transactionBal.UpdateSafComTxn(txn);
			return Response.status(200).entity(response).build();
	}
	
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/gtSafComTxns/{cardnumber}")
//	public Response GetAllSafComTxns(@PathParam("cardnumber") String cardnumber) {
//		List<SafComTxns> txns =transactionBal.GetAllTxns(cardnumber);
//		return Response.status(200).entity(txns).build();
//	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtAgentList")
	public Response GetAgentList() {
		List<Agent> txns =transactionBal.GetAgentList();
		return Response.status(200).entity(txns).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtDeviceList")
	public Response GetDeviceList() {
		List<Device> txns =transactionBal.GetDeviceList();
		return Response.status(200).entity(txns).build();
	}

}
