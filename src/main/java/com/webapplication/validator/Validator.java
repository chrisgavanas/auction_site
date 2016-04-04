package com.webapplication.validator;

import com.webapplication.exception.ValidationException;


public interface Validator<T> {

	void validate(T request) throws ValidationException;
}
