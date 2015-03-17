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

import org.tangerine.codec.Decoder;
import org.tangerine.codec.Encoder;
import org.tangerine.net.Connection;
import org.tangerine.net.MessageRouter;
import org.tangerine.net.PacketHandler;
import org.tangerine.protocol.Message;
import org.tangerine.protocol.Packet;

public class NettyConnector implements Connector {
	
	private String host;
	private Integer port;

	private Channel channel;
	
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
			ch.pipeline().addLast("packetDecoder",  new Decoder());
			ch.pipeline().addLast("packetEncoder",  new Encoder());
			ch.pipeline().addLast("messageHandler",  this);
		}
	}
}
