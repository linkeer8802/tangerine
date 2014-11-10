package org.tangerine.components;

import java.util.HashMap;
import java.util.Map;

public class RouteDictionary {

	private Map<Short,String> routeIdToPathDictionarys = new HashMap<Short,String>();
	
	private Map<String, Short> routePathToIdDictionarys = new HashMap<String, Short>();
	
	private static RouteDictionary instance = new RouteDictionary();
	
	private RouteDictionary() {
	}
	
	public static RouteDictionary getInstance() {
		return instance;
	}
	
	public String getRoutePath(Short routeId) {
		return routeIdToPathDictionarys.get(routeId);
	}
	
	public Short getRouteId(String routePath) {
		return routePathToIdDictionarys.get(routePath);
	}
	
	
	public void init() {
		
	}
}
