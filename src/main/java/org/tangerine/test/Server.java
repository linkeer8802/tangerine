package org.tangerine.test;

import org.tangerine.connector.NettyConnector;
import org.tangerine.net.Router;

public class Server {

	public static void main(String[] args) throws Exception {
		
//		AppContext.getInstance().getConfig().setUseProtobuf(true);
		
		Router.registerRouterHandler(new HelloHandler());
		
		new NettyConnector("192.168.1.134", 9770).start();
	}
}
