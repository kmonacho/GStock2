package com.gs.bean;



public class Login {
	private String username, passsword;
	private boolean admin;
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the passsword
	 */
	public String getPasssword() {
		return passsword;
	}
	/**
	 * @param passsword the passsword to set
	 */
	public void setPasssword(String passsword) {
		this.passsword = passsword;
	}
	/**
	 * @return the admin
	 */
	public boolean isAdmin() {
		return admin;
	}
	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	
}
