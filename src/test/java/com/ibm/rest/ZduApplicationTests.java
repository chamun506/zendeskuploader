package com.ibm.rest;

import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.ibm.zdu.Utils;
import com.ibm.zdu.ZduApplication;
import com.ibm.zdu.ZduConfig;
import com.ibm.zdu.zendesk.model.ZendeskUser;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZduApplication.class)
@WebAppConfiguration
public class ZduApplicationTests {
	ZduConfig config = Utils.getObjectFromResource("zduConfig.json", ZduConfig.class);

	@Test
	public void contextLoads() {
	}
	
	@Test 
		public void testSuccessfulReading() throws Exception {
		List<ZendeskUser> userList;
		String fileName="people_anz_update.csv";
		CSVReader reader = new CSVReader(new FileReader(config.getInputCsvPath().concat(fileName)));
		HeaderColumnNameTranslateMappingStrategy<ZendeskUser> strategy = new HeaderColumnNameTranslateMappingStrategy<ZendeskUser>();
		Map<String, String> columnMapping = new HashMap<String, String>();
		columnMapping.put("last name", "lastName");
		columnMapping.put("first name", "firstName");
		columnMapping.put("internet email address", "email");
		strategy.setType(ZendeskUser.class);
		strategy.setColumnMapping(columnMapping);
		CsvToBean<ZendeskUser> ctb = new CsvToBean<ZendeskUser>();
		userList = ctb.parse(strategy,reader);
			try {
				int count = 0;
				ZendeskUser user;
				if(!userList.isEmpty()){
				Iterator<ZendeskUser> itr = userList.iterator();
				while(itr.hasNext()){
					user = itr.next();
					count++;
				}
				assertEquals(15, count);
				}
			} catch (Exception e) {
				throw e;
			} finally {
				reader.close();
			}
		}
		

}
