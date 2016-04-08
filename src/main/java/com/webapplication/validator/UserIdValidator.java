package com.webapplication.validator;

import org.springframework.stereotype.Component;

import com.webapplication.dto.UserIdRequestDto;
import com.webapplication.error.UserLogInError;
import com.webapplication.exception.ValidationException;

@Component
public class UserIdValidator implements Validator<UserIdRequestDto> {

	@Override
	public void validate(UserIdRequestDto request) throws ValidationException {
		
		if (request == null)
			throw new ValidationException(UserLogInError.MISSING_DATA);
		
		System.out.println(request.getId());
	}
}