package com.webapplication.validator;

import java.util.Arrays;
import java.util.Objects;



import javax.validation.ValidationException;

import org.springframework.stereotype.Component;

import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.error.UserLogInError;

@Component
public class UserLogInValidator implements Validator<UserLogInRequestDto> {

	@Override
	public void validate(UserLogInRequestDto request) throws ValidationException {
		if (request == null)
			throw new ValidationException(UserLogInError.MISSING_DATA.getDescription());

		if (Arrays.asList(request.getUsername(), request.getPassword())
			.stream().anyMatch(field -> { return Objects.isNull(field); }))
				throw new ValidationException(UserLogInError.MISSING_DATA.getDescription());

		if (Arrays.asList(request.getUsername(), request.getPassword())
			.stream().anyMatch(field -> { return field.isEmpty(); }))
				throw new ValidationException(UserLogInError.INVALID_DATA.getDescription());
	}

}
