/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk.utils;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wuheng
 * @since 2019.3.7
 * 
 */

public class JavaUtils {

	private static final Map<String, String> typeMapping = new HashMap<String, String>();

	static {
		typeMapping.put(String.class.getName(), String.class.getName());
		typeMapping.put(Integer.class.getName(), Integer.class.getName());
		typeMapping.put(Float.class.getName(), Float.class.getName());
		typeMapping.put(Double.class.getName(), Double.class.getName());
		typeMapping.put("String", String.class.getName());
		typeMapping.put("int", Integer.class.getName());
		typeMapping.put("float", Float.class.getName());
		typeMapping.put("double", Double.class.getName());
	}

	public static String getJavaType(Class<?> clazz) {
		String type = typeMapping.get(clazz.getName());
		return (type == null) ? clazz.getName() : type;
	}

	/*********************************************************************
	 *
	 * 
	 * Java type checker
	 * 
	 * 
	 *********************************************************************/

	/**
	 * primitive type in Java
	 */
	protected final static Set<String> primitive = new HashSet<String>();

	static {
		primitive.add(String.class.getName());
		primitive.add(Boolean.class.getName());
		primitive.add(Integer.class.getName());
		primitive.add(Long.class.getName());
		primitive.add(Double.class.getName());
		primitive.add(Float.class.getName());
		primitive.add(Byte.class.getName());
		primitive.add("boolean");
		primitive.add("int");
		primitive.add("long");
		primitive.add("double");
		primitive.add("float");
		primitive.add("byte");
	}

	/**
	 * @param typename typename
	 * @return return true if the classname is primitive, otherwise return false
	 */
	public static boolean isPrimitive(String typename) {
		return isNull(typename) ? false : primitive.contains(typename);
	}

	/**
	 * @param clazz class
	 * @return return true if the typename is primitive, otherwise return false
	 */
	public static boolean isPrimitive(Class<?> clazz) {
		return isNull(clazz) ? false : isPrimitive(clazz.getName());
	}

	/**
	 * @param typename typename
	 * @return return true if the typename is starts with java.util.Map, otherwise
	 *         return false
	 */
	public static boolean isMap(String typename) {
		return isNull(typename) ? false : typename.startsWith(Map.class.getName());
	}

	/**
	 * 
	 */
	protected final static Map<String, String> map = null;

	/**
	 * @param typename typename
	 * @return true if the typename is java.util.Map with (String, String) style,
	 *         otherwise return false
	 */
	public static boolean isStringStringMap(String typename) {
		try {
			return isNull(typename) ? false
					: typename.equals(JavaUtils.class.getDeclaredField("map").getGenericType().getTypeName());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param clazz class
	 * @return return true if the typename is starts with java.util.Map, otherwise
	 *         return false
	 */
	public static boolean isMap(Class<?> clazz) {
		return isNull(clazz) ? false : isMap(clazz.getName());
	}

	/**
	 * @param typename typename
	 * @return return true if the typename is Map, but not java.util.Map with
	 *         (String, String) style, otherwise return false
	 */
	public static boolean isStringObjectMap(String typename) {
		return isMap(typename) && !isStringStringMap(typename);
	}

	/**
	 * 
	 */
	protected final static List<String> list = null;

	/**
	 * @param typename typename
	 * @return return true if the typename is java.util.List with String style,
	 *         otherwise return false
	 */
	public static boolean isStringList(String typename) {
		try {
			return isNull(typename) ? false
					: typename.equals(JavaUtils.class.getDeclaredField("list").getGenericType().getTypeName());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param typename typename
	 * @return return true if the typename is starts with java.util.List, otherwise
	 *         return false
	 */
	public static boolean isList(String typename) {
		return isNull(typename) ? false : typename.startsWith(List.class.getName());
	}

	/**
	 * @param typename typename
	 * @return return true if the typename is starts with java.util.List, but not
	 *         java.util.List with String style, otherwise return false
	 */
	public static boolean isObjectList(String typename) {
		return isList(typename) && !isStringList(typename);
	}

	/**
	 * 
	 */
	protected final static Set<String> set = null;

	/**
	 * @param typename typename
	 * @return 是否是String类型的Set
	 */
	public static boolean isStringSet(String typename) {
		try {
			return isNull(typename) ? false
					: typename.equals(JavaUtils.class.getDeclaredField("set").getGenericType().getTypeName());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param typename 名字
	 * @return 是否是Set
	 */
	public static boolean isSet(String typename) {
		return isNull(typename) ? false : typename.startsWith(Set.class.getName());
	}

	/**
	 * @param typename 名字
	 * @return 是否是Object类型的Set
	 */
	public static boolean isObjectSet(String typename) {
		return isSet(typename) && !isStringSet(typename);
	}

	/**
	 * @param typename              typename
	 * @return Map中对象
	 */
	public static String getClassNameForMapStyle(String typename) {
		if (!isMap(typename)) {
			throw new InvalidParameterException("typename shoule be Map");
		}
		int start = typename.indexOf(",");
		int end = typename.indexOf(">");
		return (start == -1) ? typename : typename.substring(start + 1, end).trim(); // <String, Object>的,后有一个空格
	}

	/**
	 * @param typename 名字
	 * @return List中对象
	 */
	public static String getClassNameForListOrSetStyle(String typename) {
		if (!isList(typename) && !isSet(typename)) {
			throw new InvalidParameterException("typename shoule be Map");
		}
		int start = typename.indexOf("<");
		int end = typename.indexOf(">");
		return (start == -1) ? typename : typename.substring(start + 1, end).trim();
	}

	/*********************************************************************
	 * 
	 * 
	 *                         
	 * 
	 * 
	 *********************************************************************/

	protected final static Set<String> ignoreMethods = new HashSet<String>();

	static {
		ignoreMethods.add("getProtectionDomain");
		ignoreMethods.add("getModifiers");
		ignoreMethods.add("getSuperclass");
		ignoreMethods.add("getComponentType");
		ignoreMethods.add("getAnnotatedInterfaces");
		ignoreMethods.add("getAnnotatedSuperclass");
		ignoreMethods.add("getCanonicalName");
		ignoreMethods.add("getClassLoader");
		ignoreMethods.add("getClasses");
		ignoreMethods.add("getConstructors");
		ignoreMethods.add("getDeclaredAnnotations");
		ignoreMethods.add("getDeclaredClasses");
		ignoreMethods.add("getDeclaredConstructors");
		ignoreMethods.add("getDeclaredFields");
		ignoreMethods.add("getDeclaredMethods");
		ignoreMethods.add("getDeclaringClass");
		ignoreMethods.add("getEnclosingClass");
		ignoreMethods.add("getEnclosingConstructor");
		ignoreMethods.add("getEnclosingMethod");
		ignoreMethods.add("getEnumConstants");
		ignoreMethods.add("getFields");
		ignoreMethods.add("getGenericInterfaces");
		ignoreMethods.add("getGenericSuperclass");
		ignoreMethods.add("getInterfaces");
		ignoreMethods.add("getMethods");
		ignoreMethods.add("getPackage");
		ignoreMethods.add("getSigners");
		ignoreMethods.add("getSimpleName");
		ignoreMethods.add("getTypeName");
		ignoreMethods.add("getTypeParameters");
		ignoreMethods.add("getClass");
		ignoreMethods.add("getBytes");

		ignoreMethods.add("equals");
		ignoreMethods.add("wait");
		ignoreMethods.add("toString");
		ignoreMethods.add("hashCode");
		ignoreMethods.add("notify");
		ignoreMethods.add("notifyAll");
	}

	/**
	 * @param name 名字
	 * @return 是否过滤
	 */
	public static boolean ignoreMethod(String name) {
		return isNull(name) ? true : ignoreMethods.contains(name);
	}

	/**
	 * Check whether a string is null
	 * 
	 * @param str string
	 * @return return true of the string is null, otherwise return false
	 */
	public static boolean isNull(String str) {
		return (str == null || "".equals(str)) ? true : false;
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @param obj 对象
	 * @return 是否为空
	 */
	public static boolean isNull(Object obj) {
		return (obj == null) ? true : false;
	}
}
