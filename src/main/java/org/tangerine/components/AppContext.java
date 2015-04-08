package org.tangerine.components;

public class AppContext {

	private AppConfig config;
	
	private RouteDictionary routeDictionary;
	
	private String connectionString;
	
	private AppContext() {
		config = new AppConfig();
		routeDictionary = new RouteDictionary();
	}

	public AppConfig getConfig() {
		return config;
	}

	public RouteDictionary getRouteDictionary() {
		return routeDictionary;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
}
