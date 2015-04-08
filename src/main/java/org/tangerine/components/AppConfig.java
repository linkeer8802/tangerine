package org.tangerine.components;

public class AppConfig {

	private Boolean routeFlag = false;
	
	private Integer heartbeat = 3;
	
	private Boolean useProtobuf = false;
	
	public Boolean getRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(Boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public Integer getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(Integer heartbeat) {
		this.heartbeat = heartbeat;
	}

	public Boolean getUseProtobuf() {
		return useProtobuf;
	}

	public void setUseProtobuf(Boolean useProtobuf) {
		this.useProtobuf = useProtobuf;
	}
}
