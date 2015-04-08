package org.tangerine.connector;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.tangerine.Tangerine;
import org.tangerine.codec.Decoder;
import org.tangerine.codec.Encoder;
import org.tangerine.net.NotifyServiceHandler;

public class NettyConnector implements Connector {
	
	private String host;
	private Integer port;

	private Channel channel;
	
	private NettyIOSocket ioSocket;
	
	public NettyConnector(String host, Integer port) {
		this.host = host;
		this.port = port;
		
		ioSocket = NettyIOSocket.getInstance();
	}
	
	@Override
	public void start() throws Exception {
		
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class) 
			.handler(new ChannelInitializer<ServerSocketChannel>(){
				@Override
				protected void initChannel(ServerSocketChannel ch) throws Exception {
					ch.pipeline().addLast("log", new LoggingHandler(LogLevel.INFO));
					ch.pipeline().addLast("redis", Tangerine.getInstance().getBean(NotifyServiceHandler.class));
				}
				
			}).childHandler(new ServerChannelInitializer())
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
//			ch.pipeline().addLast("ping", new IdleStateHandler(60, 15, 13));
			ch.pipeline().addLast("ioSocket",  ioSocket);
		}
	}
}
