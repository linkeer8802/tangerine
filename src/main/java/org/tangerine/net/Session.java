package org.tangerine.net;

import java.util.HashMap;
import java.util.Map;

public class Session {
	
	private Integer sessionId;
	
	private Long userId;
	
	private String uname;
	
	private Long lastActive;
	 
	private Connection connection;
	
	 private final Map<String, Object> sessionData = new HashMap<String, Object>();

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public Long getLastActive() {
		return lastActive;
	}

	public void setLastActive(Long lastActive) {
		this.lastActive = lastActive;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Map<String, Object> getSessionData() {
		return sessionData;
	}
}
