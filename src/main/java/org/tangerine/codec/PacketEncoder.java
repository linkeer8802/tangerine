//package org.tangerine.codec;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.MessageToByteEncoder;
//
//import org.tangerine.Constant.PacketType;
//import org.tangerine.protocol.Message;
//import org.tangerine.protocol.PacketEntity;
//import org.tangerine.protocol.PacketHead;
//
//public class PacketEncoder extends MessageToByteEncoder<PacketEntity>{
//
//	@Override
//	protected void encode(ChannelHandlerContext ctx, PacketEntity packet, ByteBuf out) throws Exception {
//		/**
//		 * 包头
//		 */
//		PacketHead.encode(packet.getPacketHead(), out);
//		
//		/**
//		 * 数据包
//		 */
//		if (packet.getPacketHead().getType().equals(PacketType.PCK_DATA) &&
//				packet.getBody() instanceof Message) {
//			Message.encode((Message) packet.getBody(), out);
//		} else {
//			out.writeBytes((ByteBuf) packet.getBody());
//		}
//	}
//}
