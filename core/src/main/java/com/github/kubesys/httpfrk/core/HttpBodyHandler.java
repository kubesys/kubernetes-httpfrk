/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpMethod;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeBindings;
import com.fasterxml.classmate.types.ResolvedObjectType;
import com.fasterxml.classmate.types.ResolvedPrimitiveType;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.Swagger;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

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
	 * messages
	 */
	protected final static Set<ResponseMessage> messages =  new HashSet<>();
	
	
	/**
	 * mapper
	 */
	protected final static Map<String, ResolvedType> mapper = new HashMap<>();
	
	static {
		mapper.put("boolean", ResolvedPrimitiveType.all().get(0));
		mapper.put("byte", ResolvedPrimitiveType.all().get(1));
		mapper.put("short", ResolvedPrimitiveType.all().get(2));
		mapper.put("char", ResolvedPrimitiveType.all().get(3));
		mapper.put("int", ResolvedPrimitiveType.all().get(4));
		mapper.put("long", ResolvedPrimitiveType.all().get(5));
		mapper.put("float", ResolvedPrimitiveType.all().get(6));
		mapper.put("double", ResolvedPrimitiveType.all().get(7));
		mapper.put("Java.lang.String", ResolvedObjectType.create(JsonNode.class, TypeBindings.emptyBindings(), null, null));
		mapper.put("json", ResolvedObjectType.create(JsonNode.class, TypeBindings.emptyBindings(), null, null));
	}
	
	/**
	 * produces
	 */
	protected final static Set<String> produces = new HashSet<>();
	
	/**
	 * operations
	 */
	protected final List<springfox.documentation.service.Operation> operations = new ArrayList<>();
	
	/**
	 * tags
	 */
	protected final Set<String> swaggerTags = new HashSet<>();
	
	
	/**
	 * tags
	 */
	protected final Set<Tag> tags = new HashSet<>();
	
	/**
	 * registered
	 */
	protected final Set<String> registered = new HashSet<String>();
	
	
	/**
	 * All handers
	 */
	@Autowired
	protected HandlerManager configure;
	
	/**
	 * app context
	 */
	protected static ApplicationContext ctx;
	
	static {
		ModelReference responseModel = new ModelRef("json");
		@SuppressWarnings("rawtypes")
		ResponseMessage msg = new ResponseMessage(200, 
								"code 20000, 50000 mean a right or wrong response, respectively", 
								responseModel , 
								new HashMap<>(), 
								new ArrayList<VendorExtension> ());
		messages.add(msg);
		
		produces.add("*/*");
	}

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
		swaggerTags.add(serviceModule);
		
		
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
				wrapperSwagger(serviceModule, service);
				
			
			} else {
				m_logger.severe(HttpConstants.EXCEPTION_UNABLE_TO_REGISTER_SERVICE_WITH_UNKNOWN_REASON);
				throw new Exception(HttpConstants.EXCEPTION_UNABLE_TO_REGISTER_SERVICE_WITH_UNKNOWN_REASON);
			}
			
			
		}
	}

	void wrapperSwagger(String serviceModule, Method service) throws Exception {
		
		ServiceModelToSwagger2Mapper mapper= (ServiceModelToSwagger2Mapper) 
										ctx.getBean("serviceModelToSwagger2MapperImpl");
		DocumentationCache cache = (DocumentationCache) ctx.getBean("resourceGroupCache");
		Documentation documentation = cache.documentationByGroup("default");
		Swagger swagger = mapper.mapDocumentation(cache.documentationByGroup("default"));
		
		
		Multimap<String, ApiListing> apiMap =  documentation.getApiListings();
		ApiOperation apiOpt = fromApiOperation(serviceModule, service);
		
		List<ApiDescription> apis = new ArrayList<>();
		apis.add(new ApiDescription("default", "/" + serviceModule + "/" + service.getName(), 
					apiOpt != null ? apiOpt.value() : "", operations, false));
		
		Api apiDesc = getClass().getDeclaredAnnotation(Api.class);
		
		
		ApiListing apiListing =  new ApiListingBuilder(new Ordering<ApiDescription>( ) {
		
			@Override
			public int compare(ApiDescription left, ApiDescription right) {
				return left.hashCode() - right.hashCode();
			}
			
		}).basePath(swagger.getBasePath()).apiVersion(swagger.getInfo().getVersion())
				.resourcePath("/" + serviceModule).apis(apis)
				.produces(produces).tagNames(swaggerTags)
				.description(apiDesc != null ? apiDesc.value() : "")
				.build();
		
		apiMap.put("/" + serviceModule + "/" + service.getName(), apiListing);
		
	}

	@SuppressWarnings("rawtypes")
	ApiOperation fromApiOperation(String serviceModule, Method service) {
		ApiOperation apiOpt = service.getDeclaredAnnotation(ApiOperation.class);
		
		springfox.documentation.service.Operation request = new OperationBuilder(new CachingOperationNameGenerator())
					.authorizations(new ArrayList<SecurityReference>())
					.uniqueId(serviceModule + "." + service.getName())
					.extensions(new ArrayList<VendorExtension>())
					.responseMessages(messages).parameters(fromApiParam(service))
					.responseModel( new ModelRef("json")).tags(swaggerTags)
					.summary(apiOpt != null ? apiOpt.value() : "")
					.method(service.getParameterCount() == 0 ? HttpMethod.GET : HttpMethod.POST).build();
		operations.add(request);
		return apiOpt;
	}

	@SuppressWarnings("rawtypes")
	List<springfox.documentation.service.Parameter> fromApiParam(Method service) {
		List<springfox.documentation.service.Parameter> parameters = new ArrayList<>();
		
		for (java.lang.reflect.Parameter param : service.getParameters()) {
			
			ApiParam apiParam = param.getAnnotation(ApiParam.class);
			
			if (apiParam != null) {
				String typeName = param.getParameterizedType().getTypeName();
				Optional<ResolvedType> of = mapper.get(typeName) != null ? Optional.of(mapper.get(typeName)) : Optional.of(mapper.get("json"));
				springfox.documentation.service.Parameter swaggerParam = new springfox.documentation.service.Parameter(
						param.getName(), apiParam.value(), apiParam.defaultValue(), apiParam.required(), 
						apiParam.allowMultiple(), apiParam.allowEmptyValue(), new ModelRef("json"), of, 
						null, param.getType().getSimpleName(), 
						apiParam.access(), apiParam.hidden(), null, apiParam.collectionFormat(), 0, null, 
						LinkedHashMultimap.create(), new ArrayList<VendorExtension>());
				parameters.add(swaggerParam);
			}
		}
		return parameters;
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
