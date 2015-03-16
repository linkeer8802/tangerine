package org.tangerine.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterUtil {

	public static Class<?> getTypeClass(Type parameterType) {
		Class<?> typeClz = null;
		if (parameterType instanceof Class) {
			typeClz = (Class<?>)parameterType;
		} else if (parameterType instanceof ParameterizedType) {
			typeClz = (Class<?>)((ParameterizedType)parameterType).getRawType();
		} else if (parameterType instanceof GenericArrayType) {
			typeClz = (Class<?>)((GenericArrayType)parameterType).getGenericComponentType();
		} else {
			throw new IllegalArgumentException("Unsupport parameter type[" + parameterType + "].");
		}
		return typeClz;
	}
	
	public static Class<?> getParameterizedType(Type type) {
		return ((Class<?>) ((ParameterizedType)type).getActualTypeArguments()[0]);
	}
}
