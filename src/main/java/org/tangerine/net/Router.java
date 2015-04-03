package org.tangerine.net;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.tangerine.Constant.Config;
import org.tangerine.Constant.MSGType;
import org.tangerine.annotation.RuoteMapping;
import org.tangerine.components.AppContext;
import org.tangerine.protocol.Message;
import org.tangerine.util.JsonUtil;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

public class Router {

	private static Router instance;
	
	private Map<String, RouterHandler> messageHandlers = new HashMap<String, RouterHandler>();
	
	private Router() {}
	
	public static synchronized Router getInstance() {
		if (instance == null) {
			instance = new Router();
		}
		return instance;
	}
	
	public static void registerRouterHandler(Object handler) {
		
		for (Method method : handler.getClass().getDeclaredMethods()) {
			
			RuoteMapping mapping = method.getAnnotation(RuoteMapping.class);
			if (mapping == null) {
				continue;
			}
			
			String routePath = StringUtils.uncapitalize(handler.getClass().getSimpleName()) + "." + mapping.value();
			Router.getInstance().messageHandlers.put(routePath, new RouterHandler(handler));
		}
	}
	
	public void route(Connection connection, Message message) throws Exception {
		
		System.out.println("message body size:" + message.getBody().length);
		
		byte type = message.getMessageType();
		RoutePath routePath = new RoutePath(message.getRoutePath());
		
		RouterHandler routerHandler = getHandler(routePath);
		Object handler = routerHandler.getHandler();
		if (routerHandler == null || handler == null) {
			noHandlerFound(connection, message);
			return;
		}
		
		for (Method method : routerHandler.getHandler().getClass().getDeclaredMethods()) {
			
			RuoteMapping messageMapping = method.getAnnotation(RuoteMapping.class);
			if (routePath.getAction().equals(messageMapping.value())) {
				
				ResponseMessage responseMessage = null;
				
				Object result = method.invoke(handler, 
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
		return messageHandlers.get(handlerName + "." + routePath.getAction());
	}
	
	public Object[] getHandlerMethodArgs(Method method, Connection connection,
			Message message, ResponseMessage responseMessage) throws Exception {
		
		List<Object> args = new ArrayList<Object>();
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
					if (AppContext.getInstance().getConfig().getUseProtobuf()) {
						//probuf
						@SuppressWarnings("rawtypes")
						Codec codec = ProtobufProxy.create(clz);
						args.add(codec.decode(message.getBody()));
					} else {
						//json
						String json = new String(message.getBody(), Config.DEFAULT_CHARTSET);
						args.add(JsonUtil.fromJson(json, clz));
					}
					
				} catch (Exception e) {
					args.add(null);
				}
			}
		}
//		throw new RuntimeException("dfdf");
		return args.toArray();
	}
}
