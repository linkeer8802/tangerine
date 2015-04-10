package org.tangerine.net;

import java.io.Serializable;

public class OnlineUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2501339927362015263L;

	private Integer sessionId;
	
	private Long userId;
	
	private String uname;
	
	private Long lastActive;

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
}
