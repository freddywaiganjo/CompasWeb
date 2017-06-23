package com.compulynx.compas.web.svc;
import com.compulynx.compas.bal.impl.TmsBalImpl;
import com.compulynx.compas.models.tms.*;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Elkanah
 * <p>This is the main service entry point</p>
 */
@Component
@Path("/api")
public class TmsService {
    @Autowired
    TmsBalImpl tmsBal;

    ServerResponse serverResponse=null;

    private static final Logger log = Logger.getLogger(TmsService.class.getName());

    @POST
    @Path("/uploadapplication")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadApplicationFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
        String TOMCAT_HOME ="";
        String fileName="";
        String PATH = "";

        TOMCAT_HOME = System.getProperty("catalina.base");
        fileName = fileDetail.getFileName();
        PATH = TOMCAT_HOME+"/tms_files/";
        log.info("Uploading file now Backend##"+PATH+fileName);
        log.info("Type##"+fileDetail);
        String uploadedFileLocation = PATH + fileName;

        log.info("File Name##"+fileName);
        if(!(fileName.trim().split("\\.")[1].equalsIgnoreCase("DAT")) || fileName.length() ==0){
            return Response.status(200).entity(new ServerResponse(201,"Wrong file format uploaded, Please try again")).build();
        } else{
            // save it
            if(writeToFile(uploadedInputStream, uploadedFileLocation)){
                serverResponse= new ServerResponse();
                serverResponse = tmsBal.uploadApplicationFile(uploadedFileLocation,fileName);
            } else{
                return Response.status(200).entity(new ServerResponse(201,"Error uploading file, Please try again")).build();
            }

            return Response.status(200).entity(serverResponse).build();
        }



    }

    /**
     *
     * Pos Firmware Update
     * @param deviceFirmwareDetails
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/firmware_update/")
    public Response posFirmwareUpdate(DeviceFirmwareDetails deviceFirmwareDetails) {
        //log.info("firmware update acknowledgement##");
        ServerResponse response= tmsBal.updateFirmWareVersion(deviceFirmwareDetails);
        return Response.status(200).entity(response).build();
    }


    /**
     *
     * Pos Stats Update
     * @param deviceStats
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update_stats/")
    public Response updateDeviceStats(DeviceStats deviceStats) {
        //System.out.println("firmware update acknowledgement##");
        ServerResponse response= tmsBal.updateDeviceStats(deviceStats);
        return Response.status(200).entity(response).build();
    }

    /**
     * get all device statistics
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_device_stats")
    public Response getAllDevices(){
        List<DeviceStats> stats=new ArrayList<>();
        stats = tmsBal.fetchDeviceStats();
        return Response.status(200).entity(stats).build();
    }


    /**
     * get all dashboard device statistics
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_dash_stats")
    public Response getDashboardDeviceStats(){
        DashboardDeviceStats stats=new DashboardDeviceStats();
        stats = tmsBal.fetchTmsDashboardStats();
        return Response.status(200).entity(stats).build();
    }


    /**
     *
     * Generate License
     * @param tmsLicense
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/generate_license")
    public Response generateLicense(TmsLicense tmsLicense) {
        ServerResponse response= tmsBal.generateLicense(tmsLicense);
        return Response.status(200).entity(response).build();
    }


    /**
     * Write to file
     * @param uploadedInputStream
     * @param uploadedFileLocation
     * @return
     */
    private boolean writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
        System.out.println("#Writing to file function##");
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

            System.out.println("##Sucessfully written to file");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("##Failed to write to file");
        return false;
    }

}
