package com.webapplication.validator;

import java.util.Arrays;
import java.util.Objects;

import javax.validation.ValidationException;

import org.springframework.stereotype.Component;

import com.webapplication.dto.UserRegisterRequestDto;
import com.webapplication.error.UserRegisterError;

@Component
public class UserRegisterValidator implements Validator<UserRegisterRequestDto> {

	@Override
	public void validate(UserRegisterRequestDto request) throws ValidationException {		
		if (request == null)
			throw new ValidationException(UserRegisterError.MISSING_DATA.getDescription());
		
		if (Arrays.asList(request.getCountry(), request.getDateOfBirth(), request.getEmail(),
				request.getFirstName(), request.getGender(), request.getIsAdmin(), request.getLastName(),
				request.getMobileNumber(), request.getPassword(), request.getRegistrationDate(), 
				request.getUsername(), request.getVat())
				.stream().anyMatch(field -> { return Objects.isNull(field); }))
					throw new ValidationException(UserRegisterError.MISSING_DATA.getDescription());
						
		if (Arrays.asList(request.getCountry(), request.getEmail(), request.getFirstName(), 
				request.getLastName(), request.getMobileNumber(), request.getPassword(), 
				request.getUsername(), request.getVat())
				.stream().anyMatch(field -> { return field.isEmpty(); }))
					throw new ValidationException(UserRegisterError.MISSING_DATA.getDescription());
		
		if (Arrays.asList(request.getStreet(), request.getCity(), request.getPostalCode(), request.getPhoneNumber())
			.stream().filter(Objects::nonNull).anyMatch(field -> { return field.isEmpty(); }))
				throw new ValidationException(UserRegisterError.INVALID_DATA.getDescription());
	}
}
