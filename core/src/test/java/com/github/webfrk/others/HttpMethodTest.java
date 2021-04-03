/**
 * Copyright (2018, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.webfrk.others;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.kubesys.httpfrk.core.HttpController;

/**
 * 
 * @author wuheng
 * @since 2021.4.10
 */
public class HttpMethodTest {
	
	static Map<String, Set<HttpMethod>> httpMapper = new HashMap<>();

	public static void main(String[] args) {
		for (Method m : HttpController.class.getMethods()) {
			RequestMapping annotation = m.getAnnotation(RequestMapping.class);
			if (annotation != null && annotation.value() != null) {
				for (String str : annotation.value()) {
					int idx = str.lastIndexOf("/");
					String prefix = str.substring(idx + 1, idx + 4);
					for (RequestMethod req : annotation.method()) {
						Set<HttpMethod> list = httpMapper.get(prefix);
						list = (list == null) ? new HashSet<>() : list;
						if (req.name().equals("GET")) {
							list.add(HttpMethod.GET);
						} else if (req.name().equals("PUT")) {
							list.add(HttpMethod.PUT);
						} else if (req.name().equals("DELETE")) {
							list.add(HttpMethod.DELETE);
						} else {
							list.add(HttpMethod.POST);
						}
						httpMapper.put(prefix, list);
					}
				}
			}
		}
		
		System.out.println(httpMapper);
	}
	
}
