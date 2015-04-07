package org.tangerine.test;

import org.tangerine.components.AppContext;
import org.tangerine.connector.NettyConnector;
import org.tangerine.net.Router;

public class Server {

	public static void main(String[] args) throws Exception {
		
//		AppContext.getInstance().getConfig().setUseProtobuf(true);
		
		Router.registerRouterHandler(new HelloHandler());
		
//		new NettyConnector("192.168.1.134", 9770).start();
//		String host = "192.168.1.134";
		AppContext.getInstance().getConfig().setHeartbeat(15);
		
//		new NettyConnector("192.168.1.134", 9770).start();
//		String host = "localhost";
		String host = "121.199.65.49";
		int port = 9770;
		System.out.println("bind host:" + host + ", port:" + port);
		new NettyConnector(host, port).start();
	}
}
