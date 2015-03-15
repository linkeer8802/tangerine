package org.tangerine.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import org.tangerine.Constant.Config;
import org.tangerine.Constant.MSGType;
import org.tangerine.components.RouteDictionary;
import org.tangerine.protocol.Message;
import org.tangerine.protocol.Packet;

public class PacketDecoder extends ByteToMessageDecoder{

	private Packet packet;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {                  
		
		if (packet == null && in.readableBytes() >= Packet.getHeadLength()) {
			packet = new Packet();
			packet.setType(in.readByte());
			packet.setLength(in.readShort());
		}
		
		if (packet != null && in.readableBytes() >= packet.getLength()) {
			
			ByteBuf buf = in.readBytes(packet.getLength());
			Message message = new Message();
			
			byte flag = buf.readByte(); 
			message.setMessageType(message.getMessageType(flag));
			message.setRouteFlag(message.getRouteFlag(flag));
			
			if (message.getMessageType().equals(MSGType.MSG_REQUEST)
					|| message.getMessageType().equals(MSGType.MSG_RESPONSE)) {
				message.setMessageId(buf.readInt());
			}
			
			/**只有REQUEST和NOTIFY和PUSH才需要route **/
			if (packet.getMessage().getMessageType().equals(MSGType.MSG_REQUEST)
					|| packet.getMessage().getMessageType().equals(MSGType.MSG_NOTIFY)
					|| packet.getMessage().getMessageType().equals(MSGType.MSG_PUSH)) {
				
				if (!message.getRouteFlag()) {
					byte routePathLength = buf.readByte();
					byte[] dst = new byte[routePathLength];
					buf.readBytes(dst);
					message.setRoutePath(new String(dst, Config.DEFAULT_CHARTSET));
					
				} else {
					Short routeId = buf.readShort();
					message.setRoutePath(RouteDictionary.getInstance().getRoutePath(routeId));
				}
			}
			
			
			byte[] body = new byte[buf.readableBytes()];
			buf.readBytes(body, 0, buf.readableBytes());
			message.setBody(body);
			
			packet.setMessage(message);
			out.add(packet);
			packet = null;
		}
	}

}
