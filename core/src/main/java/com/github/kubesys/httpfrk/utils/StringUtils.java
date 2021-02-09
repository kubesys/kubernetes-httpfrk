/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk.utils;

import java.lang.reflect.Method;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author  wuheng
 * @since   2019.2.20
 */
public class StringUtils {

	/**********************************
	 *  String utils
	 **********************************/
	
	/**
	 * convert Java object to JSON String
	 * 
	 * @param obj   obejct
	 * @return      return JSON String   
	 * @throws Exception exception
	 */
	public static String toJSONString(Object obj) throws Exception {
		if (isNull(obj)) {
			throw new NullPointerException("Object is null");
		}
		return new ObjectMapper().writeValueAsString(obj);
	}

	/**
	 * check whether the object is empty or not
	 * 
	 * @param obj  object
	 * @return     return true if 'obj' is not null, otherwise return false
	 */
	public static boolean isNull (Object obj) {
		return (obj == null) ? true : false;
	}
	
	
	/**
	 * @param method     method name
	 * @return           service type name
	 */
	public static String getServiceType(Method method) {
		String methodName = method.getName();
		return methodName.substring(0, 
				methodName.length() - "Request".length());
	}
	
	public static JsonNode toJsonNode(Map<String, String> map) {
		ObjectNode on = new ObjectMapper().createObjectNode();
		for (String key : map.keySet()) {
			on.put(key, map.get(key));
		}
		return on;
	}
	
}
