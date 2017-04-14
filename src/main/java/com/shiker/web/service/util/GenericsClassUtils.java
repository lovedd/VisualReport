package com.shiker.web.service.util;

import java.lang.reflect.ParameterizedType;

/**
 * 发射工具类
 * 
 * @author DangT
 * @date 2016年5月6日 上午10:46:34
 * @version V1.0
 */
public class GenericsClassUtils {

	private GenericsClassUtils() {
	}

	@SuppressWarnings("rawtypes")
	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	@SuppressWarnings("rawtypes")
	public static Class getSuperClassGenricType(Class clazz, int index) {

		java.lang.reflect.Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		java.lang.reflect.Type params[] = ((ParameterizedType) genType)
				.getActualTypeArguments();
		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		} else {
			return (Class) params[index];
		}

	}

}
