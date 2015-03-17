package org.tangerine.protocol;

public class Packet {

	private PacketHead packetHead;
	
	private Object body;

	public PacketHead getPacketHead() {
		return packetHead;
	}

	public void setPacketHead(PacketHead packetHead) {
		this.packetHead = packetHead;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
}
