package org.tangerine.connector;

import org.tangerine.net.Connection;
import org.tangerine.net.MessageRouter;
import org.tangerine.net.PacketHandler;
import org.tangerine.protocol.Message;
import org.tangerine.protocol.Packet;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class NettyChannelHandler extends ChannelDuplexHandler {
	
	private static final AttributeKey<Connection> connectionAttr = AttributeKey.valueOf("CONNECTION");
	
	private PacketHandler packetHandler;
	private MessageRouter messageRouter;
	
	public NettyChannelHandler() {
		this.packetHandler = new PacketHandler();
		this.messageRouter = new MessageRouter();
	}
	/**
	 * 请求连接
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//新建Connection
		Connection connection = new Connection(ctx);
		ctx.attr(connectionAttr).set(connection);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		Connection connection = ctx.attr(connectionAttr).get();
		
		if(msg instanceof Packet) {
			
			packetHandler.handle(connection, (Packet) msg);
			
		} else if(msg instanceof Message) {
			
			messageRouter.route(connection, (Message) msg);
			
		} else {
			connection.close();
			throw new Exception("unknown packet data !");
		} 
	}
	/**
	 * 断开连接
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}
}
