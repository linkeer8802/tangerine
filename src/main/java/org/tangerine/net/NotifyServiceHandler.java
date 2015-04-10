package org.tangerine.net;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.tangerine.test.NotifyMessage;

@Component
public class NotifyServiceHandler extends ChannelDuplexHandler  {

	private static final String TANGERINE_MSG_QUEUE = "TANGERINE_MSG_QUEUE";
	
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	private Thread notifyMsgThread;
	
	private boolean isDone = false;
	
	@PostConstruct
	public void init() {
		notifyMsgThread = new Thread() {
			@Override
			public void run() {
				while(!isDone) {
					
					Object content = redisTemplate.opsForList().rightPop(TANGERINE_MSG_QUEUE, 0, TimeUnit.SECONDS);
					
					if (content != null && content instanceof String) {
						
						for (Session session : SessionManager.getInstance().getAll()) {
							NotifyMessage msg = new NotifyMessage();
							msg.setTime(System.currentTimeMillis());
							msg.setContent((String) content);
							session.getConnection().deliver(null, "onNotify", msg);
						}
					}
				}
			}
		};
		notifyMsgThread.setDaemon(true);
		notifyMsgThread.setName("NotifyMsg Thread");
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		isDone = false;
		notifyMsgThread.start();
		super.channelActive(ctx);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		isDone = true;
	}
}
