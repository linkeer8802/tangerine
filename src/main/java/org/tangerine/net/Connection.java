package org.tangerine.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.ScheduledFuture;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import org.tangerine.Constant.Config;
import org.tangerine.Constant.MSGType;
import org.tangerine.Tangerine;
import org.tangerine.components.AppConfig;
import org.tangerine.components.AppContext;
import org.tangerine.protocol.Message;
import org.tangerine.protocol.Packet;
import org.tangerine.util.JsonUtil;

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
		AppConfig config = Tangerine.getInstance().getContext().getBean(AppContext.class).getConfig();
		Message message = new Message();
		
		if (reqId == null) {
			message.setMessageType(MSGType.MSG_PUSH);
		} else {
			message.setMessageType(MSGType.MSG_RESPONSE);
			message.setMessageId(reqId);
		}
		
		if (route == null) {
			message.setRouteFlag(config.getRouteFlag());
		} else {
			
			String routePath = route;
			boolean routeFlag = false;
			
			if (config.getRouteFlag()) {
				Short routeId = Tangerine.getInstance().getContext().getBean(AppContext.class).getRouteDictionary().getRouteId(route);
				if (routeId != null) {
					routePath = routeId.toString();
					routeFlag = true;
				}
			}
			message.setRouteFlag(routeFlag);
			message.setRoutePath(routePath);
		}
		
		try {
			message.setBody(JsonUtil.toJson(body).getBytes(Config.DEFAULT_CHARTSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		ctx.writeAndFlush(new Packet(message));
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
