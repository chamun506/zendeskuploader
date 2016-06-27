package com.ibm.zdu.zendesk.model;

import java.io.Serializable;

import com.ibm.zdu.csv.model.CsvUser;

public class MappedUser implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7980574651303001142L;

	private String name;
	private String email;
	

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
