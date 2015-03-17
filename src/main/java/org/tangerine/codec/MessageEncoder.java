//package org.tangerine.codec;
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.MessageToMessageEncoder;
//
//import java.util.List;
//
//import org.tangerine.Constant.MSGType;
//import org.tangerine.components.RouteDictionary;
//import org.tangerine.protocol.Message;
//import org.tangerine.protocol.Packet;
//
//
//public class MessageEncoder extends MessageToMessageEncoder<Message> {
//
//	@Override
//	protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
//		/**
//		 * Message
//		 */
//		out.writeByte(packet.getMessage().getFlag());
//		
//		/**只有REQUEST和RESPONSE才需要messageId**/
//		if (packet.getMessage().getMessageType().equals(MSGType.MSG_REQUEST)
//				|| packet.getMessage().getMessageType().equals(MSGType.MSG_RESPONSE)) {
//			out.writeInt(packet.getMessage().getMessageId());
//		}
//		
//		/**只有REQUEST和NOTIFY和PUSH才需要route **/
//		if (packet.getMessage().getMessageType().equals(MSGType.MSG_REQUEST)
//				|| packet.getMessage().getMessageType().equals(MSGType.MSG_NOTIFY)
//				|| packet.getMessage().getMessageType().equals(MSGType.MSG_PUSH)) {
//			
//			if (!packet.getMessage().getRouteFlag()) {
//				out.writeByte(packet.getMessage().getRoutePath().getBytes().length);
//				out.writeBytes(packet.getMessage().getRoutePath().getBytes());
//				
//			} else {
//				Short routeId = RouteDictionary.getInstance().getRouteId(packet.getMessage().getRoutePath());
//				out.writeShort(routeId);
//			}
//		}
//		
//		/**message body**/
//		out.writeBytes(packet.getMessage().getBody());
//	}
//}
