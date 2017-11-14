package com.compulynx.compas.models.tms;

/**
 * Created by Eli on 1/12/2016.
 */
public class TmsLicense {

    private String licenseDescription;

    private String licenseNumber;

    public String getLicenseDescription() {
        return licenseDescription;
    }

    public void setLicenseDescription(String licenseDescription) {
        this.licenseDescription = licenseDescription;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public TmsLicense() {
        super();
    }


}
