package com.compulynx.compas.models.tms;

/**
 * Created by Elly on 8/27/2015.
 */
public class ServerResponse {

    private int responseCode;

    private String responseMessage;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public ServerResponse(int responseCode, String responseMessage) {
        super();
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public ServerResponse() {
        super();
    }

}
