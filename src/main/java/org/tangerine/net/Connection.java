package org.tangerine.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutor;

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
