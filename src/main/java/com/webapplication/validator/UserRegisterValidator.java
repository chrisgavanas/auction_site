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
		UserRegisterError error;
		
		if (request == null) {
			error = UserRegisterError.MISSING_DATA;
			throw new ValidationException(error.getDescription());
		}
		
		if (Arrays.asList(request.getCountry(), request.getDateOfBirth(), request.getEmail(),
				request.getFirstName(), request.getGender(), request.getIsAdmin(), request.getLastName(),
				request.getMobileNumber(), request.getPassword(), request.getRegistrationDate(), 
				request.getUsername(), request.getVat())
				.stream().anyMatch(field -> { return Objects.isNull(field); })) {
					error = UserRegisterError.MISSING_DATA;
					throw new ValidationException(error.getDescription());
		}
						
		if (Arrays.asList(request.getCountry(), request.getEmail(), request.getFirstName(), 
				request.getLastName(), request.getMobileNumber(), request.getPassword(), 
				request.getUsername(), request.getVat())
				.stream().anyMatch(field -> { return field.isEmpty(); })) {
					error = UserRegisterError.MISSING_DATA;
					throw new ValidationException(error.getDescription());
		}	
		
		if (Arrays.asList(request.getStreet(), request.getCity(), request.getPostalCode(), request.getPhoneNumber())
			.stream().filter(Objects::nonNull).anyMatch(field -> { return field.isEmpty(); })) {
				error = UserRegisterError.INVALID_DATA;
				throw new ValidationException(error.getDescription());
		}
	}
}
