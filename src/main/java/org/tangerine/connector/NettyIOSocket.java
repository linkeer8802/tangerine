package org.tangerine.connector;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import org.tangerine.net.Connection;
import org.tangerine.net.MessageRouter;
import org.tangerine.net.PacketHandler;
import org.tangerine.protocol.Message;
import org.tangerine.protocol.Packet;

public class NettyIOSocket extends ChannelDuplexHandler {
	
	private static final AttributeKey<Connection> connectionAttr = AttributeKey.valueOf("CONNECTION");
	
	private static final NettyIOSocket instance = new NettyIOSocket();
	
	private PacketHandler packetHandler;
	private MessageRouter messageRouter;
	
	protected NettyIOSocket() {
		this.packetHandler = new PacketHandler();
		this.messageRouter = new MessageRouter();
	}
	
	public static NettyIOSocket getInstance() {
		return instance;
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
			
			Packet packet = (Packet) msg;
			
			if (packet.getBody() instanceof Message) {
				messageRouter.route(connection, (Message) msg);
				
			} else {
				packetHandler.handle(connection, (Packet) msg);
			}
			
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
