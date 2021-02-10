/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.springframework.stereotype.Component;



/**
 * @author  wuheng
 * @since   2021.1.20
 */
@Component
public class Jsr303Tool {

	/**********************************
	 *  JSR 303
	 **********************************/
	
	protected Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	public <T> ValidationResult validateEntity(T obj) {
		ValidationResult result = new ValidationResult();
		Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);

		if (set != null && set.size() != 0) {

			result.setHasErrors(true);
			Map<String, String> errorMsg = new HashMap<String, String>();

			for (ConstraintViolation<T> cv : set) {
				errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
			}
			result.setErrorMsg(errorMsg);
		}
		return result;
	}
	
	public static class ValidationResult {
		 
	    private boolean             hasErrors;
	 
	    private Map<String, String> errorMsg;

		public boolean isHasErrors() {
			return hasErrors;
		}

		public void setHasErrors(boolean hasErrors) {
			this.hasErrors = hasErrors;
		}

		public Map<String, String> getErrorMsg() {
			return errorMsg;
		}

		public void setErrorMsg(Map<String, String> errorMsg) {
			this.errorMsg = errorMsg;
		}
	    
	}
}
