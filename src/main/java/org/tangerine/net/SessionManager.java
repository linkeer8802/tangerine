package org.tangerine.net;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.tangerine.Tangerine;

public class SessionManager {

	private static SessionManager instance;
	
	private final AtomicInteger sessionIdCounter = new AtomicInteger(0);
	
	 private Map<String, Session> clientSessions = new ConcurrentHashMap<String, Session>();
	
	 private RedisTemplate<String, Object> redisTemplate;
	 
	 public SessionManager() {
		 redisTemplate = Tangerine.getInstance().getContext().getBean("redisTemplate", RedisTemplate.class);
	}
	
    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                instance = new SessionManager();
            }
        }
        return instance;
    }
    
    public static Integer getNextSessionId() {
    	return SessionManager.instance.sessionIdCounter.incrementAndGet();
    }

	public void createNewSession(Session session) {
		
		clientSessions.put(session.getUname(), session);
		session.getConnection().init(session);
		
		OnlineUser onlineUser = new OnlineUser();
		BeanUtils.copyProperties(session, onlineUser);
		
		redisTemplate.opsForHash().put("TANGERINE-ONLINE-USERS", session.getUname(), onlineUser);
	}
	
	public Session getSession(String uname) {
		return clientSessions.get(uname);
	}
	
	public Collection<Session> getAll() {
		return clientSessions.values();
	}
	
	public void removeSession(String uname) {
		Session session = clientSessions.remove(uname);
		if (session != null) {
			session.getConnection().close();
		}
	}
}
