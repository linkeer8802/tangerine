package org.tangerine.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.tangerine.Constant.Config;
import org.tangerine.Constant.PacketType;
import org.tangerine.components.AppContext;
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
		System.out.println("server: recive Heartbeat packet at " + new Date());
		int delay = AppContext.getInstance().getConfig().getHeartbeat()*1000;
		System.out.println("delay:" + delay);
		connection.scheduleDeliver(Packet.buildHeartbeatPacket(), delay);
	}

	private void handleShakeACK(Connection connection) {
		connection.setConnected(true);
		System.out.println("handleShake success.");
	}

	private void handleHandshake(Connection connection, HandShakeRequest handShakeRequest) {
		HandShakeResponse response = new HandShakeResponse();
		response.setCode(200);
		response.setHeartbeat(AppContext.getInstance().getConfig().getHeartbeat());
		//客户端发来的握手信息
		if (!handShakeRequest.getVersion().equals("1.0")) {
			response.setCode(501);
		}
		
		
		ByteBuf body = null;
		try {
			body = Unpooled.wrappedBuffer(JsonUtil
					.toJson(response).getBytes(Config.DEFAULT_CHARTSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			response.setCode(500);
		}
		//发送握手响应
		connection.deliver(new Packet(new PacketHead(PacketType.PCK_HANDSHAKE, body.readableBytes()), body));
	}
}
