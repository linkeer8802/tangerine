package org.tangerine.test;

import org.tangerine.annotation.RuoteMapping;
import org.tangerine.net.Connection;
import org.tangerine.protocol.Message;

public class HelloHandler {

	@RuoteMapping(value="sayHello")
	public void sayHello(Connection connection, String message) {
		System.out.println(message);
		connection.deliver(null, "onSayHello", "pull sayHello from server.");
	}
	
	@RuoteMapping(value="sayHello2")
	public String sayHello2(Connection connection, Message message, HelloMessage msg) {
		System.out.println(msg);
		return msg.getMsg();
	}
}
