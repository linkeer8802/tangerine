package org.tangerine.net;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionManager {

	private static SessionManager instance;
	
	private final AtomicInteger sessionIdCounter = new AtomicInteger(0);
	
	 private Map<String, Session> clientSessions = new ConcurrentHashMap<String, Session>();
	
	
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
		
	}
	
	public Session getSession(String uname) {
		return clientSessions.get(uname);
	}
	
	public void removeSession(String uname) {
		Session session = clientSessions.remove(uname);
		if (session != null) {
			session.getConnection().close();
		}
	}
}
