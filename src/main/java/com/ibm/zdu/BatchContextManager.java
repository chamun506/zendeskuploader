/**
 * 
 */
package com.ibm.zdu;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sanketsw
 *
 */
public class BatchContextManager {
	
	Map<String, JobStatusManager> statusMap = new HashMap<>();
	
	private static BatchContextManager _inst = null;
	
	private BatchContextManager() {
	}
	
	public static BatchContextManager getInstance() {
		if(_inst == null) {
			_inst = new BatchContextManager();
		}
		return _inst;
	}

	/**
	 * @return the statusMap
	 */
	public Map<String, JobStatusManager> getStatusMap() {
		return statusMap;
	}

	public JobStatusManager initializeStatusManager(String name, ZduConfig config) {
		JobStatusManager statusManager =  new JobStatusManager();
		statusManager.setConfig(config);
		return getStatusMap().put(name, statusManager);	
	}	
	

}
