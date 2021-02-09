package com.github.webfrk.examples;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.kubesys.httpfrk.core.HttpBodyHandler;
import com.github.kubesys.tools.annotations.ServiceDefinition;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "这个有啥用")
@RequestMapping("/app")
@ServiceDefinition
public class SwaggerService extends HttpBodyHandler {
	
	@ApiOperation(value = "desc of method", notes = "")
	@RequestMapping(value = "echoHello", method = RequestMethod.GET)
	public Object echoHello( /* 参数注解 */ @ApiParam(value = "desc of param", required = true) @RequestParam String name) {
		return "Hello " + name + "!";
	}
	
	@ApiOperation(value = "desc of method", notes = "")
	@RequestMapping(value = "echoHello1", method = RequestMethod.GET)
	public Object echoHello1( /* 参数注解 */
			@ApiParam(value = "desc of name", required = true) @RequestParam String name,
			@ApiParam(value = "desc of habit", required = true) @RequestParam String habit) {
		return "Hello " + name + "!";
	}
	
	@ApiOperation(value = "desc of method", notes = "")
	@RequestMapping(value = "echoHello2", method = RequestMethod.GET)
	public Object echoHello2() {
		return "Hello default !";
	}
}