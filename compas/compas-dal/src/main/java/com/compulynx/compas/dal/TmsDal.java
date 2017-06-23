package com.compulynx.compas.dal;

import com.compulynx.compas.models.tms.*;

import java.util.List;

/**
 * Created by Eli on 1/11/2016.
 */
public interface TmsDal {

    public ServerResponse uploadApplicationFile(String filePath, String fileName);
    public ServerResponse updateFirmWareVersion(DeviceFirmwareDetails deviceFirmwareDetails);
    public ServerResponse updateDeviceStats(DeviceStats deviceStats);
    public List<DeviceStats> fetchDeviceStats();
    public DashboardDeviceStats fetchTmsDashboardStats();
    public ServerResponse generateLicense(TmsLicense tmsLicense);

}
