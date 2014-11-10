package org.tangerine.connector;

import java.nio.ByteBuffer;

import org.tangerine.protocol.Packet;

/**
 * Connector
 * @author weird
 *
 */
public interface Connector {

	public void start();
	
	public void stop();
	
	public ByteBuffer encode(Packet packet);
	
	public Packet decode(ByteBuffer buffer);
	
}
