//package org.tangerine.codec;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.ByteToMessageDecoder;
//
//import java.util.List;
//
//import org.tangerine.protocol.Packet;
//
//public class PacketDecoder extends ByteToMessageDecoder{
//
//	@Override
//	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {                  
//		//头长度
//		if (in.readableBytes() < Packet.getHeadLength()) {
//			return;
//		}
//		//读取头
//		in.markReaderIndex();
//		byte type = in.readByte();
//		short bodyLength = in.readShort();
//		
//		//消息体长度
//		if (in.readableBytes() < bodyLength) {
//			in.resetReaderIndex();
//			return;
//		}
//		
//		/**
//		 * 已读取到完整包数据
//		 */
//		Packet packet = new Packet();
//		packet.setType(type);
//		packet.setLength(bodyLength);
//		
//		ByteBuf buf = in.readBytes(packet.getLength());
//		byte[] dst = new byte[packet.getLength()];
//		buf.readBytes(dst);
//		packet.setBody(dst);
//		
//		out.add(packet);
//	}
//
//}
