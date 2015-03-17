package org.tangerine.protocol;

import io.netty.buffer.ByteBuf;


public class PacketHead {

	private Byte type;
	
	private Short length;
	
	public PacketHead() {}

	public PacketHead(Byte type, Short length) {
		this.type = type;
		this.length = length;
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
	
	public static PacketHead decode(ByteBuf buf) {
		
		PacketHead packetHead = new PacketHead();
		packetHead.setType(buf.readByte());
		packetHead.setLength(buf.readShort());
		
		return packetHead;
	}
	
	public static void encode(PacketHead packetHead, ByteBuf out) {
		
		out.writeByte(packetHead.getType());
		out.writeShort(packetHead.getLength());
	}
}
