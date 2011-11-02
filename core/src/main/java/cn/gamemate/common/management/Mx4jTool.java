package cn.gamemate.common.management;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mx4jTool {

	protected static final Logger logger = LoggerFactory
			.getLogger(Mx4jTool.class);

	public Mx4jTool() {
		tryLoading();
	}

	public boolean tryLoading() {
		logger.debug("trying to load mx4j now, if it's in the classpath");
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName processorName = new ObjectName(
					"Server:name=XSLTProcessor");

			Class<?> httpAdaptorClass = Class
					.forName("mx4j.tools.adaptor.http.HttpAdaptor");
			Object httpAdaptor = httpAdaptorClass.newInstance();
			String address = getAddress();
			httpAdaptorClass.getMethod("setHost", String.class).invoke(
					httpAdaptor, address);
			int port = getPort();
			httpAdaptorClass.getMethod("setPort", Integer.TYPE).invoke(
					httpAdaptor, port);

			ObjectName httpName = new ObjectName("system:name=http");
			mbs.registerMBean(httpAdaptor, httpName);

			Class<?> xsltProcessorClass = Class
					.forName("mx4j.tools.adaptor.http.XSLTProcessor");
			Object xsltProcessor = xsltProcessorClass.newInstance();
			httpAdaptorClass.getMethod("setProcessor",
					Class.forName("mx4j.tools.adaptor.http.ProcessorMBean"))
					.invoke(httpAdaptor, xsltProcessor);
			mbs.registerMBean(xsltProcessor, processorName);
			httpAdaptorClass.getMethod("start").invoke(httpAdaptor);
			logger.info("mx4j successfuly loaded (" + address + ":" + port + ")");
			return true;

		} catch (ClassNotFoundException e) {
			logger.info("Will not load MX4J, mx4j-tools.jar is not in the classpath");
		} catch (Exception e) {
			logger.warn("Could not start register mbean in JMX", e);
		}
		return false;
	}

	private String getAddress() {
		String defaultAddress = "0.0.0.0";
		String address = System.getProperty("mx4jaddress", "0.0.0.0");
		if (address == null || address.equals("")){
			return defaultAddress;
		}
		return  address;
	}
	private int getPort() {
		int port = 8111;
		String sPort = System.getProperty("mx4jport");
		if (sPort != null && !sPort.equals("")){
			port = Integer.parseInt(sPort);
		}
		return port;
	}

}
