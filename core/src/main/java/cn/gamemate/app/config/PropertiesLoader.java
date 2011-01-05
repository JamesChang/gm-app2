package cn.gamemate.app.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PropertiesLoader {
	
	private Logger logger = LoggerFactory.getLogger(PropertiesLoader.class); 
	
	public PropertiesLoader() throws FileNotFoundException, IOException {
		loadConfigDir();
		
	}
	

	public void loadConfigDir() throws FileNotFoundException, IOException{
		File gmConfigDirFile;
		String gmConfigDir = System.getProperty("gm.app.config.dir",null);
		if (gmConfigDir == null){
			String gmHomeDir = System.getProperty("gm.app.home",null);
			if (gmHomeDir == null){
				logger.info("Skip Loading Properties cause none of \"gm.app.home\" and \"gm.app.config.dir\" is set.");
				throw new RuntimeException("Configuration files not set.");
				
			}
			gmConfigDirFile = new File(gmHomeDir,"config");
		}
		else{
			gmConfigDirFile = new File(gmConfigDir);
		}
		logger.info("Loading properties from \"{}\"", gmConfigDirFile.getAbsolutePath());
		
		File[] listFiles = gmConfigDirFile.listFiles();
		ArrayList<File> propertyFiles = new ArrayList<File>();
		for (int i =0; i<listFiles.length;++i){
			if (listFiles[i].getName().endsWith(".properties")){
				Properties t = new Properties();
				FileInputStream fileInputStream = new FileInputStream(listFiles[i]);
				t.load(fileInputStream);
				fileInputStream.close();
				for(String k:t.stringPropertyNames()){
					System.setProperty(k, t.getProperty(k));
				}
				
			}
		}
	}

}
