/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk.adapters;

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

import com.github.kubesys.httpfrk.utils.SwaggerUtil;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Swagger;
import springfox.documentation.schema.Example;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;



/**
 * @author  wuheng
 * @since   2021.1.20
 */
@Component
public class SwaggerAdapter {

	protected final static Map<String, List<String>> mapper = new HashMap<>();
	
	/**
	 * response
	 */
	protected final static Set<ResponseMessage> response =  new HashSet<>();
	
	/**
	 * example
	 */
	protected final static Multimap<String, Example> examples = LinkedHashMultimap.create();
	
	/**
	 * tags
	 */
	protected final Set<String> tags = new HashSet<>();
	

	static {
		
		ModelReference responseModel = new ModelRef("object");
		@SuppressWarnings("rawtypes")
		ResponseMessage msg = new ResponseMessage(200, 
								"code 20000, 50000 mean a right or wrong response, respectively", 
								responseModel , 
								new HashMap<>(), 
								new ArrayList<VendorExtension> ());
		response.add(msg);
		
		
	}

	/**********************************************************
	 * 
	 * toSwagger
	 * 
	 *********************************************************/
	public void wrapperSwagger(ApplicationContext ctx, String serviceModule, Method service) throws Exception {
		
		ServiceModelToSwagger2Mapper mapper= (ServiceModelToSwagger2Mapper) 
										ctx.getBean("serviceModelToSwagger2MapperImpl");
		DocumentationCache cache = (DocumentationCache) ctx.getBean("resourceGroupCache");
		
		Swagger swagger = mapper.mapDocumentation(cache.documentationByGroup("default"));
		
		Documentation documentation = cache.documentationByGroup("default");
		
		Multimap<String, ApiListing> apiMap =  documentation.getApiListings();
		
		ApiOperation apiOpt = service.getDeclaredAnnotation(ApiOperation.class);
		
		String path = "/" + serviceModule + "/" + service.getName();
		
		List<ApiDescription> apis = new ArrayList<>();
		apis.add(new ApiDescription("default", path, apiOpt.value(), 
					SwaggerUtil.toOperation(
							serviceModule + "." + service.getName(), 
							HttpMethod.GET, apiOpt.value(), tags, 
							SwaggerUtil.toApiParam(service), response), 
					false));
		
		apiMap.put(path, SwaggerUtil.toApiListing(
						serviceModule, swagger, apis, 
						getClass().getDeclaredAnnotation(Api.class)));
		
	}

}
