package org.tangerine.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import org.tangerine.protocol.Packet;


public class MessageEncoder extends MessageToMessageEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
		
	}
}
