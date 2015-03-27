package org.tangerine.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.tangerine.Constant.Config;
import org.tangerine.Constant.MSGType;
import org.tangerine.Constant.PacketType;
import org.tangerine.codec.Decoder;
import org.tangerine.codec.Encoder;
import org.tangerine.net.Connection;
import org.tangerine.net.HandShakeRequest;
import org.tangerine.protocol.Message;
import org.tangerine.protocol.Packet;
import org.tangerine.protocol.PacketHead;
import org.tangerine.util.JsonUtil;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

public class Client {

	public void start() throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                	ch.pipeline().addLast("packetDecoder",  new Decoder());
        			ch.pipeline().addLast("packetEncoder",  new Encoder());
        			ch.pipeline().addLast("ioSocket",  new ClientHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect("localhost", 9770).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
	}
	
	static class ClientHandler extends ChannelDuplexHandler {
		
		private static final AttributeKey<Connection> connectionAttr = AttributeKey.valueOf("CONNECTION");
		/**
		 * 请求连接
		 */
		@Override
		public void channelActive(final ChannelHandlerContext ctx) throws Exception {
			sendDataPacket(ctx);
			//新建Connection
			Connection connection = new Connection(ctx);
			ctx.attr(connectionAttr).set(connection);
		}

		private void sendHandShakePacket(ChannelHandlerContext ctx) throws UnsupportedEncodingException {
			HandShakeRequest handShakeRequest = new HandShakeRequest();
			handShakeRequest.setVersion("1.0");
			ByteBuf body = Unpooled.wrappedBuffer(JsonUtil
					.toJson(handShakeRequest).getBytes(Config.DEFAULT_CHARTSET));
			Packet packet = new Packet(new PacketHead(PacketType.PCK_HANDSHAKE, body.readableBytes()), body);
			
			ctx.writeAndFlush(packet);
		}
		
		private void sendDataPacket(ChannelHandlerContext ctx) throws IOException {
			Message message = new Message();
			HelloMessage hello = new HelloMessage();
			hello.setFrom("user1");
			hello.setMsg("hi, I am user1~");
			hello.setTime(System.currentTimeMillis());
			hello.setTo("user2");
			
//			ByteBuf body = Unpooled.wrappedBuffer(JsonUtil
//					.toJson(hello).getBytes(Config.DEFAULT_CHARTSET));
			
			Codec<HelloMessage> codec = ProtobufProxy.create(HelloMessage.class);
			
			message.setBody(codec.encode(hello));
			message.setMessageId(1);
			message.setMessageType(MSGType.MSG_REQUEST);
			message.setRouteFlag(false);
			message.setRoutePath("chart.hello.sayHello2");
			
			ctx.writeAndFlush(new Packet(message));
		}
		
		private void sendHeartBeatPacket(ChannelHandlerContext ctx) throws UnsupportedEncodingException {
			ctx.writeAndFlush(Packet.buildHeartbeatPacket());
		}
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			Connection connection = ctx.attr(connectionAttr).get();
			
			if(msg instanceof Packet) {
				
				Packet packet = (Packet) msg;
				System.out.println("client: recive Heartbeat packet at " + new Date());
				connection.scheduleDeliver(Packet.buildHeartbeatPacket(), 3000);
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
	
	public static void main(String[] args) throws Exception {
		new Client().start();
	}
}
