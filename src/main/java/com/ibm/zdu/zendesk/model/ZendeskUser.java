package com.ibm.zdu.zendesk.model;

import java.io.Serializable;

import com.ibm.zdu.csv.model.CsvUser;

public class ZendeskUser implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7980574651303001142L;

	MappedUser mappedUser;
	
	public CsvUser userInCsvFormat;
	
	
		

	/**
	 * @return the userInCsvFormat
	 */
	public CsvUser getUserInCsvFormat() {
		return userInCsvFormat;
	}

	/**
	 * @param userInCsvFormat the userInCsvFormat to set
	 */
	public void setUserInCsvFormat(CsvUser userInCsvFormat) {
		this.userInCsvFormat = userInCsvFormat;
	}

	/**
	 * @return the mappedUser
	 */
	public MappedUser getMappedUser() {
		return mappedUser;
	}

	/**
	 * @param mappedUser the mappedUser to set
	 */
	public void setMappedUser(MappedUser mappedUser) {
		this.mappedUser = mappedUser;
	}

	
}
