package org.tangerine.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import org.tangerine.Constant.PacketType;
import org.tangerine.protocol.Message;
import org.tangerine.protocol.Packet;
import org.tangerine.protocol.PacketHead;

public class Decoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		//头长度
		if (in.readableBytes() < PacketHead.getHeadLength()) {
			return;
		}
		//读取头
		in.markReaderIndex();
		PacketHead packetHead = PacketHead.decode(in);
		
		//消息体长度
		if (in.readableBytes() < packetHead.getLength()) {
			in.resetReaderIndex();
			return;
		}
		
		/**
		 * 已读取到完整包数据
		 */
		Packet packet = new Packet();
		packet.setPacketHead(packetHead);
		
		/**
		 * 数据包
		 */
		if (packetHead.getType().equals(PacketType.PCK_DATA)) {
			packet.setBody(Message.decode(in));
		} else {
			packet.setBody(in.readBytes(packetHead.getLength()));
		}
		
		out.add(packet);
	}
}
