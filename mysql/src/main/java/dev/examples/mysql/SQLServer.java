/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package dev.examples.mysql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import com.github.kubesys.httpfrk.HttpServer;


/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @author xuyuanjia2017@otcaix.iscas.ac.cn
 * @since 2019.11.16
 * 
 *        <p>
 *        The {@code ApplicationServer} class is used for starting web
 *        applications. Please configure
 * 
 *        src/main/resources/application.yml
 *        src/main/resources/log4j.properties
 * 
 */
@ComponentScan(basePackages = { "com.github.kubesys.httpfrk", "dev.examples.mysql"})
@EntityScan(basePackages = {"dev.examples.mysql"})
@MapperScan(basePackages = {"dev.examples.mysql"})
public class SQLServer extends HttpServer  {

	/**
	 * program entry point
	 * 
	 * @param args default is null
	 */
	public static void main(String[] args) {
		SpringApplication.run(SQLServer.class, args);
	}

	@Override
	public String getTitle() {
		return "SQL db";
	}

	@Override
	public String getDesc() {
		return "This is a mysql demo";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public String getPackage() {
		return "dev.examples.mysql";
	}

}
