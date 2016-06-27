/**
 * 
 */
package com.ibm.zdu;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.ibm.zdu.csv.model.CsvUser;
import com.ibm.zdu.zendesk.model.MappedUser;
import com.ibm.zdu.zendesk.model.ZendeskUser;

/**
 * @author sanketsw
 *
 */
public class Utils {

	static final Logger logger = LogManager.getLogger();
	
	public static <T> T getObjectFromFile(String file, Class<T> class1) {
		T pojo = null;
		try {
			String configFileContents = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
			pojo = new Gson().fromJson(configFileContents, class1);
		} catch (IOException e) {
			logger.warn(e);
		}
		return pojo;
	}

	public static <T> T getObjectFromResource(String fileResource, Class<T> class1) {
		T pojo = null;
		try {
			InputStream is = Utils.class.getResourceAsStream(fileResource);
			String configFileContents = IOUtils.toString(is);		
			pojo = new Gson().fromJson(configFileContents, class1);
		} catch (IOException e) {
			logger.error(e);
		}
		return pojo;
	}
	
	public static String toJson(Object obj) {
		try {
			return new Gson().toJson(obj);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	public static <T> T fromJson(String json, Class<T> class1) {
		try {
			return new Gson().fromJson(json, class1);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	public static void displayProgress(int currentRecords, int totalRecords) {
		try {
			String anim= "|/-\\";
			int scale = 2;
			int percent = (int)((currentRecords * 100.0f) / totalRecords);
			String data = "\r|";
			for(int i=0; i<percent/scale; i++) 
				data += "=";
			for(int i=percent/scale+1; i<100/scale; i++) 
				data += " ";
			data += "| " + percent + "% ("+currentRecords +"/"+ totalRecords+" items)" ;
			System.out.write(data.getBytes());
		} catch (Exception e) {
			logger.warn(e);
		}
	}

	public static List<MappedUser> getMappedUsers(List<? extends ZendeskUser> users) {
		List<MappedUser> mappedUsers = new ArrayList<>();
		for(ZendeskUser user : users) {
			mappedUsers.add(user.getMappedUser());
		}
		return mappedUsers;
	}

	public static ZduConfig getConfiguration(String configFile) {
		ZduConfig config = null;
		if (configFile != null) {
			logger.info("Loading user-defined configuration from {}...", configFile);
			config = Utils.getObjectFromFile(configFile, ZduConfig.class);
		}
		if (config == null) {
			logger.info("Could not find a valid user-defined configuration. Loading default config...");
			config = Utils.getObjectFromResource("zduConfig.json", ZduConfig.class);
		} 
		return config;
	}
}
