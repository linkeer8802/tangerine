package org.tangerine.net;

public class RouterHandler {

	private Object handler;

	public RouterHandler() {}
	
	public RouterHandler(Object handler) {
		super();
		this.handler = handler;
	}

	public Object getHandler() {
		return handler;
	}

	public void setHandler(Object handler) {
		this.handler = handler;
	}
}
