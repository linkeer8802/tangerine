package org.tangerine;

public final class Constant {

	public static class Config {
		
		public static final String DEFAULT_CHARTSET = "utf-8";
	}
	
	public static class PacketType {
		
		public static final byte TYPE_REQUEST = 0x0;
		
		public static final byte TYPE_NOTIFY = 0x1;
		
		public static final byte TYPE_RESPONSE = 0x2;
		
		public static final byte TYPE_PUSH = 0x3;
	}
}
