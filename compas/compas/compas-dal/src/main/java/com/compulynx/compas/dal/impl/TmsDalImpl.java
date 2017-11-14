package com.compulynx.compas.dal.impl;

import com.compulynx.compas.dal.TmsDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.FtpFunctions;
import com.compulynx.compas.dal.operations.IConstants;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.tms.*;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Eli on 1/11/2016.
 */
public class TmsDalImpl implements TmsDal {
    private static final Logger log = Logger.getLogger(TmsDalImpl.class.getName());
    private DataSource datasource;
    private String message;
    ServerResponse serverResponse = null;


    public TmsDalImpl(DataSource datasource) {
        this.datasource = datasource;
    }



    @Override
    public ServerResponse uploadApplicationFile(String filePath,String fileName) {
        serverResponse = sendToFtp(filePath,fileName);
        return serverResponse;
    }

    /**
     * updates the firmware details
     * @param deviceFirmwareDetails
     * @return
     */
    @Override
    public ServerResponse updateFirmWareVersion(DeviceFirmwareDetails deviceFirmwareDetails) {
        String sql = "insert into tbl_device_firmware(deviceSerial,firmware_version) values(?,?)";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = datasource.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, deviceFirmwareDetails.getDeviceSerial());
            statement.setString(2, deviceFirmwareDetails.getFirmwareVersion());
            if(statement.executeUpdate() == 1)
                return new ServerResponse(200,"Firmware Update Successful");

        } catch (SQLException e) {
            log.log(Level.SEVERE,e.getMessage());
        } finally {
            DBOperations.DisposeSql(connection, statement, null);
        }
        return new ServerResponse(201,"Firmware Update Failed!!");
    }

    /**
     * Updates Device statistics
     * @param deviceStats
     * @return
     */
    @Override
    public ServerResponse updateDeviceStats(DeviceStats deviceStats) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = datasource.getConnection();
            statement = connection.prepareStatement(Queryconstants.insert_device_stats_query);
            statement.setString(1, deviceStats.getDeviceSerial());
            statement.setString(2, deviceStats.getBatteryLevel());
            statement.setString(3, deviceStats.getFirmwareVersion());
            statement.setString(4, deviceStats.getKernelVersion());
            statement.setString(5, deviceStats.getSignalStrength());
            statement.setString(6, deviceStats.getLatitude());
            statement.setString(7, deviceStats.getLongitude());
            statement.setString(8,deviceStats.getAgentId());
            statement.setString(9,deviceStats.getBatteryStatus());
            if(statement.executeUpdate() == 1)
                return new ServerResponse(200,"Device Stats Update Successful");

        } catch (SQLException e) {
            log.log(Level.SEVERE,e.getMessage());
        } finally {
            DBOperations.DisposeSql(connection, statement, null);
        }
        return new ServerResponse(201,"Device Stats Update Failed");
    }






    /**
     *
     *
     * @return
     */
    @Override
    public List<DeviceStats> fetchDeviceStats() {
        List<DeviceStats> deviceStats = new ArrayList<DeviceStats>();
        Connection connection=null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement=null;
        try{
            connection= datasource.getConnection();
            preparedStatement = connection.prepareStatement(Queryconstants.fetch_device_stats_query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                DeviceStats acc=new DeviceStats();
                acc.setId(resultSet.getInt("id"));
                acc.setAgentId(resultSet.getString("agent_id"));
                acc.setBatteryLevel(resultSet.getString("battery_level"));
                acc.setDeviceSerial(resultSet.getString("device_serial"));
                acc.setFirmwareVersion(resultSet.getString("firmware_version"));
                acc.setKernelVersion(resultSet.getString("kernel_version"));
                acc.setLatitude(resultSet.getString("latitude"));
                acc.setLongitude(resultSet.getString("longitude"));
                acc.setLastUpdated(resultSet.getString("last_updated"));
                acc.setSignalStrength(resultSet.getString("signal_strength"));
                acc.setBatteryStatus(resultSet.getString("battery_status"));
                acc.setIsOnline(getMinuteDifference(resultSet.getString("last_updated")) > 6 ? false : true);
                deviceStats.add(acc);
            }
        } catch(Exception ex){
            ex.printStackTrace();
        } finally {
            DBOperations.DisposeSql(connection, preparedStatement, resultSet);
        }
        return deviceStats;
    }


    @Override
    public DashboardDeviceStats fetchTmsDashboardStats() {
        DashboardDeviceStats acc=new DashboardDeviceStats();
        Connection connection=null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement=null;
        HashMap<String, String> hashMap=new HashMap<>();
        try{
            connection= datasource.getConnection();
            preparedStatement = connection.prepareStatement(Queryconstants.fetch_dashboard_stats);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                hashMap.put(resultSet.getString("name"),resultSet.getString("value"));
            }

            acc.setHashList(hashMap);

        } catch(Exception ex){
            ex.printStackTrace();
        } finally {
            DBOperations.DisposeSql(connection, preparedStatement, resultSet);
        }
        return acc;
    }

    @Override
    public ServerResponse generateLicense(TmsLicense tmsLicense) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = datasource.getConnection();
            statement = connection.prepareStatement(Queryconstants.insert_generated_license);
            statement.setString(1, tmsLicense.getLicenseDescription());
            statement.setString(2, FtpFunctions.generateLicenseNumber());
            if(statement.executeUpdate() == 1)
                return new ServerResponse(200,"License Generated Successfully!!");

        } catch (SQLException e) {
            log.log(Level.SEVERE,e.getMessage());
        } finally {
            DBOperations.DisposeSql(connection, statement, null);
        }
        return new ServerResponse(201,"License Generation Failed");
    }


    /**
     * check time difference
     * @param date
     * @return
     */
    private int getMinuteDifference(String date){
        int minutes=6;
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime time=fmt.parseDateTime(date);
        System.out.println(""+time.toString());
        DateTime today=new DateTime();
        minutes= Minutes.minutesBetween(time,today).getMinutes();
        System.out.println(Minutes.minutesBetween(time, time).getMinutes());
        return minutes;
    }


    /**
     *
     * @param filePath
     * @return
     */
    private ServerResponse sendToFtp(String filePath,String fileName){
        try{
            FtpFunctions ftp=new FtpFunctions(IConstants.SFTPHOST,
                    IConstants.SFTPPORT,IConstants.SFTPUSER,IConstants.SFTPPASS);

            ftp.uploadFTPFile(filePath,fileName,IConstants.SFTPWORKINGDIR);
            ftp.disconnect();
            return new ServerResponse(200,"Application uploaded Successfully!!");

        } catch(Exception ex){
            ex.printStackTrace();
            return new ServerResponse(201,"Error Uploading Application,Try Again!!");
        }
    }
}
