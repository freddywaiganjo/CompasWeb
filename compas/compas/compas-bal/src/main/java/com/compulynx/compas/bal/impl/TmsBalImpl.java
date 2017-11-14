package com.compulynx.compas.bal.impl;

import com.compulynx.compas.bal.TmsBal;
import com.compulynx.compas.dal.impl.TmsDalImpl;
import com.compulynx.compas.models.CompasProperties;
import com.compulynx.compas.models.tms.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by Eli on 1/11/2016.
 */
public class TmsBalImpl implements TmsBal {

    @Autowired
    private TmsDalImpl tmsDal;

    @Override
    public ServerResponse uploadApplicationFile(String filePath, String fileName) {
        return tmsDal.uploadApplicationFile(filePath,fileName);
    }

    @Override
    public ServerResponse updateFirmWareVersion(DeviceFirmwareDetails deviceFirmwareDetails) {
        return tmsDal.updateFirmWareVersion(deviceFirmwareDetails);
    }

    @Override
    public ServerResponse updateDeviceStats(DeviceStats deviceStats) {
        return tmsDal.updateDeviceStats(deviceStats);
    }

    @Override
    public List<DeviceStats> fetchDeviceStats() {
        return tmsDal.fetchDeviceStats();
    }

    @Override
    public DashboardDeviceStats fetchTmsDashboardStats() {
        return tmsDal.fetchTmsDashboardStats();
    }

    @Override
    public ServerResponse generateLicense(TmsLicense tmsLicense) {
        return tmsDal.generateLicense(tmsLicense);
    }

    @Override
    public CompasProperties getCompasProperties() {
        String path = System.getProperty("catalina.base") +"/conf/application.properties";
        Properties properties = new Properties();
        try{
            FileInputStream file;
            //load the file handle for main.properties
            file = new FileInputStream(path.trim());
            //load all the properties from this file
            properties.load(file);
            //we have loaded the properties, so close the file handle
            file.close();
            return new CompasProperties(properties.getProperty("afis.host").trim(),properties.getProperty("afis.port").trim());
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


}
