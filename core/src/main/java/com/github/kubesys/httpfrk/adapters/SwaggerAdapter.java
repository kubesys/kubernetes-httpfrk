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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kubesys.httpfrk.core.HttpController;
import com.github.kubesys.httpfrk.utils.JavaUtil;
import com.github.kubesys.httpfrk.utils.SwaggerUtil;
import com.google.common.collect.Multimap;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Swagger;
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

	static Map<String, Set<HttpMethod>> httpMapper = new HashMap<>();
	
	/**
	 * response
	 */
	protected final static Set<ResponseMessage> response =  new HashSet<>();
	
	/**
	 * tags
	 */
	protected final Set<String> tags = new HashSet<>();
	

	static {
		
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
		
		ModelReference responseModel = new ModelRef("object");
		@SuppressWarnings("rawtypes")
		ResponseMessage msg = new ResponseMessage(20000, 
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
		
		String shortName = service.getName().substring(0, 3);
		
		Set<HttpMethod> httpSet = httpMapper.get(shortName);
		
		ObjectNode json = new ObjectMapper().createObjectNode();
		
		HttpMethod http = httpSet.contains(HttpMethod.GET) ? HttpMethod.GET : HttpMethod.POST;
		
		for (java.lang.reflect.Parameter param : service.getParameters()) {
			if (JavaUtil.isPrimitive(param.getType())) {
				json.put(param.getName(), param.getType().getName());
			} else {
				http = HttpMethod.POST;
				json.put(param.getName(), new ObjectMapper().writeValueAsString(param.getType().newInstance()));
			}
		}
		
		apis.add(new ApiDescription(http.name(), path, apiOpt.value(), 
					SwaggerUtil.toOperation(
							serviceModule + "." + service.getName(), 
							http, apiOpt.value(), tags, 
							SwaggerUtil.toApiParam(service, 
									(http == HttpMethod.GET) ? "query" : "body" , 
									(http == HttpMethod.GET) ? null : json), response), 
					false));
		
		apiMap.put(path, SwaggerUtil.toApiListing(
						serviceModule, swagger, apis, 
						getClass().getDeclaredAnnotation(Api.class)));
		
	}
	
}
