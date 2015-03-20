package org.tangerine.components;

public class AppContext {

	public static final AppContext instance = new AppContext();
	
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

	public void setConfig(AppConfig config) {
		this.config = config;
	}

	public RouteDictionary getRouteDictionary() {
		return routeDictionary;
	}

	public void setRouteDictionary(RouteDictionary routeDictionary) {
		this.routeDictionary = routeDictionary;
	}
}
