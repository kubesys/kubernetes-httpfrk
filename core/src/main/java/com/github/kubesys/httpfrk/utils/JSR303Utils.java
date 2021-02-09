/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.httpfrk.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;


/**
 * @author  wuheng
 * @since   2019.2.20
 */
public class JSR303Utils {

	/**********************************
	 *  JSR 303
	 **********************************/
	
	protected static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	public static <T> ValidationResult validateEntity(T obj) {
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
