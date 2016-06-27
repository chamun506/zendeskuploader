package com.ibm.zdu.batch;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.ibm.zdu.Utils;
import com.ibm.zdu.csv.model.CsvUser;
import com.ibm.zdu.zendesk.model.MappedUser;
import com.ibm.zdu.zendesk.model.ZendeskUser;

public class UserProcessor implements ItemProcessor<CsvUser, ZendeskUser> {

    private static final Logger log = LoggerFactory.getLogger(UserProcessor.class);

	@Override	
	public ZendeskUser process(CsvUser user) throws Exception {
		ZendeskUser zdUser = new ZendeskUser();
		
		zdUser.setMappedUser(getMappedUser(user));
		zdUser.setUserInCsvFormat(user);
		return zdUser;
	}

	public static MappedUser getMappedUser(CsvUser user) {
		MappedUser mappedUser = new MappedUser();
		mappedUser.setName(user.getFirstName() + " " + user.getLastName());
		mappedUser.setEmail(user.getEmail());
		return mappedUser;
	}
}
