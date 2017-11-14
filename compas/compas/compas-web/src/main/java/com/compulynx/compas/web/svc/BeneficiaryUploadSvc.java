package com.compulynx.compas.web.svc;

import com.compulynx.compas.bal.impl.BeneficiaryUploadBalImpl;
import com.compulynx.compas.dal.operations.Utility;
import com.compulynx.compas.models.CompasProperties;
import com.compulynx.compas.models.CompasResponse;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.*;

/**
 * Created by Elly on 5/7/2015.
 */


@Component
@Path("/upload")
public class BeneficiaryUploadSvc {

	private static final Logger logger = Logger.getLogger(BeneficiaryUploadSvc.class.getName());

    String TOMCAT_HOME ="";
    String fileName="";
    String PATH = "";
    CompasResponse response=null;

    @Autowired
    BeneficiaryUploadBalImpl bnfUploadBal;

   
    @POST
    @Path("/uploadbeneficiary")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("createdBy") String createdBy,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {

        TOMCAT_HOME = System.getProperty("catalina.base");
        fileName = fileDetail.getFileName();
        PATH = TOMCAT_HOME+"/evoucher_uploads/";
        logger.info("Uploading file now Backend##"+PATH+fileName);
        String uploadedFileLocation = PATH + fileName;

        // save it
        if(writeToFile(uploadedInputStream, uploadedFileLocation)){

            response= new CompasResponse();

            response = bnfUploadBal.UploadAccount(uploadedFileLocation,createdBy);
        } else{
            return Response.status(200).entity(new CompasResponse(201,"Error uploading file, Please try again")).build();
        }

        //String output = "File uploaded to : " + uploadedFileLocation;
        return Response.status(200).entity(response).build();
    }

    
    @POST
    @Path("/uploadhhdetails")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadHHDetails(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("createdBy") String createdBy,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("locationId") int locationId) {

    	CompasProperties compasProperties =new CompasProperties();
    	Utility util = new Utility();
    	String param="upload.filepath";
		compasProperties=util.getCompasProperties(param);
		logger.info("Done Reading Props File##"+compasProperties.bnfUploadFilePath);
        TOMCAT_HOME = System.getProperty("catalina.base");
        fileName = fileDetail.getFileName();
        PATH = compasProperties.bnfUploadFilePath;
        logger.info("Uploading file now Backend##"+PATH+fileName);
        String uploadedFileLocation = PATH + fileName;
        // save it
        if(writeToFile(uploadedInputStream, uploadedFileLocation)){

            response= new CompasResponse();

            response = bnfUploadBal.UploadHHDetails(uploadedFileLocation,createdBy,locationId);
        } else{
            return Response.status(200).entity(new CompasResponse(201,"Error uploading file, Please try again")).build();
        }

        //String output = "File uploaded to : " + uploadedFileLocation;
        return Response.status(200).entity(response).build();
    }



    // save uploaded file to new location
    private boolean writeToFile(InputStream uploadedInputStream,
                             String uploadedFileLocation) {
    	logger.info("#Writing to file function##");
        try {
            OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();

            logger.info("##Sucessfully written to file");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("##Failed to write to file");
        return false;
    }

}
