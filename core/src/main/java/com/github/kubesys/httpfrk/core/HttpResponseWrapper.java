/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk.core;

/**
 * @author  wuheng
 * @since   2019.2.20
 * 
 * <p>
 * The {@code HttpResponse} class represents the return
 * value should be bound to the web response body.
 */
public interface HttpResponseWrapper {

	public static final String SUCCESS = "success";
	
	public static final String FAIL    = "fail";
	
	public String unwrap(String status, Object value) throws Exception;
}
