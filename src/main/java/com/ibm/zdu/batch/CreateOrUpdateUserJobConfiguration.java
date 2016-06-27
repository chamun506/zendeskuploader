package com.ibm.zdu.batch;



import javax.annotation.PostConstruct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import com.ibm.zdu.BatchContextManager;
import com.ibm.zdu.JobStatusManager;
import com.ibm.zdu.csv.model.CsvUser;
import com.ibm.zdu.zendesk.model.ZendeskUser;

// dont start the batch automatically.
// Start the batch from uploadToZendesk
@Configuration
@EnableBatchProcessing
public class CreateOrUpdateUserJobConfiguration {
	
	JobStatusManager statusManager;
	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    //TODO call this code on init with right override and method name
    @PostConstruct
    public void init() {
    	System.out.println("statusManager***"+statusManager);
    }

	@Bean
	public FlatFileItemReader<CsvUser> reader() {
		FlatFileItemReader<CsvUser> reader = null;
		try {
			reader = new FlatFileItemReader<CsvUser>();
			System.out.println("statusManager***"+statusManager);
			reader.setResource(new FileSystemResource(statusManager.getConfig().getInputCsvPath().concat("\\people_anz_update.csv")));
			reader.setLinesToSkip(1);
			DefaultLineMapper<CsvUser> lineMapper = new DefaultLineMapper<CsvUser>();
			DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
			tokenizer.setNames(new String[] { "first name", "last name", "internet email address" });
			lineMapper.setLineTokenizer(tokenizer);
			lineMapper.setFieldSetMapper(new CSVFieldSetMapper());
			reader.setLineMapper(lineMapper);
		} catch(Exception ex){
		}
		return reader;
	}

    @Bean
    public UserProcessor processor() {
        return new UserProcessor();
    }
    
    @Bean
    public ItemWriter<ZendeskUser> writer() {
    	return new APIWriter();
    }
    
    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener();
    }

   
    @Bean
    public Job createOrUpdateUserJob() {
        Job job = jobBuilderFactory.get("createOrUpdateUserJob").incrementer(new RunIdIncrementer()).listener(listener()).flow(step1()).end().build();
        System.out.println("job.getName()***"+job.getName());
    	statusManager = BatchContextManager.getInstance().getStatusMap().get(job.getName());
    	
    	return job;
    }

    @Bean
    public Step step1() {
    	// TODO Use statusManager.getConfig().getChunkSize() instead of 15
        return stepBuilderFactory.get("step1").<CsvUser, ZendeskUser> chunk(15).reader(reader()).processor(processor()).writer(writer()).build();
    }

}
	


