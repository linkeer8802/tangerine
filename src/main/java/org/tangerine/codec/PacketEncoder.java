package org.tangerine.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.tangerine.Constant.PacketType;
import org.tangerine.components.RouteDictionary;
import org.tangerine.protocol.Packet;

public class PacketEncoder extends MessageToByteEncoder<Packet>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
		/**
		 * Packet
		 */
		out.writeByte(packet.getType());
		out.writeShort(packet.getLength());
		/**
		 * Message
		 */
		out.writeByte(packet.getMessage().getFlag());
		
		/**只有REQUEST和RESPONSE才需要messageId**/
		if (packet.getMessage().getMessageType().equals(PacketType.TYPE_REQUEST)
				|| packet.getMessage().getMessageType().equals(PacketType.TYPE_RESPONSE)) {
			out.writeInt(packet.getMessage().getMessageId());
		}
		
		/**只有REQUEST和NOTIFY和PUSH才需要route **/
		if (packet.getMessage().getMessageType().equals(PacketType.TYPE_REQUEST)
				|| packet.getMessage().getMessageType().equals(PacketType.TYPE_NOTIFY)
				|| packet.getMessage().getMessageType().equals(PacketType.TYPE_PUSH)) {
			
			if (!packet.getMessage().getRouteFlag()) {
				out.writeByte(packet.getMessage().getRoutePath().getBytes().length);
				out.writeBytes(packet.getMessage().getRoutePath().getBytes());
				
			} else {
				Short routeId = RouteDictionary.getInstance().getRouteId(packet.getMessage().getRoutePath());
				out.writeShort(routeId);
			}
		}
		
		/**message body**/
		out.writeBytes(packet.getMessage().getBody());

	}

}
