package org.tangerine;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tangerine.components.AppContext;
import org.tangerine.connector.Connector;
import org.tangerine.connector.NettyConnector;
import org.tangerine.net.Router;
import org.tangerine.test.HelloHandler;
import org.tangerine.test.UserAuthHandler;

public class Tangerine {

	Logger log = Logger.getLogger(Tangerine.class);
	
	private static Tangerine instance;
	private ApplicationContext context;
	
	private Connector connector;
	
	private Tangerine() {
		context = new ClassPathXmlApplicationContext("conf/applicationContext.xml"); 
	}
	
	public synchronized static Tangerine getInstance() {
		if (instance == null) {
			instance = new Tangerine();
		}
		return instance;
	}
	
	public void start() throws Exception {
		
		Router.registerRouterHandler(new HelloHandler());
		Router.registerRouterHandler(new UserAuthHandler());
		
		
		AppContext appContext = getContext().getBean(AppContext.class);
		
		appContext.getConfig().setHeartbeat(15);
		
		String[] conStrs = appContext.getConnectionString().split(":");
		String host = conStrs[0];
		int port = Integer.valueOf(conStrs[1]);
		
		log.info("bind host:" + host + ", port:" + port);
		
		connector = new NettyConnector(host, port);
		connector.start();
	}

	public ApplicationContext getContext() {
		return context;
	}
	
	public <T> T getBean(Class<T> clz) {
		return getContext().getBean(clz);
	}
	
	public void stop() {
		connector.stop();
	}
}
