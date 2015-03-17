//package org.tangerine.codec;
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.MessageToMessageDecoder;
//
//import java.nio.ByteBuffer;
//import java.util.List;
//
//import org.tangerine.Constant.Config;
//import org.tangerine.Constant.MSGType;
//import org.tangerine.Constant.PacketType;
//import org.tangerine.protocol.Message;
//import org.tangerine.protocol.Packet;
//
//public class MessageDecoder extends MessageToMessageDecoder<Packet> {
//
//	@Override
//	protected void decode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception {
//		
//		//不是数据包，不需要decode
//		if (!packet.getType().equals(PacketType.PCK_DATA)) {
//			
//			out.add(packet);
//			
//		} else {
//		
//			Message message = new Message();
//			ByteBuffer buf = ByteBuffer.wrap(packet.getBody());
//			
//			byte flag = buf.get();
//			message.setMessageType(message.getMessageType(flag));
//			message.setRouteFlag(message.getRouteFlag(flag));
//			
//			/**只有REQUEST和NOTIFY和RESPONSE才需要MessageId **/
//			if (message.getMessageType().equals(MSGType.MSG_REQUEST)
//					|| message.getMessageType().equals(MSGType.MSG_RESPONSE)) {
//				message.setMessageId(buf.getInt());
//			}
//			
//			/**只有REQUEST和NOTIFY和PUSH才需要route **/
//			if (message.getMessageType().equals(MSGType.MSG_REQUEST)
//					|| message.getMessageType().equals(MSGType.MSG_NOTIFY)
//					|| message.getMessageType().equals(MSGType.MSG_PUSH)) {
//				
//				if (!message.getRouteFlag()) {
//					byte routePathLength = buf.get();
//					byte[] dst = new byte[routePathLength];
//					buf.get(dst);
//					message.setRoutePath(new String(dst, Config.DEFAULT_CHARTSET));
//					
//				} else {
//					Short routeId = buf.getShort();
//					message.setRoutePath(routeId.toString());
//				}
//			}
//			
//			byte[] body = new byte[buf.remaining()];
//			buf.get(body);
//			message.setBody(body);
//			
//			out.add(message);
//		}
//	}
//}
