package org.tangerine.util;

import com.google.gson.GsonBuilder;

public class JsonUtil {

	public static String toHtmlPrettyJson(Object object) {
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(object);
		json = json.replace("\n", "<br/>");
		json = json.replace(" ", "&nbsp;");
		return json;
	}
	
	public static <T> T fromJson(String json, Class<T> classOfT) {
		return new GsonBuilder().create().fromJson(json, classOfT);
	}
	
	public static String toJson(Object src) {
		return new GsonBuilder().create().toJson(src);
	}
}
