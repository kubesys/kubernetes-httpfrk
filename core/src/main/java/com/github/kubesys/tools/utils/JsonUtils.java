/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.tools.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kubesys.httpfrk.utils.JavaUtils;


/**
 * @author wuheng
 * @since 2019.3.17
 * 
 *  mysqldump -h 172.17.0.2 -u root -p demo user -- dbname_users.sql
 *  
 */

public class JsonUtils {
	
	public static String createJSON(Class<?> clazz) throws Exception {
		return new ObjectMapper().writeValueAsString(createObject(clazz));
	}
	
	public static String createJSON(Map<String, String> map) throws Exception {
		return new ObjectMapper().writeValueAsString(map);
	}
	
	protected static Map<String, Object> createObject(Class<?> clazz) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		for (Method method: clazz.getDeclaredMethods()) {
			if ((!method.getName().startsWith("set")
					|| !Modifier.isStatic(method.getModifiers()))
					&& Modifier.isPublic(method.getModifiers())
					&& method.getParameterCount() == 1) {
				String value = method.getGenericParameterTypes()[0].getTypeName();
				String key = method.getName().substring(3).toLowerCase();
				if (JavaUtils.isPrimitive(value)
						|| JavaUtils.isStringList(value)
						|| JavaUtils.isStringSet(value)
						|| JavaUtils.isStringStringMap(value)) {
					model.put(key, value);
				}  else if (JavaUtils.isObjectList(value) 
						|| JavaUtils.isObjectSet(value)) {
					model.put(key, createObject(Class.forName(
							value.substring(1, value.length() -1 ).trim())));
					model.put(key, createObject(Class.forName(
							value.substring(1, value.length() -1 ).trim())));
				} else if (JavaUtils.isStringObjectMap(value)) {
					Map<String, Object> newValue = new HashMap<String, Object>();
					int start = value.indexOf(",");
					int end = value.lastIndexOf(">");
					String classname = value.substring(start + 1, end - 1).trim();
					newValue.put("key1", createObject(Class.forName(classname)));
					newValue.put("key2", createObject(Class.forName(classname)));
					model.put(key, newValue);
				}
			}
		}
		return model;
	}
	
}
