/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk.responses;

import com.github.kubesys.httpfrk.core.HttpResponseWrapper;
import com.github.kubesys.httpfrk.utils.StringUtils;

/**
 * @author  wuheng
 * @since   2019.2.20
 * 
 * <p>
 * The {@code HttpResponse} class represents the return
 * value should be bound to the web response body.
 */
public class DefaultHttpResponseWrapper implements HttpResponseWrapper {

	@Override
	public String unwrap(String status, Object value) throws Exception {
		HttpResponse response = "fail".equals(status) ?
				new HttpResponse(50000, value) : new HttpResponse(20000, value);
		return StringUtils.toJSONString(response);
	}

	public static class HttpResponse {
		/**
		 * neither Success or Failure
		 */
		protected int code;
		
		/**
		 * it represents the exception information,
		 * otherwise it should be null
		 */
		protected String message;
		
		/**
		 * if it is not an exception, the response
		 * is the object.
		 */
		protected Object data;
		
		public HttpResponse() {
		}
		
		public HttpResponse(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public HttpResponse(int code, Object data) {
			this.code = code;
			this.data = data;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}
	}

}
