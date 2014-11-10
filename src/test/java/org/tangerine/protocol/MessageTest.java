package org.tangerine.protocol;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageTest {

	private Message message;
	
	@Before
	public void setUp() throws Exception {
		message = new Message();
	}

	@Test
	public void testGetRouteWithFlag() {
		byte flag = (byte) 0x01;
		Assert.assertTrue(message.getRouteFlag(flag));
		flag = (byte) 0x02;
		Assert.assertFalse(message.getRouteFlag(flag));
	}
	
	
	@Test
	public void testMessageTypeWithFlag() {
		byte flag = (byte) 0x00;
		Assert.assertEquals(0, message.getMessageType(flag).intValue());
		flag = (byte) 0x02;
		Assert.assertEquals(1, message.getMessageType(flag).intValue());
		flag = (byte) 0x04;
		Assert.assertEquals(2, message.getMessageType(flag).intValue());
		flag = (byte) 0x06;
		Assert.assertEquals(3, message.getMessageType(flag).intValue());
	}
	
	@Test
	public void testGetFlag() {
		message.setRouteFlag(true);
		message.setMessageType((byte)0x0);
		Assert.assertEquals(1, message.getFlag());
		
		message.setRouteFlag(false);
		message.setMessageType((byte)0x1);
		Assert.assertEquals(2, message.getFlag());
		
		message.setRouteFlag(true);
		message.setMessageType((byte)0x2);
		Assert.assertEquals(5, message.getFlag());
		
		message.setRouteFlag(false);
		message.setMessageType((byte)0x3);
		Assert.assertEquals(6, message.getFlag());
	}
}
