package org.tangerine.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

import org.tangerine.protocol.Message;
import org.tangerine.protocol.Packet;

public class Connection {

	private ChannelHandlerContext ctx;
	
	private boolean connected;
	
	private boolean closed;
	
	private long connectTime;
	
	private Session session;
	
	public Connection(ChannelHandlerContext ctx) {
		this.ctx = ctx;
		this.closed = false;
		this.connected = false;
	}
	
	public void deliver(Packet packet) {
		
		ctx.writeAndFlush(packet);
		
	}
	
	public void deliver(Integer reqId, String route, Object body) {
		Message message = new Message();
		message.setMessageId(reqId);
	}
	/**
	 * 延迟(单位为毫秒)发送数据包
	 * @param packet
	 * @param delay
	 * @return ScheduledFuture
	 */
	public ScheduledFuture<?> scheduleDeliver(final Packet packet, long delay) {
		return ctx.executor().schedule(new Runnable() {
			@Override
			public void run() {
				ctx.writeAndFlush(packet);
			}
		}, delay, TimeUnit.MILLISECONDS);
	}
	
	public void init(Session session) {
		this.session = session;
	}
	
	public EventExecutor getEventExecutor() {
		
		return ctx.executor();
		
	}
	
	public void close() {
		ctx.close();
		this.closed = true;
		this.connected = false;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public long getConnectTime() {
		return connectTime;
	}

	public void setConnectTime(long connectTime) {
		this.connectTime = connectTime;
	}

	public Session getSession() {
		return session;
	}
	
	public boolean isAuth() {
		return this.session != null;
	}
}
