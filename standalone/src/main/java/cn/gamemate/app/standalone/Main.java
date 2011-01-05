package cn.gamemate.app.standalone;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.xml.XmlConfiguration;



public class Main {
	static String configDir = "";
	static String logDir = "";
	static String appDir = "";
	static String jettyDir= "";

	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Server server = null;
		appDir = System.getProperty("gm.app.home", "/usr/gamemate/apps");
		jettyDir = new File(appDir, "config").getAbsolutePath();
		configDir = System.getProperty("gm.config.dir","/var/log/gamemate");
		logDir = System.getProperty("gm.log.dir", "/etc/gamemate/");
		
		
		try {
			server = new Server();
			
			File jettyConfigFile = new File(jettyDir, "jetty.xml");
			URL configUrl = getConfigUrl(jettyConfigFile);
			XmlConfiguration xmlConfiguration = new XmlConfiguration(configUrl);
			xmlConfiguration.configure(server);
			
			
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO: LOGGING
			System.err.println("Could not start the Jetty server: " + e);
			e.printStackTrace();
			if (server != null) {
				try {
					server.stop();
				} catch (Exception e1) {
					System.err
							.println("Unable to stop the jetty server: " + e1);
				}
			}
		}
	}
	

	private static URL getConfigUrl(File jettyConfigFile) throws MalformedURLException{
		URL configUrl;
		if (jettyConfigFile.exists() && jettyConfigFile.isFile()){
			System.out.println("Starting jetty from configuration file " + jettyConfigFile.getAbsolutePath());
			configUrl = new URL("file:" + jettyConfigFile.toURI().getPath());
			return configUrl;
		}else{
			System.err.println("No jetty configuration file found at " + jettyConfigFile.getAbsolutePath());
			System.exit(1);
			return null;
		}
	}

}
