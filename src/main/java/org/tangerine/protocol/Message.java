package org.tangerine.protocol;

public class Message {

	private Byte messageType;
	
	private Boolean routeFlag;
	
	private Integer messageId;
	
	private String routePath;
	
	private byte[] body;

	public Byte getMessageType() {
		return messageType;
	}

	public void setMessageType(Byte messageType) {
		this.messageType = messageType;
	}

	public Boolean getRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(Boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getRoutePath() {
		return routePath;
	}

	public void setRoutePath(String routePath) {
		this.routePath = routePath;
	}
	
	public byte[] getBody() {
		return body;
	}
	
	public void setBody(byte[] body) {
		this.body = body;
	}
	
	/**
	 * flag 的第5~7位
	 * @param flag
	 * @return
	 */
	public Byte getMessageType(byte flag) {
		byte value = 0;
		value = (byte) (flag & 0x0e);
		value = (byte) (value >> 1);
		return value;
	}
	/**
	 * flag 最后一位
	 * @param flag
	 * @return
	 */
	public Boolean getRouteFlag(byte flag) {
		return (flag & 0x01) != 0;
	}
	
	public byte getFlag() {
		byte flag = 0;
		
		flag = (byte) (this.messageType << 1);
		flag = (byte) (flag & 0xfe);
		
		if (this.routeFlag) {
			flag = (byte) (flag | 0x01);
		}
		
		return flag;
	}
}
