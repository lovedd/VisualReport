package com.shiker.web.service.util;

import com.alibaba.fastjson.JSON;

/**
 * Json操作工具类
 * 
 * @author DangT
 * @date 2016年5月17日 下午3:11:26
 * @version V1.0
 */
public class JsonUtil {

	private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 将对象转换成Json
	 *
	 * @param object
	 * @return
	 */
	public static String toJson(Object object) {

		return JSON.toJSONStringWithDateFormat(object, YYYY_MM_DD_HH_MM_SS);

	}

	/**
	 * 将Json串转换成对象
	 *
	 * @param text
	 * @param clazz
	 * @return
	 */
	public static <T> T toBean(String text, Class<T> clazz) {

		return JSON.parseObject(text, clazz);

	}

}
