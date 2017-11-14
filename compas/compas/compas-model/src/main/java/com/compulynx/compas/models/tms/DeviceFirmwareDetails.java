package com.compulynx.compas.models.tms;

/**
 * Created by Eli on 1/9/2016.
 */
public class DeviceFirmwareDetails {

    private int deviceId;

    private String deviceSerial;

    private String firmwareVersion;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public DeviceFirmwareDetails(){
        super();
    }


}

