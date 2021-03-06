/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
@ComponentScan(basePackages = {
		"com.github.kubesys.httpfrk",
		"com.github.kubesys.examples" })
@Configuration
@SpringBootApplication
@EnableAutoConfiguration
@EnableSwagger2
public class DemoHttpServer extends HttpServer  {

	public static void main(String[] args) {
		SpringApplication.run(DemoHttpServer.class, args);
	}

	@Override
	public String getTitle() {
		return "Swagger Test";
	}

	@Override
	public String getDesc() {
		return "Using swagger instead of self-defined software.";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getPackage() {
		return "com.github.webfrk.examples";
	}

}
