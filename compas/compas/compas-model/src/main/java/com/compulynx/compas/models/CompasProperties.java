package com.compulynx.compas.models;

/**
 * Created by Kibet on 5/21/2016.
 */
public class CompasProperties {

	 private String afis_ip;
	    private String afis_port;
	    public String bnfUploadFilePath;
		public String reportFilePath;
	
	    /**
	     *
	     */
	    public CompasProperties() {
	        super();
	    }

	    public String getBnfUploadFilePath() {
			return bnfUploadFilePath;
		}

		public void setBnfUploadFilePath(String bnfUploadFilePath) {
			this.bnfUploadFilePath = bnfUploadFilePath;
		}

		public CompasProperties(String afis_ip, String afis_port) {
	        this.afis_ip = afis_ip;
	        this.afis_port = afis_port;
	    }

	    public CompasProperties(String settleMentFilePath) {
			super();
			this.bnfUploadFilePath = settleMentFilePath;
		}

		public String getReportFilePath() {
			return reportFilePath;
		}

		public void setReportFilePath(String reportFilePath) {
			this.reportFilePath = reportFilePath;
		}

		public String getAfis_port() {
	        return afis_port;
	    }

	    public void setAfis_port(String afis_port) {
	        this.afis_port = afis_port;
	    }

	    public String getAfis_ip() {
	        return afis_ip;
	    }

	    public void setAfis_ip(String afis_ip) {
	        this.afis_ip = afis_ip;
	    }
}
