package org.tangerine.net;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tangerine.Constant.MSGType;
import org.tangerine.annotation.MessageMapping;
import org.tangerine.protocol.Message;

public class MessageRouter {

	private Map<String, RouterHandler> messageHandlers = new HashMap<String, RouterHandler>();
	
	public void route(Connection connection, Message message) throws Exception {
		
		byte type = message.getMessageType();
		RoutePath routePath = new RoutePath(message.getRoutePath());
		
		RouterHandler routerHandler = getHandler(routePath);
		if (routerHandler == null || routerHandler.getHandler() == null) {
			noHandlerFound(connection, message);
			return;
		}
		
		for (Method method : routerHandler.getHandler().getClass().getDeclaredMethods()) {
			
			MessageMapping messageMapping = method.getAnnotation(MessageMapping.class);
			if (messageMapping.type().equals(type) 
					&& routePath.getAction().equals(messageMapping.value())) {
				
				ResponseMessage responseMessage = null;
				
				Object result = method.invoke(routerHandler.getHandler(), 
						getHandlerMethodArgs(method, connection, message, responseMessage));
				
				if (type == MSGType.MSG_REQUEST) {
					
					connection.deliver(message.getMessageId(), null, result);
				}
				break;
			}
		}
		
	}
		
	private void noHandlerFound(Connection connection, Message message) {
		
	}

	private RouterHandler getHandler(RoutePath routePath) {
		String handlerName = routePath.getHandler();
		if (!handlerName.endsWith("Handler")) {
			handlerName = handlerName + "Handler";
		}
		return messageHandlers.get(handlerName);
	}
	
	public Object[] getHandlerMethodArgs(Method method, Connection connection,
			Message message, ResponseMessage responseMessage) throws Exception {
		
		List<Object> args = new ArrayList<Object>(method.getParameterCount());
		Class<?>[] types = method.getParameterTypes();
		for (Class<?> clz : types) {
			
			if (clz.isAssignableFrom(Connection.class)) {
				args.add(connection);
				
			} else if (clz.isAssignableFrom(Message.class)) {
				args.add(message);
				
			} else if (clz.isAssignableFrom(ResponseMessage.class)) {
				args.add(responseMessage);
				
			} else {
				try {
					args.add(clz.newInstance());
					
				} catch (Exception e) {
					args.add(null);
				}
			}
		}
		
		return args.toArray();
	}
}
