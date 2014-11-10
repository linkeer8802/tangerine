package org.tangerine.protocol;

public class Packet {

	private Byte type;
	
	private Short length;
	
	private Message message;

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Short getLength() {
		return length;
	}

	public void setLength(Short length) {
		this.length = length;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	public static int getHeadLength() {
		return 3;
	}
}
