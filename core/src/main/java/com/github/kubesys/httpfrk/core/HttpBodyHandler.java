/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.github.kubesys.httpfrk.adapters.SwaggerAdapter;

/**
 * @author wuheng
 * @since 2019.2.20
 */
public abstract class HttpBodyHandler implements CommandLineRunner, ApplicationContextAware {

	/**
	 * logger
	 */
	public final static Logger m_logger = Logger.getLogger(HttpController.class.getName());

	
	/**
	 * All handers
	 */
	@Autowired
	protected HandlerManager configure;
	
	/**
	 * app context
	 */
	protected static ApplicationContext ctx;
	
	@Autowired
	protected SwaggerAdapter swaggerTool;
	
	/**
	 * registered
	 */
	protected final Set<String> registered = new HashSet<String>();
	
	/**
	 * 
	 */
	public HttpBodyHandler() {
		super();
	}

	/**********************************************************
	 * 
	 * 
	 * 
	 **********************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextAware#setApplicationContext(org
	 * .springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (ctx == null) {
			ctx = applicationContext;
		}
		
	}

	@Override
	public void run(String... args) throws Exception {

		// get service module based on classname
		String classname = getClass().getSimpleName();

		// cannot convert inner classname to service module
		if (classname.indexOf("/") != -1) {
			m_logger.severe(HttpConstants.EXCEPTION_UNABLE_TO_REGISTER_SERVICE_FROM_INNER_CLASS + classname);
			throw new Exception(HttpConstants.EXCEPTION_UNABLE_TO_REGISTER_SERVICE_FROM_INNER_CLASS + classname);
		}

		// service module name must be ends with 'Service'
		if (!classname.endsWith(HttpConstants.POSTFIX_SERVICE)) {
			m_logger.severe(HttpConstants.EXCEPTION_UNABLE_TO_REGISTER_SERVICE_WITH_WRONG_NAME + classname);
			throw new Exception(HttpConstants.EXCEPTION_UNABLE_TO_REGISTER_SERVICE_WITH_WRONG_NAME + classname);
		}

		// register services in the specified service module
		registerService(classname);
	}

	private void registerService(String classname) throws Exception {

		// In our design, the method name with some rules is also the service name,
		// It's impossible to exist duplicated services so and we unsupport polymorphism
		
		String serviceModule = getServiceModule(classname);
		
		
		for (Method service : getClass().getDeclaredMethods()) {

			if (service.getModifiers() != Modifier.PUBLIC) {
				continue;
			}

			// if exist duplicated services
			if (registered.contains(service.getName())) {
				m_logger.severe(HttpConstants.EXCEPTION_UNABLE_TO_REGISTER_SERVICE_WITH_POLYMORPHISM + classname
						+ "." + service.getName());
				throw new Exception(HttpConstants.EXCEPTION_UNABLE_TO_REGISTER_SERVICE_WITH_POLYMORPHISM + classname
						+ "." + service.getName());
			}
			
			// The rules (a method is a service) include
			// 1. it is a public method
			
			if (!Modifier.isPublic(service.getModifiers())) {
				continue;
			}
			
								
			// 2. register to <code>HandlerManager.addHandler<code> successful
			if (configure.addHandler(serviceModule, service)) {
				m_logger.info("servelet path '" + serviceModule 
						+ "/" + service.getName() + "' registered sucessful.");
				registered.add(service.getName());
				
				// 3. swagger support
				swaggerTool.wrapperSwagger(ctx, serviceModule, service);
				
			
			} else {
				m_logger.severe(HttpConstants.EXCEPTION_UNABLE_TO_REGISTER_SERVICE_WITH_UNKNOWN_REASON);
				throw new Exception(HttpConstants.EXCEPTION_UNABLE_TO_REGISTER_SERVICE_WITH_UNKNOWN_REASON);
			}
			
			
		}
	}

	

	/**
	 * @param name object name
	 * @return lowercase
	 */
	private static String getServiceModule(String classname) {
		String name = classname.substring(0, classname.length() 
				- HttpConstants.POSTFIX_SERVICE.length());
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}
	

}
