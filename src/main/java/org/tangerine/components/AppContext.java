package org.tangerine.components;

public class AppContext {

	private static final AppContext instance = new AppContext();
	
	private AppConfig config;
	
	private RouteDictionary routeDictionary;
	
	private AppContext() {
		config = new AppConfig();
		routeDictionary = new RouteDictionary();
	}
	
	public static AppContext getInstance() {
		return instance;
	}

	public AppConfig getConfig() {
		return config;
	}

	public RouteDictionary getRouteDictionary() {
		return routeDictionary;
	}
}
