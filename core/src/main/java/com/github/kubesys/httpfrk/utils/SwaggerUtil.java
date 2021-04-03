/**
 * Copyright (2018, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.models.Swagger;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.schema.Example;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
/**
 * 
 * @author wuheng
 * @since 2019.4.10
 */
public class SwaggerUtil{

	/***********************************************************
	 * 
	 * ModelRef
	 *
	 ************************************************************/
	
	/**
	 * mapper
	 */
	protected final static Map<String, ModelRef> swaggerMapper = new HashMap<>();
	
	static {
		swaggerMapper.put("boolean", new ModelRef("boolean"));
		swaggerMapper.put("byte", new ModelRef("byte"));
		swaggerMapper.put("short", new ModelRef("short"));
		swaggerMapper.put("int", new ModelRef("int"));
		swaggerMapper.put("long", new ModelRef("long"));
		swaggerMapper.put("float", new ModelRef("float"));
		swaggerMapper.put("double", new ModelRef("double"));
		swaggerMapper.put("int", new ModelRef("int"));
		swaggerMapper.put("long", new ModelRef("long"));
		swaggerMapper.put("float", new ModelRef("float"));
		swaggerMapper.put("double", new ModelRef("double"));
		swaggerMapper.put(String.class.getName(), new ModelRef("string"));
		swaggerMapper.put(Boolean.class.getName(), new ModelRef("boolean"));
		swaggerMapper.put(Byte.class.getName(), new ModelRef("byte"));
		swaggerMapper.put(Short.class.getName(), new ModelRef("short"));
		swaggerMapper.put(Integer.class.getName(), new ModelRef("string"));
		swaggerMapper.put(Long.class.getName(), new ModelRef("boolean"));
		swaggerMapper.put(Float.class.getName(), new ModelRef("byte"));
		swaggerMapper.put(Double.class.getName(), new ModelRef("short"));
	}
	
	public static ModelRef toModelRef(String type) {
		ModelRef ref = swaggerMapper.get(type);
		return ref == null ? new ModelRef("object") : ref;
	}
	
	/***********************************************************
	 * 
	 * Api
	 *
	 ************************************************************/
	@SuppressWarnings("rawtypes")
	public static List<Parameter> toApiParam(Method service, String queryOrBody,  ObjectNode json) {
		
		List<Parameter> parameters = new ArrayList<>();
		
		for (java.lang.reflect.Parameter param : service.getParameters()) {
			
			ApiParam apiParam = param.getAnnotation(ApiParam.class);
			
			if (apiParam != null) {
				String type = param.getParameterizedType().getTypeName();
				Parameter parameter = new Parameter(
						param.getName(), 
						queryOrBody.equals("query") ? apiParam.value() 
								: apiParam.value() + "\n请求格式为:\n" + (json != null ? json.toPrettyString() : ""), 
						apiParam.defaultValue(), 
						apiParam.required(), 
						apiParam.allowMultiple(), apiParam.allowEmptyValue(), 
						toModelRef(type), null, null, 
						queryOrBody, apiParam.access(), 
						apiParam.hidden(), null, 
						apiParam.collectionFormat(), 
						0, json != null ? json : "", toExamples(json), 
						new ArrayList<VendorExtension>());
				parameters.add(parameter);
			}
		}
		return parameters;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Operation> toOperation(
			String id, HttpMethod http, String summary, Set<String> swaggerTags, 
			List<Parameter> params, Set<ResponseMessage> messages) {
		
		List<Operation> opts = new ArrayList<>();
		Operation opt = new OperationBuilder(new CachingOperationNameGenerator())
					.authorizations(new ArrayList<SecurityReference>())
					.uniqueId(id)
					.extensions(new ArrayList<VendorExtension>())
					.responseMessages(messages).parameters(params)
					.responseModel( new ModelRef("object")).tags(swaggerTags)
					.summary(summary)
					.method(http).build();
		opts.add(opt);
		return opts;
	}

	public static ApiListing toApiListing(String serviceModule, Swagger swagger, List<ApiDescription> apis, Api apiDesc) {
		ApiListing apiListing =  new ApiListingBuilder(new Ordering<ApiDescription>( ) {
		
			@Override
			public int compare(ApiDescription left, ApiDescription right) {
				return left.hashCode() - right.hashCode();
			}
			
		}).basePath(swagger.getBasePath()).apiVersion(swagger.getInfo().getVersion())
				.resourcePath("/" + serviceModule).apis(apis)
				.produces(toProduces())
				.description(apiDesc != null ? apiDesc.value() : "")
				.build();
		return apiListing;
	}
	
	public static Multimap<String, Example> toExamples(ObjectNode json) {
		Multimap<String, Example> examples = LinkedHashMultimap.create();
		if (json != null) {
			examples.put("default", new Example("application/json", json));
		}
		return examples;
	}
	
	public static Set<String> toProduces() {
		Set<String> produces = new HashSet<>();
		produces.add("*/*");
		return produces;
	}
}
