/**
 * 
 */
package com.ibm.zdu.zendesk.model;

/**
 * @author sanketsw
 *
 */
public class ZendeskUserResult {
	
	ZendeskUser user;
	
	Result status;


	/**
	 * @return the user
	 */
	public ZendeskUser getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(ZendeskUser user) {
		this.user = user;
	}

	/**
	 * @return the status
	 */
	public Result getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Result status) {
		this.status = status;
	}

	
	

}
