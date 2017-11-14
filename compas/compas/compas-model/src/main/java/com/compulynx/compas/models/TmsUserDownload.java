/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author shree
 *
 */
public class TmsUserDownload {

	public int id;
	public String first_name;
	public String last_name;
	public String username;
	public String password;
	public int role;
	public TmsUserDownload(int id, String first_name, String last_name,
			String username, String password, int role) {
		super();
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	public TmsUserDownload() {
		super();
		// TODO Auto-generated constructor stub
	}
}
