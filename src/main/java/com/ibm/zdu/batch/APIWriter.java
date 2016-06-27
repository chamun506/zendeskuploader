package com.ibm.zdu.batch;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import com.ibm.zdu.BatchContextManager;
import com.ibm.zdu.JobStatusManager;
import com.ibm.zdu.Utils;
import com.ibm.zdu.ZduConfig;
import com.ibm.zdu.zendesk.model.PendingJob;
import com.ibm.zdu.zendesk.model.Result;
import com.ibm.zdu.zendesk.model.ZendeskBatchJob;
import com.ibm.zdu.zendesk.model.ZendeskUser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

public class APIWriter implements ItemWriter<ZendeskUser>, InitializingBean {

	static final Logger logger = LogManager.getLogger();

	JobStatusManager statusManager;

	/**
	 * @return the statusManager
	 */
	public JobStatusManager getStatusManager() {
		return statusManager;
	}

	/**
	 * @param statusManager
	 *            the statusManager to set
	 */
	public void setStatusManager(JobStatusManager statusManager) {
		this.statusManager = statusManager;
	}
	
	@BeforeStep
	public void getBatchContextManager(StepExecution stepExecution) {
		statusManager = BatchContextManager.getInstance().getStatusMap().get(stepExecution.getJobExecution().getJobConfigurationName());
		System.out.println("APIWriter***"+stepExecution.getJobExecution().getJobConfigurationName());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	

	@Override
	public void write(List<? extends ZendeskUser> users) throws Exception {
		try {
			logger.info("writing " + users.size() + " users from count "
					+ (users.isEmpty() ? "unknown" : users.get(0).getUserInCsvFormat().getRow()));
			List<ZendeskUser> usersChunk = new ArrayList<>();
			for (ZendeskUser user : users) {
				if (usersChunk.size() == statusManager.getConfig().getRecordsPerAPI()) {
					executePostRestService(usersChunk);
					usersChunk = new ArrayList<>();
				}
				usersChunk.add(user);
			}
			if (!usersChunk.isEmpty())
				executePostRestService(usersChunk);

			boolean finished = false;
			while (!finished) {
				for (PendingJob pendingJob : statusManager.getPendingJobs().values()) {
					executeGetRestService(pendingJob.getJob().getJob_status().getId());
				}
				finished = statusManager.getPendingJobs().isEmpty() ? true : false;
				Thread.sleep(1000);
			}
			// Thread.sleep(50000);
		} catch (Exception e) {
			logger.throwing(e);
		}
	}

	public void executePostRestService(List<? extends ZendeskUser> users) {
		String input = "{\"users\": " + Utils.toJson(Utils.getMappedUsers(users)) + "}";
		logger.info("input is ***" + input);
		Client client = Client.create();
		ZduConfig config = statusManager.getConfig();
		String auth = new String(Base64.encode(config.getCredentials()));
		WebResource webResource = client.resource(config.getZendeskHost());
		ClientResponse response = webResource.path(config.getCreateUpdateManyURI())
				.header("Authorization", "Basic " + auth).type(MediaType.APPLICATION_JSON_TYPE)
				.accept("application/json").post(ClientResponse.class, input);
		logger.info("HTTP Response on POST " + response.getStatus());
		if (response.getStatus() == 200) {
			String responseString = response.getEntity(String.class).toString();
			logger.info(responseString);
			ZendeskBatchJob job = Utils.fromJson(responseString, ZendeskBatchJob.class);
			if (job != null && job.getJob_status().getId() != null) {
				PendingJob pendingJob = new PendingJob();
				pendingJob.setJob(job);
				pendingJob.setUsers(users);
				statusManager.getPendingJobs().put(job.getJob_status().getId(), pendingJob);
			}
		} else {
			// TODO Write failure results. Status not 200 means all users in
			// this chunk failed.
		}

	}

	public void executeGetRestService(String jobId) {

		PendingJob pendingJob = statusManager.getPendingJobs().get(jobId);
		Client client = Client.create();
		ZduConfig config = statusManager.getConfig();
		String auth = new String(Base64.encode(config.getCredentials()));
		WebResource webResource = client.resource(config.getZendeskHost());
		ClientResponse response = webResource.path(config.getJobStatusesURI()).path(jobId + ".json")
				.header("Authorization", "Basic " + auth).type(MediaType.APPLICATION_JSON_TYPE)
				.accept("application/json").get(ClientResponse.class);
		logger.info("HTTP Response on GET Status of the pending job {} = {}", jobId, response.getStatus());
		if (response.getStatus() == 200) {
			String responseString = response.getEntity(String.class).toString();
			logger.info(responseString);
			ZendeskBatchJob job = Utils.fromJson(responseString, ZendeskBatchJob.class);
			List<Result> results = job.getJob_status().getResults();
			if (results == null) {
				// Job is still in progress
				return;
			} else {
				statusManager.incrementProgress(pendingJob.getUsers().size());
				statusManager.getPendingJobs().remove(jobId);
			}
			for (Result result : results) {
				if (result.getErrors() != null) {
					ZendeskUser failedUser = pendingJob.getUsers().get(results.indexOf(result));
					logger.info("Error creating user {} {} : {} {}", failedUser.getMappedUser().getName(),
							failedUser.getMappedUser().getEmail(), result.getStatus(), result.getErrors());
					// TODO Process the error for this user and write to report
				}
			}
		} else {
			// TODO Write failure results. Status not 200 means all users in
			// this chunk failed.
		}

	}

}
