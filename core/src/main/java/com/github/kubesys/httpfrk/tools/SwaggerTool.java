/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk.tools;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

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
 * @author  wuheng
 * @since   2021.1.20
 */
@Component
public class SwaggerTool {

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
	
	public void addTag(String tag) {
		swaggerTags.add(tag);
	}
	
	public void wrapperSwagger(ApplicationContext ctx, String serviceModule, Method service) throws Exception {
		
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
}
