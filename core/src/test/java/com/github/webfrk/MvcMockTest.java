/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.webfrk;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.github.kubesys.httpfrk.core.HttpConstants;


/**
 * @author wuheng@iscas.ac.cn
 * @since  2019.11.16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.github.kubesys.httpfrk.core.HttpController.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages= {"com.github.webfrk", "dev.examples.basic.services"})
public class MvcMockTest  {

	public final static String INVALID_GET_REQUEST_PATH = "/mock/listMock8";
	
	public final static String VALID_GET_REQUEST_PATH = "/mock/listMock";
	
	public final static String LESS_THAN_MIN_SIZE = "ab";
	
	public final static String GREAT_THAN_MAX_SIZE = "abccccccccc";
	
	public final static String VALID_SIZE = "abc";
	
	@Autowired
	private MockMvc mvc;

	@Test
	public void testInvalidGetRequestBody() throws Exception {
		@SuppressWarnings("deprecation")
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(
							INVALID_GET_REQUEST_PATH)
				.param("name", LESS_THAN_MIN_SIZE)
				.accept(MediaType.APPLICATION_JSON_UTF8);
		mvc.perform(builder)
				.andExpect(status().isOk())
				.andExpect(jsonPath("code").value(HttpConstants.HTTP_RESPONSE_STATUS_FAILED));
	}
	

	
}
