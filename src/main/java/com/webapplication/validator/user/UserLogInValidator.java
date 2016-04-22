package com.webapplication.validator.user;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.webapplication.dto.user.UserLogInRequestDto;
import com.webapplication.error.user.UserLogInError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;

@Component
public class UserLogInValidator implements Validator<UserLogInRequestDto> {

    @Override
    public void validate(UserLogInRequestDto request) throws ValidationException {
        Optional.ofNullable(request).orElseThrow(() -> new ValidationException(UserLogInError.MISSING_DATA));
        Optional.ofNullable(request.getPassword()).orElseThrow(() -> new ValidationException(UserLogInError.MISSING_DATA));

        if (Arrays.asList(request.getUsername(), request.getEmail())
                .stream().filter(Objects::nonNull).count() == 0)
            throw new ValidationException(UserLogInError.MISSING_DATA);

        if (Arrays.asList(request.getUsername(), request.getEmail(), request.getPassword())
                .stream().filter(Objects::nonNull).anyMatch(String::isEmpty))
            throw new ValidationException(UserLogInError.INVALID_DATA);

    }

}
