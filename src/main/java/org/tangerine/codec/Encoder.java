package org.tangerine.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.tangerine.Constant.PacketType;
import org.tangerine.protocol.Message;
import org.tangerine.protocol.Packet;
import org.tangerine.protocol.PacketHead;

public class Encoder extends MessageToByteEncoder<Packet>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
		
		ByteBuf body = null;
		
		if (packet.getPacketHead().getType().equals(PacketType.PCK_DATA) &&
				packet.getBody() instanceof Message) {
			
			body = Message.encode((Message) packet.getBody());
			
		} else {
			if (packet.getBody() != null) {
				body = (ByteBuf) packet.getBody();
			}
		}
		
		/**
		 * 数据包
		 */
		if (body != null) {
			packet.getPacketHead().setLength((short) body.readableBytes());
			out.writeBytes(PacketHead.encode(packet.getPacketHead()));
			out.writeBytes(body);
		} else {
			packet.getPacketHead().setLength((short) 0);
			out.writeBytes(PacketHead.encode(packet.getPacketHead()));
		}
	}
}
