package com.github.webfrk.examples;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.kubesys.httpfrk.core.HttpBodyHandler;
import com.github.kubesys.tools.annotations.ServiceDefinition;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "这个有啥用")
@RequestMapping("/app")
@ServiceDefinition
public class SwaggerService extends HttpBodyHandler {
	
	@ApiOperation(value = "desc of method", notes = "")
	public Object echoHello( /* 参数注解 */ @ApiParam(value = "desc of param", required = true) @RequestParam String name) {
		return "Hello " + name + "!";
	}
	
	@ApiOperation(value = "desc of method", notes = "")
	public String echoHello1( /* 参数注解 */
			@ApiParam(value = "desc of name", required = true) @RequestParam String name,
			@ApiParam(value = "desc of habit", required = true) @RequestParam String habit) throws Exception {
		throw new Exception("assddd");
	}
	
	@ApiOperation(value = "desc of method", notes = "notes")
	@PostMapping
	public Object echoHello2(@ApiParam(value = "desc of user", type = "body", required = true, example = "asf") @RequestBody User user) {
		return "Hello, " + user.getName();
	}
	
	@ApiModel(value = "user")
	public static class User {
		
		@ApiModelProperty(notes = "First name of the person.", example = "John", required = true, position = 1)
		@Size(min = 1, max = 20)
		protected String name;
		
		@ApiModelProperty(notes = "Age of the person. Non-negative integer", example = "42", position = 3)
		@Min(0)
	    @Max(100)
		protected int age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
		
	}
}