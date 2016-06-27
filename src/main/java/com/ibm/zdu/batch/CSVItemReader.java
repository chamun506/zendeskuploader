package com.ibm.zdu.batch;


import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.ibm.zdu.ZduConfig;
import com.ibm.zdu.csv.model.CsvUser;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;


public class CSVItemReader implements ItemReader<CsvUser>{
	
	private List<CsvUser> userList;
	private int nextIndex;
	ZduConfig config;
	String csvFileName;
	
	public CSVItemReader(ZduConfig config, String csvFileName) {
	   this.config = config;
	   this.csvFileName = csvFileName;
	   parseCsvtoBean();
	}

	private void parseCsvtoBean()
	{
		try {
			CSVReader reader = new CSVReader(new FileReader(csvFileName));
			HeaderColumnNameTranslateMappingStrategy<CsvUser> strategy = new HeaderColumnNameTranslateMappingStrategy<CsvUser>();
			Map<String, String> columnMapping = new HashMap<String, String>();
			columnMapping.put("last name", "lastName");
			columnMapping.put("first name", "firstName");
			columnMapping.put("internet email address", "email");
			// TODO set row number in User object		
			
			// TODO set the totalRecords in BatchStatusManager
			strategy.setType(CsvUser.class);
			strategy.setColumnMapping(columnMapping);
			CsvToBean<CsvUser> ctb = new CsvToBean<CsvUser>();
			userList = ctb.parse(strategy,reader);
		
			//TODO move the file to output folder after process is done
	
			nextIndex=0;
		} catch (Exception e) {			
			//TODo use loggers
			e.printStackTrace();
		}
	}

	@Override
	public CsvUser read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		CsvUser user = null;
		if (nextIndex < userList.size()) {
			user = userList.get(nextIndex);
			nextIndex++;
		}
		return user;
	}

}
