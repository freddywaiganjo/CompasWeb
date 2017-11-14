package com.compulynx.compas.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kibet on 3/21/2016.
 */
public class BioSocketRequestVO implements Serializable {

    private String originIp;
    private String request;
    private String requestType;
    private String afisIp;
    private String afisPort;
    private List<BioImage> imageList;
    private List<BioImage> rawImageList;


    public String getAfisIp() {
        return afisIp;
    }

    public void setAfisIp(String afisIp) {
        this.afisIp = afisIp;
    }

    public String getAfisPort() {
        return afisPort;
    }

    public void setAfisPort(String afisPort) {
        this.afisPort = afisPort;
    }

    public BioSocketRequestVO() {
        super();
    }

    public List<BioImage> getRawImageList() {
        return rawImageList;
    }

    public void setRawImageList(List<BioImage> rawImageList) {
        this.rawImageList = rawImageList;
    }

    public List<BioImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<BioImage> imageList) {
        this.imageList = imageList;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getOriginIp() {
        return originIp;
    }

    public void setOriginIp(String originIp) {
        this.originIp = originIp;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

}
