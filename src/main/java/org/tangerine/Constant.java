package org.tangerine;

public final class Constant {

	public static class Config {
		
		public static final String DEFAULT_CHARTSET = "utf-8";
	}
	
	public static class PacketType {
		
		public static final byte MSG_REQUEST = 0x0;
		
		public static final byte MSG_NOTIFY = 0x1;
		
		public static final byte MSG_RESPONSE = 0x2;
		
		public static final byte MSG_PUSH = 0x3;
	}
	
	public static class MSGType {
		
		public static final byte MSG_REQUEST = 0x0;
		
		public static final byte MSG_NOTIFY = 0x1;
		
		public static final byte MSG_RESPONSE = 0x2;
		
		public static final byte MSG_PUSH = 0x3;
	}
}
