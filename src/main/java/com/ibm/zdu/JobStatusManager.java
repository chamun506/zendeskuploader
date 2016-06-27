/**
 * 
 */
package com.ibm.zdu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.zdu.zendesk.model.PendingJob;
import com.ibm.zdu.zendesk.model.ZendeskUserResult;

/**
 * @author sanketsw
 *
 */
public class JobStatusManager {

	Map<String, PendingJob> pendingJobs = new HashMap<>();

	List<ZendeskUserResult> failedUserResults = new ArrayList<>();
	
	int currentCompletedRecords = 0;
	
	int totalRecords;
	
	ZduConfig config;

	/**
	 * @return the pendingJobs
	 */
	public Map<String, PendingJob> getPendingJobs() {
		return pendingJobs;
	}

	/**
	 * @return the failedUserResults
	 */
	public List<ZendeskUserResult> getFailedUserResults() {
		return failedUserResults;
	}

	/**
	 * @return the currentCompletedRecords
	 */
	public int getCurrentCompletedRecords() {
		return currentCompletedRecords;
	}

	/**
	 * @param currentCompletedRecords the currentCompletedRecords to set
	 */
	public void setCurrentCompletedRecords(int currentCompletedRecords) {
		this.currentCompletedRecords = currentCompletedRecords;
	}

	/**
	 * @return the totalRecords
	 */
	public int getTotalRecords() {
		return totalRecords;
	}

	/**
	 * @param totalRecords the totalRecords to set
	 */
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	/**
	 * @return the config
	 */
	public ZduConfig getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(ZduConfig config) {
		this.config = config;
	}

	public void incrementProgress(int size) {
		currentCompletedRecords += size;
		Utils.displayProgress(currentCompletedRecords, totalRecords);
	}

	
	
}
