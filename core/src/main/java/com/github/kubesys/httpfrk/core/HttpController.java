/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kubesys.httpfrk.tools.Jsr303Tool;
import com.github.kubesys.httpfrk.tools.Jsr303Tool.ValidationResult;
import com.github.kubesys.httpfrk.utils.JavaUtils;
import com.github.kubesys.httpfrk.utils.StringUtils;

import io.swagger.annotations.ApiParam;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2020.2.9
 * 
 *        The {@code HttpController} class is used to dispatch request to the
 *        related handler, if the handler is not found, it would throw an
 *        exception.
 */
@RestController
@ComponentScan
public class HttpController implements ApplicationContextAware {

	/**
	 * logger
	 */
	public static final Logger m_logger = Logger.getLogger(HttpController.class.getName());

	/**
	 * handler means how to deal with the request for specified servletPath
	 */
	@Autowired
	protected HandlerManager handlers;

	@Autowired
	protected Jsr303Tool jsr303;
	
	/**
	 * app context
	 */
	protected static ApplicationContext ctx;

	public HttpController() {
		super();
	}
	
	/**************************************************
	 * 
	 * Request Mapping
	 * 
	 **************************************************/

	
	/**
	 * @param request servlet path should be startwith 'add', 'create', or 'new'
	 * @param body    just body
	 * @return the {@code HttpBodyHandler} result. In fact, it may be an exception.
	 * @throws Exception it can be any exception that {@code HttpBodyHandler} throws
	 */
	@RequestMapping(method = RequestMethod.POST, value = { "/**/login*", "/**/logout*", "/**/add*", "/**/create*", "/**/new*",
			"/**/insert*", "/**/clone*", "/**/attach*", "/**/plug*", "/**/set*", "/**/bind*", "/**/solve*" })
	public @ResponseBody String createTypeRequest(HttpServletRequest request, @RequestBody JsonNode body)
			throws Exception {
		return doResponse(getServletPath(request), body);
	}

	/**
	 * @param request servlet path should be startwith 'delete', or 'remove'
	 * @param body    just body
	 * @return the {@code HttpBodyHandler} result. In fact, it may be an exception.
	 * @throws Exception it can be any exception that {@code HttpBodyHandler} throws
	 */
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.DELETE }, value = { "/**/delete*", "/**/remove*",
			"/**/eject*", "/**/detach*", "/**/unplug*", "/**/unset*", "/**/unbind*" })
	public @ResponseBody String deleteTypeRequest(HttpServletRequest request, @RequestBody JsonNode body)
			throws Exception {
		return doResponse(getServletPath(request), body);
	}

	/**
	 * @param request servlet path should be startwith 'update', 'modify', or
	 *                'replace'
	 * @param body    just body
	 * @return the {@code HttpBodyHandler} result. In fact, it may be an exception.
	 * @throws Exception it can be any exception that {@code HttpBodyHandler} throws
	 */
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, value = { "/**/update*", "/**/diff*", "/**/modify*",
			"/**/replace*", "/**/change*", "/**/resize*", "/**/tune*", "/**/revert*", "/**/convert*" })
	public @ResponseBody String updateTypeRequest(HttpServletRequest request, @RequestBody JsonNode body)
			throws Exception {
		return doResponse(getServletPath(request), body);
	}

	/**
	 * @param request servlet path should be startwith 'get', 'list', or 'describe'
	 * @param body    just body
	 * @return the {@code HttpBodyHandler} result. In fact, it may be an exception.
	 * @throws Exception it can be any exception that {@code HttpBodyHandler} throws
	 */
	@RequestMapping(method = { RequestMethod.POST }, value = { "/**/mock*", "/**/user*",
			"/**/get*", "/**/list*", "/**/query*", "/**/describe*", "/**/retrieve*", "/**/echo*", "/**/exec*" })
	public @ResponseBody String retrievePostTypeRequest(HttpServletRequest request, @RequestBody JsonNode body)
			throws Exception {
		return doResponse(getServletPath(request), body);
	}

	/**
	 * @param request servlet path should be startwith 'get', 'list', or 'describe'
	 * @param body    just body
	 * @return the {@code HttpBodyHandler} result. In fact, it may be an exception.
	 * @throws Exception it can be any exception that {@code HttpBodyHandler} throws
	 */
	@RequestMapping(method = { RequestMethod.GET }, value = {  "/**/mock*", "/**/user*",
			"/**/get*", "/**/list*", "/**/query*", "/**/describe*", "/**/retrieve*", "/**/echo*", "/**/exec*" })
	public @ResponseBody String retrieveTypeGetRequest(HttpServletRequest request,
			@RequestParam(required = false) Map<String, String> body) throws Exception {
		return doResponse(getServletPath(request), StringUtils.toJsonNode(body));
	}
	
	/**
	 * just for faas
	 * 
	 * @throws Exception
	 */
	@RequestMapping(method = { RequestMethod.GET }, value = {  "/exit" })
	public void exit() throws Exception {
		System.exit(0);
	}
	/**
	 * @param request servlet path should be startwith 'get', 'list', or 'describe'
	 * @return the {@code HttpBodyHandler} result. In fact, it may be an exception.
	 * @throws Exception it can be any exception that {@code HttpBodyHandler} throws
	 */
	@ResponseBody
	public String invalidRequest(HttpServletRequest request) throws Exception {
		m_logger.severe("Fail to deal with " + request.getServletPath() + " the reason is: "
				+ HttpConstants.EXCEPTION_INVALID_REQUEST_URL);
		return ((HttpResponseWrapper) ctx.getBean("wrapper"))
				.unwrap("fail", HttpConstants.EXCEPTION_INVALID_REQUEST_URL);
	}

	/**
	 * @param request servlet path should be startwith 'get', 'list', or 'describe'
	 * @return the {@code HttpBodyHandler} result. In fact, it may be an exception.
	 */
	protected String getServletPath(HttpServletRequest request) {
		return request.getRequestURI().substring(request.getContextPath().length() + 1);
	}

	/**************************************************
	 * 
	 * Response encapsulation
	 * 
	 **************************************************/

	/**
	 * @param servletPath                   path
	 * @param body                          body
	 * @return                              resp
	 * @throws Exception                    exception
	 */
	protected String doResponse(String servletPath, JsonNode body) throws Exception {

		m_logger.info("Begin to deal with " + servletPath);

		Method hanlder = handlers.geHandler(servletPath);
		try {

			Object[] params = getParameters(body, hanlder);
			Object result = (params != null) ? hanlder.invoke(getInstance(servletPath), params)
					: hanlder.invoke(getInstance(servletPath));

			m_logger.info("Successfully deal with " + servletPath);
			return ((HttpResponseWrapper) ctx.getBean("wrapper"))
							.unwrap("success", result);
		} catch (Exception ex) {
			StringBuffer sb = new StringBuffer();
			if (ex instanceof InvocationTargetException) {
				sb.append(((InvocationTargetException) ex).getTargetException());
			} else {
				sb.append(ex.getMessage()).append("\n");
				for (StackTraceElement ste : ex.getStackTrace()) {
					sb.append("\t").append(ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber()).append("\n");
				}
			}
			throw new Exception(sb.toString());
		}

	}

	/**
	 * @param request                         request
	 * @param e                               exception
	 * @return                                resp
	 * @throws Exception                      exception
	 */
	@ExceptionHandler
	@ResponseBody
	public String invalidResponse(HttpServletRequest request, Exception e) throws Exception {
		
		m_logger.severe(
				"Fail to deal with " + request.getServletPath() + ", the reason is: " + String.valueOf(e.getMessage()));
		return ((HttpResponseWrapper) ctx.getBean("wrapper"))
				.unwrap("fail", String.valueOf(e.getMessage()));
	}

	/**
	 * @param body                             body
	 * @param targetMethod                     target
	 * @return                                 objects 
	 * @throws Exception                       exception
	 */
	protected Object[] getParameters(JsonNode body, Method targetMethod) throws Exception {

		int parameterCount = targetMethod.getParameterCount();
		Object[] params = (parameterCount == 0) ? null : new Object[parameterCount];
		for (int i = 0; i < parameterCount; i++) {
			String name = targetMethod.getParameters()[i].getName();
			if (!body.has(name)) {
				String typeName = targetMethod.getParameters()[i].getType().getName();
				if (JavaUtils.isPrimitive(typeName) && !typeName.equals(String.class.getName())) {
					params[i] = 0;
				} else {
					params[i] = null;
				}
			} else {
				params[i] = new ObjectMapper().readValue(body.get(name).toPrettyString(),
					targetMethod.getParameterTypes()[i]);
			}
			
			// jsr303
			ApiParam pd = targetMethod.getParameters()[i].getAnnotation(ApiParam.class);
			if (pd != null && pd.required() && targetMethod.getParameters()[i]
										.getAnnotation(Valid.class) != null) {
				checkParameter(params, i);
			}
		}
		return params;

	}

	/**
	 * @param params                              params
	 * @param i                                   i
	 * @throws Exception                          exception
	 */
	protected void checkParameter(Object[] params, int i) throws Exception {
		ValidationResult result = jsr303.validateEntity(params[i]);
		if (result.isHasErrors()) {
			throw new Exception(new ObjectMapper().writeValueAsString(result.getErrorMsg()));
		}
	}

	/**
	 * @param servletPath                         path
	 * @return                                    obj
	 * @throws Exception                          exception
	 */
	protected Object getInstance(String servletPath) throws Exception {
		String name = handlers.geHandler(servletPath).getDeclaringClass().getSimpleName();
		return ctx.getBean(name.substring(0, 1).toLowerCase() + name.substring(1));
	}

	/**************************************************
	 * 
	 * Framework setting
	 * 
	 **************************************************/

	/**
	 *
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (ctx == null) {
			ctx = applicationContext;
		}
	}
	
	public static class CorsInterceptor implements HandlerInterceptor {

		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {
			String origin = request.getHeader("Origin");
			response.setHeader("Access-Control-Allow-Origin", origin);
			response.setHeader("Access-Control-Allow-Headers",
					"Origin, X-Requested-With, Content-Type, Accept, "
							+ "WG-App-Version, WG-Device-Id, WG-Network-Type, WG-Vendor, WG-OS-Type, "
							+ "WG-OS-Version, WG-Device-Model, WG-CPU, WG-Sid, WG-App-Id, WG-Token, X-token");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setContentType("application/json;charset=UTF-8");
			m_logger.info("Target URL:" + request.getRequestURI());
			return true;
		}

		@Override
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
				ModelAndView modelAndView) throws Exception {
			if (response.getStatus() > 400) {
				response.setStatus(200);
				response.setContentType("application/json;charset=utf-8");
				m_logger.info("Fail to deal with " + request.getRequestURL() + ", the reason is: "
									+ "Invalid servlet path, or invalid parameters was requested");
				try {
					response.getWriter().flush();
					response.getWriter().write(((HttpResponseWrapper)
							ctx.getBean("wrapper")).unwrap("fail", "Invalid servlet path, or invalid parameters was requested"));
				} catch (Exception ex) {
					
				}
			}
			HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
		}
		
	}

}
