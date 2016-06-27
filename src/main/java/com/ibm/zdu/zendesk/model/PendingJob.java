/**
 * 
 */
package com.ibm.zdu.zendesk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sanketsw
 *
 */
public class PendingJob {
	
	List<? extends ZendeskUser> users = new ArrayList<>();
	
	ZendeskBatchJob job;

	/**
	 * @return the users
	 */
	public List<? extends ZendeskUser> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<? extends ZendeskUser> users) {
		this.users = users;
	}

	/**
	 * @return the job
	 */
	public ZendeskBatchJob getJob() {
		return job;
	}

	/**
	 * @param job the job to set
	 */
	public void setJob(ZendeskBatchJob job) {
		this.job = job;
	}
	
	

}
