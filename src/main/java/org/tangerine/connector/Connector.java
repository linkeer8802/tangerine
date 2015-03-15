package org.tangerine.connector;


/**
 * Connector
 * @author weird
 *
 */
public interface Connector {

	public void start() throws Exception;
	
	public void stop();
}
