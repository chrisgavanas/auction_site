package com.webapplication.validator;

import javax.validation.ValidationException;

public interface Validator<T> {

	void validate(T request) throws ValidationException;
}
