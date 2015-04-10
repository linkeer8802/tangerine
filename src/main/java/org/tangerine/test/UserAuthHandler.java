package org.tangerine.test;

import org.springframework.stereotype.Component;
import org.tangerine.annotation.RuoteMapping;
import org.tangerine.net.Connection;
import org.tangerine.net.Session;
import org.tangerine.net.SessionManager;

@Component
public class UserAuthHandler {

	@RuoteMapping(value="login")
	public boolean login(Connection connection, AuthMessage msg) {
		if (msg.getUname() != null
				&& msg.getPassword().equals("111111")) {
			
			Session session = new Session();
			session.setLastActive(System.currentTimeMillis());
			session.setConnection(connection);
			session.setUname(msg.getUname());
			
			SessionManager.getInstance().createNewSession(session);
			
			return true;
		}
		return false;
	}
}
