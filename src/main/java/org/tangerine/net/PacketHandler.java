package org.tangerine.net;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.tangerine.Constant.Config;
import org.tangerine.Constant.PacketType;
import org.tangerine.protocol.Packet;
import org.tangerine.protocol.PacketHead;
import org.tangerine.util.JsonUtil;

public class PacketHandler {

	public void handle(Connection connection, Packet packet) throws Exception {
		
		byte type = packet.getPacketHead().getType();
		String body = new String(((ByteBuf)packet.getBody()).array(), Config.DEFAULT_CHARTSET);
		
		if (type == PacketType.PCK_HANDSHAKE) {
			handleHandshake(connection, JsonUtil.fromJson(body, HandShakeRequest.class));
			
		} else if (type == PacketType.PCK_SHAKE_ACK) {
			handleShakeACK(connection);
			
		} else if (type == PacketType.PCK_HEARTBEAT) {
			handleHeartbeat(connection);
		}
	}

	private void handleHeartbeat(Connection connection) {
		connection.scheduleDeliver(Packet.buildHeartbeatPacket(), 3000);
	}

	private void handleShakeACK(Connection connection) {
		connection.setConnected(true);
	}

	private void handleHandshake(Connection connection, HandShakeRequest handShakeRequest) {
		//保存客户端发来的握手信息
		//发送握手响应
		HandShakeResponse response = new HandShakeResponse();
		response.setCode(200);
		response.setHeartbeat(3);
		
		ByteBuf body = null;
		try {
			body = Unpooled.wrappedBuffer(JsonUtil
					.toHtmlPrettyJson(response).getBytes(Config.DEFAULT_CHARTSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		connection.deliver(new Packet(new PacketHead(PacketType.PCK_HANDSHAKE, body.readableBytes()), body));
	}
}
