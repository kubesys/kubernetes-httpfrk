/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.kubesys.httpfrk.core.HttpController.CorsInterceptor;
import com.github.kubesys.httpfrk.core.HttpResponseWrapper;
import com.github.kubesys.httpfrk.responses.DefaultHttpResponseWrapper;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @author xuyuanjia2017@otcaix.iscas.ac.cn
 * @since 2019.11.16
 * 
 *        <p>
 *        The {@code ApplicationServer} class is used for starting web
 *        applications. Please configure
 * 
 *        src/main/resources/application.yml src/main/resources/log4j.properties
 * 
 */

@Configuration
@EnableSwagger2
@SpringBootApplication
@EnableAutoConfiguration
public abstract class HttpServer implements WebMvcConfigurer {

	/**
	 * m_logger
	 */
	public static final Logger m_logger = Logger.getLogger(HttpServer.class.getName());

	/**
	 * program entry point
	 * 
	 * @param args default is null
	 */
	public static void main(String[] args) {
		SpringApplication.run(HttpServer.class, args);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CorsInterceptor()).addPathPatterns("/**");
	}

	@Bean(name = "wrapper")
	public HttpResponseWrapper getResponseWrapper() {
		return new DefaultHttpResponseWrapper();
	}

	/*************************************************************************
	 * 
	 * Swagger2
	 * 
	 *************************************************************************/

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage(getPackage())).paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(getTitle()).description(getDesc()).version(getVersion()).build();
	}

	public abstract String getTitle();

	public abstract String getDesc();

	public abstract String getVersion();

	public abstract String getPackage();

}
