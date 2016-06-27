package com.ibm.zdu;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@RestController
@EnableSwagger2
public class ZduApplication {

	static final Logger logger = LogManager.getLogger();
	private static ConfigurableApplicationContext ctx;
	
	

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("springboot").apiInfo(apiInfo()).select()
				.paths(regex("/.*")).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("ZDUploader")
				.description("Data Uploader for Zendesk")
				.termsOfServiceUrl("http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open")
				.contact("sanketsw@au1.ibm.com").license("Apache License Version 2.0")
				.licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE").version("2.0")
				.build();
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "uploadToZendesk", nickname = "uploadToZendesk")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public String uploadToZendesk(@RequestParam(value = "configFile") String configFile) throws Exception {
		
		logger.info("Executing GET /upload?config={}", configFile);
		ZduConfig config = Utils.getConfiguration(configFile);

		//TODO read all CSV files that are placed in the path
		String csvPath = config.getInputCsvPath();
		String fileName= csvPath.concat("\\people_anz_update.csv");
		
		//Execute the batch process
		executeBatchProcess(config);
		
		/*
		JobParameters jobParameters = new JobParametersBuilder().addDate("date", new Date()).toJobParameters(); 
		JobStatusManager statusManager = BatchContextManager.getInstance().initializeStatusManager(job.getName(), config); 
		jobLauncher.run(job, jobParameters);*/
		
		// TODO Return appropriate report object
		String result = config.getInputCsvPath();
		logger.info(result);
		return result;
	}

	public static void main(String[] args) throws Exception {
		ctx = SpringApplication.run(ZduApplication.class, args);
		ZduApplication application = ctx.getBean(ZduApplication.class);
		application.uploadToZendesk(args.length > 0 ? args[0] : null);
			   
		
		//ctx.getBean(BatchConfiguration.class);
        
       /* for (int x =1 ; x <= 10000 ; x++){
            Utils.displayProgress(x, 10000);
            try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }*/
	}
	
	private static void executeBatchProcess(ZduConfig config) {
		// TODO read all CSV files that are placed in the path
		try {
			// Execute the batch process
			Job createOrUpdateUserJob = ctx.getBean("createOrUpdateUserJob", Job.class);
			System.out.println("job.getName()***"+createOrUpdateUserJob.getName());
			JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
			JobStatusManager statusManager = BatchContextManager.getInstance().initializeStatusManager(createOrUpdateUserJob.getName(), config); 
			System.out.println("in application statusManager***"+statusManager);

			JobParameters jobParameters = new JobParametersBuilder().addDate("date", new Date()).toJobParameters();
			JobExecution jobExecution = jobLauncher.run(createOrUpdateUserJob, jobParameters);
		} catch (Exception e) {
		}
	}

}
