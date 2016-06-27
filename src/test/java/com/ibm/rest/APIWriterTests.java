package com.ibm.rest;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ibm.zdu.JobStatusManager;
import com.ibm.zdu.Utils;
import com.ibm.zdu.ZduApplication;
import com.ibm.zdu.ZduConfig;
import com.ibm.zdu.batch.APIWriter;
import com.ibm.zdu.batch.UserProcessor;
import com.ibm.zdu.csv.model.CsvUser;
import com.ibm.zdu.zendesk.model.ZendeskUser;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZduApplication.class)
@WebAppConfiguration
public class APIWriterTests {
	ZduConfig config = Utils.getObjectFromResource("zduConfig.json", ZduConfig.class);


	@Test
	public void testWrite() {
		APIWriter writer = new APIWriter();
		JobStatusManager statusManager = new JobStatusManager();
		statusManager.setConfig(config);
		writer.setStatusManager(statusManager);
		
		
		List<ZendeskUser> users = new ArrayList<>();
		users.add(getDummyUser("sampleJunit@exmaple.org", "Sample", "JunitUser"));
		users.add(getDummyUser("sampleJunit02@exmaple.org", "Sample02", "JunitUser02"));
		
		Exception e1 = null;
		try {
			writer.write(users);
		} catch(Exception e) {
			e1 = e;
		}
		
		assertNull(e1);
		
		//TODO Check whether user is created using a get API
		
		// TODO remove users at the end of the test
		
	}

	private ZendeskUser getDummyUser(String email, String fname, String lname) {
		ZendeskUser user = new ZendeskUser();
		CsvUser csvUser = new CsvUser();
		csvUser.setEmail(email);
		csvUser.setFirstName(fname);
		csvUser.setLastName(lname);
		user.setMappedUser(UserProcessor.getMappedUser(csvUser));
		user.setUserInCsvFormat(csvUser);
		return user;
	}
		

}
