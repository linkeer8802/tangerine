package org.tangerine.connector;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;

import org.tangerine.codec.PacketDecoder;
import org.tangerine.codec.PacketEncoder;
import org.tangerine.net.Connection;
import org.tangerine.protocol.Packet;

public abstract class NettyConnector extends ChannelDuplexHandler implements Connector {
	
	private String host;
	private Integer port;

	private Channel channel;
	private static final AttributeKey<Connection> connectionAttr = AttributeKey.valueOf("CONNECTION");
	
	public NettyConnector(String host, Integer port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	public void start() throws Exception {
		
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class) 
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ServerChannelInitializer())
			.option(ChannelOption.SO_BACKLOG, 1024);
			
			channel = bootstrap.bind(host, port).sync().channel();
			channel.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	@Override
	public void stop() {
		channel.close();
	}
	
	class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

		@Override
		public void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast(
					new PacketDecoder(),
					new PacketEncoder(),
					this
				);
		}
	}
	
	/**********************************Handler***********************************/
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
		
		if(!(msg instanceof Packet)) {
			throw new Exception("unknown packet data !");
		} 
		
		Packet packet = (Packet) msg;
		
		try {
			dispatchPacket(ctx.attr(connectionAttr).get(), packet);
			
		} catch (Exception e) {
			e.printStackTrace();
			Connection connection = (Connection) ctx.attr(connectionAttr);
            connection.close();
		}
	}
	/**
	 * 分发数据包
	 * @param connection
	 * @param packet
	 */
	public abstract void dispatchPacket(Connection connection, Packet packet);
	
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
