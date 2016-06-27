package com.ibm.zdu;

public class ZduConfig {

	private String inputCsvPath;
	private String reportPath;
	private int recordsPerAPI;
	private int APIPerMin;
	private String zendeskHost;
	private String credentials;
	private String createUpdateManyURI;
	private String jobStatusesURI;
	/**
	 * @return the inputCsvPath
	 */
	public String getInputCsvPath() {
		return inputCsvPath;
	}
	/**
	 * @param inputCsvPath the inputCsvPath to set
	 */
	public void setInputCsvPath(String inputCsvPath) {
		this.inputCsvPath = inputCsvPath;
	}
	/**
	 * @return the reportPath
	 */
	public String getReportPath() {
		return reportPath;
	}
	/**
	 * @param reportPath the reportPath to set
	 */
	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	/**
	 * @return the recordsPerAPI
	 */
	public int getRecordsPerAPI() {
		return recordsPerAPI;
	}
	/**
	 * @param recordsPerAPI the recordsPerAPI to set
	 */
	public void setRecordsPerAPI(int recordsPerAPI) {
		this.recordsPerAPI = recordsPerAPI;
	}
	/**
	 * @return the aPIPerMin
	 */
	public int getAPIPerMin() {
		return APIPerMin;
	}
	/**
	 * @param aPIPerMin the aPIPerMin to set
	 */
	public void setAPIPerMin(int aPIPerMin) {
		APIPerMin = aPIPerMin;
	}
	/**
	 * @return the zendeskHost
	 */
	public String getZendeskHost() {
		return zendeskHost;
	}
	/**
	 * @param zendeskHost the zendeskHost to set
	 */
	public void setZendeskHost(String zendeskHost) {
		this.zendeskHost = zendeskHost;
	}
	/**
	 * @return the credentials
	 */
	public String getCredentials() {
		return credentials;
	}
	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}
	/**
	 * @return the createUpdateManyURI
	 */
	public String getCreateUpdateManyURI() {
		return createUpdateManyURI;
	}
	/**
	 * @param createUpdateManyURI the createUpdateManyURI to set
	 */
	public void setCreateUpdateManyURI(String createUpdateManyURI) {
		this.createUpdateManyURI = createUpdateManyURI;
	}
	/**
	 * @return the jobStatusesURI
	 */
	public String getJobStatusesURI() {
		return jobStatusesURI;
	}
	/**
	 * @param jobStatusesURI the jobStatusesURI to set
	 */
	public void setJobStatusesURI(String jobStatusesURI) {
		this.jobStatusesURI = jobStatusesURI;
	}

	public int getChunkSize() {
		return getAPIPerMin() * getRecordsPerAPI();
	}

}
