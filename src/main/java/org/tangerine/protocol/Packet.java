package org.tangerine.protocol;

import org.tangerine.Constant.PacketType;
import org.tangerine.util.JsonUtil;

public class Packet {

	private Byte type;
	
	private Short length;
	
	private byte[] body;
	
	public Packet() {}
	
	public Packet(Byte type, Object body) {
		
		byte[] bytes = JsonUtil.toJson(body).getBytes();
		
		this.type = type;
		this.body = bytes;
		this.length = (short) bytes.length;
	}
	
	public static Packet buildHeartbeatPacket() {
		
		Packet packet = new Packet();
		packet.setBody(null);
		packet.setLength((short) 0);
		packet.setType(PacketType.PCK_HEARTBEAT);
		
		return packet;
	}

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
	/**
	 * type + length
	 * @return
	 */
	public static int getHeadLength() {
		return 3;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}
}
