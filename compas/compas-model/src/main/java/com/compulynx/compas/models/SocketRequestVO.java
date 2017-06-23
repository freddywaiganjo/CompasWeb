package com.compulynx.compas.models;

/**
 * Created by Kibet on 5/19/2016.
 */
public class SocketRequestVO {

    private String afisIp;
    private String afisPort;

    private String requestType;
    private Object request;

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

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public SocketRequestVO() {
        super();
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Object getObject() {
        return request;
    }

    public void setObject(Object request) {
        this.request = request;
    }
}
