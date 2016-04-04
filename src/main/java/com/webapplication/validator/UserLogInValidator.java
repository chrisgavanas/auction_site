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
        UserLogInError error;

        if (request == null) {
            error = UserLogInError.MISSING_DATA;
            throw new ValidationException(error.getDescription());
        }
        if (Arrays.asList(request.getUsername(), request.getPassword())
            .stream().anyMatch(field -> { return Objects.isNull(field); })) {
                error = UserLogInError.MISSING_DATA;
                throw new ValidationException(error.getDescription());
        }
        if (Arrays.asList(request.getUsername(), request.getPassword())
            .stream().anyMatch(field -> { return field.isEmpty(); })) {
                error = UserLogInError.INVALID_DATA;
                throw new ValidationException(error.getDescription());
        }
    }

}
