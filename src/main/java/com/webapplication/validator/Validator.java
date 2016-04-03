package com.webapplication.validator;

import javax.xml.bind.ValidationException;

public interface Validator<T> {

	void validate(T request) throws ValidationException;
}
