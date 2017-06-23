package com.compulynx.compas.web.svc;

import com.compulynx.compas.bal.impl.CustomerBalImpl;
import com.compulynx.compas.bal.impl.TmsBalImpl;
import com.compulynx.compas.models.*;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.logging.Logger;

@Component
@Path("/member")
public class CustomerSvc {
	@Autowired
	CustomerBalImpl	memberBal;

	@Autowired
	TmsBalImpl tmsBal;

	private static final Logger logger = Logger.getLogger(CustomerSvc.class.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMembers/{locationId}")
	public Response GetMembers(@PathParam("locationId") int locationId) {
		try {
			List<Customer> members = memberBal.GetMembers(locationId);
			if (!(members == null)) {
				//System.out.println("memberlist: " + members.size());
				return Response.status(200).entity(members).build();
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
	@Path("/gtCustomers/")
	public Response GetCustomers() {
		try {
			List<Customer> members = memberBal.GetCustomers();
			if (!(members == null)) {
				//System.out.println("memberlist: " + members.size());
				return Response.status(200).entity(members).build();
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
	@Path("/gtVerifiedMembers/")
	public Response GetVerifiedMembers() {
		try {
			List<Customer> members = memberBal.GetVerifiedMembers();
			if (!(members == null)) {
				//System.out.println("memberlist: " + members.size());
				return Response.status(200).entity(members).build();
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
	@Path("/gtRegMembers/")
	public Response GetRegMembers() {
		try {
			List<Customer> members = memberBal.GetRegMembers();
			if (!(members == null)) {
				//System.out.println("memberlist: " + members.size());
				return Response.status(200).entity(members).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	////get member details by bioId
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtbioMember")
	public Response GetMemberBio(@QueryParam("bioId") int bioId) {
		try {
			Customer member = memberBal.GetBioMembers(bioId);
			if (!(member == null)) {
				
				return Response.status(200).entity(member).build();
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
	@Path("/gtWallets/")
	public Response GetWallets() {
		try {
			List<Wallet> wallets = memberBal.GetWallets();
			if (!(wallets == null)) {

				return Response.status(200).entity(wallets).build();
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
	@Path("/updCustomer")
	public Response UpdateCustomer(Customer customer) {
		try {
			CompasResponse response = memberBal.UpdateMember(customer);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updAndCustomer")
	public Response UpdateAndCustomer(List<Customer> customer) {
		try {
			CompasResponse response = memberBal.UpdateAndMember(customer);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMemberCi")
	public Response GetCardIssuanceList() {
		try {
			List<Customer> members = memberBal.GetCardIssuanceList();
			if (!(members == null)) {
				System.out.println("memberlist: " + members.size());
				return Response.status(200).entity(members).build();
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
	@Path("/gtCustomerCi")
	public Response GetCustomerCardIssuanceList() {
		try {
			List<Customer> members = memberBal.GetCustCardIssuanceList();
			if (!(members == null)) {
				System.out.println("memberlist: " + members.size());
				return Response.status(200).entity(members).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	// @POST
	// @Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Path("/printCard1")
	// public Response PrintCard(Member member) {
	// try {
	// CardInput cardInput = new CardInput();
	// cardInput.setCard_number(member.memberNo);
	// cardInput.setCard_serial_number("333");
	// cardInput.setHousehold_name("");
	// cardInput.setHousehold_uuid(member.memberNo);
	// cardInput.setInterventions(new int[1]);
	// cardInput.setPin_number("333");
	//
	// Recipient recipient = new Recipient();
	// recipient.setPicture(member.memberPic);
	// recipient.setFirst_name(member.firstName);
	// recipient.setLast_name(member.surName);
	// recipient.setGender("");
	// recipient.setBirthdate(member.dateOfBirth);
	//
	// cardInput.setRecipient(recipient);
	// System.out.println(cardInput.toString());
	// org.compulynx.shp.web.cardapi.PrintCard printCard = new
	// org.compulynx.shp.web.cardapi.PrintCard();
	// String outputCard = printCard.printOnly(cardInput);
	// if(outputCard != "" || outputCard != null)
	// {
	// JsonObject o = new JsonParser().parse(outputCard).getAsJsonObject();
	// Gson gson = new Gson();
	// String responseStr = gson.fromJson(o.get("response").toString(),
	// String.class);
	// JsonParser jsonParser = new JsonParser();
	// JsonObject jo = (JsonObject) jsonParser.parse(responseStr);
	// if(jo.get("code").toString().equalsIgnoreCase("200"))
	// {
	// // ShpResponse shpResponse = memberBal.UpdateMember(member)
	// member.cardStatus = "P";
	// ShpResponse response = memberBal.UpdateCardStatus(member);
	// return Response.status(response.respCode).entity(response).build();
	// }
	// else
	// {
	// ShpResponse response = new ShpResponse(202,
	// jo.get("response").toString());
	// return Response.status(response.respCode).entity(response).build();
	// }
	// }
	// return Response.status(500).entity("Failed").build();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// return Response.status(404).entity(null).build();
	// }
	// }
	//
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMemberCa/")
	public Response GetCardActivationList() {
		try {
			List<Customer> members = memberBal.GetCardActivationList();
			if (!(members == null)) {
				System.out.println("memberlist: " + members.size());
				return Response.status(200).entity(members).build();
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
	@Path("/gtMemberCb")
	public Response GetCardBlockList() {
		try {
			List<Customer> members = memberBal.GetCardBlockList();
			if (!(members == null)) {
				System.out.println("memberlist: " + members.size());
				return Response.status(200).entity(members).build();
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
	@Path("/updCardStatus")
	public Response UpdateCardStatus(Customer member) {
		try {
			CompasResponse response = memberBal.UpdateCardStatus(member);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	/*
	 * Update card Link idUpdateCardStatus
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updatecardlinkid")
	public Response updateCardLinkId(MemberCard memCard) {
		Personalize response = memberBal.updateMemberCardLinkId(memCard);
		return Response.status(200).entity(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getunissuedcards")
	public Response getListofCards() {
		List<MemberCard> cards = memberBal.getListOfCards();
		if (!(cards == null)) {
			return Response.status(200).entity(cards).build();
		} else {
			return Response.status(201).entity(null).build();
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getunprintedcards")
	public Response getListofCardsToPrint() {
		List<Customer> cards = memberBal.getListOfCardsToPrint();
		if (!(cards == null)) {
			return Response.status(200).entity(cards).build();
		} else {
			return Response.status(201).entity(null).build();
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/printCard")
	public Response printCard(MemberCard memberCard) {
		System.out.println("caling service");
		try {
			// Member member =
			// memberBal.GetMember(memberCard.memberId);
			String urlStr = "http://localhost:8666/Print/CEService/printfrontonly?data=" + "000000000" + memberCard.memberId
					+ ";FAMILY BANK DEMO;" + memberCard.cardNumber;
			System.out.println(urlStr);
			URL url = new URL(urlStr.replace(" ", "%20"));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return Response.status(200).entity(null).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updWallet")
	public Response UpdateWallet(Organization org) {
		try {
			CompasResponse response = memberBal.UpdateWallet(org);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/insertCardIssuance")
	public Response UpdateCardIssuance(MemberCard memCard) {
		CompasResponse response = memberBal.UpdateCardIssuance(memCard);
		return Response.status(200).entity(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtLoadCardInfo/{cardNumber}")
	public Response GetLoadCardDetails(@PathParam("cardNumber") String cardNumber) {
		try {
			MemberCard card = memberBal.GetLoadWalletCardDetails(cardNumber);
			if (!(card == null)) {

				return Response.status(200).entity(card).build();
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
	@Path("/updCardLoad")
	public Response UpdateCardLoad(MemberCard card) {
		try {
			CompasResponse response = memberBal.UpdateCardLoad(card);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updCustomerEnr")
	public Response UpdateCustomerEnr(Customer customer) {
		try {
			CompasResponse response = memberBal.UpdateCustomerEnr(customer);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/verifyCustomerEnr")
	public Response VerifyCustomerEnr(Customer customer) {
		try {
			String afisIp = "192.168.13.226";
			String afisPort = "8192";
			String capturePosition = "";
			byte[] capturedImage = baseToByte(customer.bioImage);
			CompasResponse response = memberBal.isImageDBDistinct(capturedImage, capturePosition, afisIp, afisPort);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	private static byte[] baseToByte(String base) {
		byte[] img = new byte[400];
		if (base.length() < 800) {
			img = org.apache.commons.codec.binary.Base64.decodeBase64(base.getBytes());
			return img;
		} else {
			String[] baseSplit = base.split("==");
			img = org.apache.commons.codec.binary.Base64.decodeBase64(baseSplit[0].getBytes());
			return img;
		}
	}
	

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtPrgBalance/")
	public Response GetProgrammeBalance(Customer customer) {
		try {
			System.out.println("enpoint");
			List<ProgrammeValue> values= memberBal.GetCardBalance(customer.cardNumber);
			if (!(values == null)) {

				System.out.println("values"+values);
				return Response.status(200).entity(values).build();
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
	@Path("/updcardreissue")
	public Response updateCardReIssue(MemberCard memCard) {
		CompasResponse response = memberBal.updateCardReissue(memCard);
		return Response.status(200).entity(response).build();
	}

	/**
	 * receives socket request
	 * @param requestContext servlet context
	 * @return response from socket
	 */
	@POST
	@Path("/socket_ops/")
	@Consumes({"text/plain",MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response connectBioSocket(@Context HttpServletRequest requestContext,
									 String socketRequest) {
		String ipClient=requestContext.getRemoteAddr();
		System.out.println("ClientiP##"+ipClient);
		logger.info("Request$$"+socketRequest);
		String request = "";
		SocketRequestVO socketRequestVO= new Gson().fromJson(socketRequest,SocketRequestVO.class);
		if(socketRequestVO.getRequestType().trim().equals("BIO")){
			logger.info("Bio Request Came through##");
			BioSocketRequestVO bioRequestVO= new Gson().fromJson(new JsonParser().parse(socketRequest)
					.getAsJsonObject().get("request")
					.getAsJsonObject(),BioSocketRequestVO.class);
			logger.info("##\t"+bioRequestVO.getRequest());
			logger.info("##\t"+bioRequestVO.getRequestType());

			CompasProperties compasProperties = tmsBal.getCompasProperties();
			logger.info("Done Reading Props File##"+compasProperties.getAfis_ip()+"\t"+compasProperties.getAfis_port());
			if (compasProperties==null){
				logger.info("##Failed to read properties file##");
				return Response.status(Response.Status.OK).entity("").build();
			}
			socketRequestVO.setAfisIp(compasProperties.getAfis_ip());
			socketRequestVO.setAfisPort(compasProperties.getAfis_port());

			request = new Gson().toJson(socketRequestVO).toString();
			logger.info("Final Bio Request##"+request);

			String result = initSocketConnection(ipClient,request);
			logger.info("Bio Socket Response##\t"+result);
			return Response.status(Response.Status.OK).entity(result).build();
		}

		if(socketRequestVO.getRequestType().trim().equals("CARD")){
			logger.info("Card Request Came through##");
			CardSocketRequestVO cardRequestVO= new Gson().fromJson(new JsonParser().parse(socketRequest)
					.getAsJsonObject().get("request")
					.getAsJsonObject(),CardSocketRequestVO.class);
			logger.info("##\t"+cardRequestVO.getRequest());
			logger.info("##\t"+cardRequestVO.getRequestType());
			request = new Gson().toJson(socketRequestVO).toString();
			logger.info("Final Card Request##"+request);

			String result = initSocketConnection(ipClient,request);
			logger.info("Card Socket Response##\t"+result);

			return Response.status(Response.Status.OK).entity(result).build();
		}

		return Response.status(Response.Status.OK).entity("unknown request type {can be BIO, CARD, PRINT}").build();
	}

	/**
	 *
	 * @param clientIp
	 * @return
	 */
	private String initSocketConnection(String clientIp,String socketRequestVO){
		String response="";
		Socket socket=null;
		PrintWriter out = null;
//        ObjectOutputStream out =null;
		BufferedReader in = null;
		try{
			logger.info(clientIp);
			socket = new Socket(clientIp, 8123);
			out = new PrintWriter(socket.getOutputStream(), true);
//            out = new ObjectOutputStream(socket.getOutputStream());
//            out.writeObject(socketRequestVO);
			out.println(socketRequestVO);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String str = socketRequestVO;
			InputStream is = new ByteArrayInputStream(str.getBytes());
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(is));
			String userInput;
			while ((userInput = stdIn.readLine()) != null) {
				System.out.println(userInput);
				String result=in.readLine();
				System.out.println("echo: " + result);
				response = result;
			}
			socket.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return response;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtProgrammesBnf/{bnfGrpId}")
	public Response GetProgrammesByBnfGrp(@PathParam("bnfGrpId") int bnfGrpId) {
		try {
			List<Programme> programmes = memberBal.GetProgrammesByBnfId(bnfGrpId);
			if (!(programmes == null)) {

				return Response.status(200).entity(programmes).build();
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
	@Path("/gtRelations/")
	public Response GetRelations() {
		List<Relations> relations = memberBal.GetRelations();
		return Response.status(200).entity(relations).build();
	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updBnfStatus")
	public Response UpdateBnfStatus(Customer customer) {
		try {
			CompasResponse response = memberBal.UpdateBnfStatus(customer);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}

	}

//	/**
//	 * gets list of vouchers to topup then insert in topup table
//	 * @param topup
//	 * @return
//     */
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Path("/topup")
//	public Response doTopup(Topup topup) {
//		logger.info("Topup##"+topup.programmes);
//		CompasResponse response=new CompasResponse();
//		response = memberBal.getTopupVouchers(topup);
//		return Response.status(200).entity(response).build();
//	}
//
//
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/voucher_download/{pos_id}")
//	public Response getVouchers(@PathParam("pos_id")String posId) {
//		String test= memberBal.downloadVouchers(posId);
//		return Response.status(200).entity(test).build();
//	}
//
//
//	@POST
//	@Consumes({"text/plain",MediaType.APPLICATION_JSON})
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/update_voucher_download_status/")
//	public Response updateVoucherStatus(String posId) {
//		logger.info("Mac Address Request Received##"+posId);
//
//		CompasResponse response= memberBal.updateVoucherDownloadStatus(posId);
//		return Response.status(200).entity(response).build();
//	}
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Path("/updPosCustomer")
//	public Response UpdatePosCustomer(Customer customer) {
//		try {
//			CompasResponse response = memberBal.UpdatePosCustomer(customer);
//			return Response.status(response.respCode).entity(response).build();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return Response.status(404).entity(null).build();
//		}
//	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/validateCustomerId")
	public Response ValidateCustomerId(Customer customer) {
		try {
			CompasResponse loginSession = memberBal.GetCustId(customer);
			return Response.status(loginSession.respCode).entity(loginSession)
					.build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
}
