package com.webapplication.validator.user;

import com.webapplication.dto.user.UserRegisterRequestDto;
import com.webapplication.error.user.UserRegisterError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
public class UserRegisterValidator implements Validator<UserRegisterRequestDto> {

    @Override
    public void validate(UserRegisterRequestDto request) throws ValidationException {
        if (request == null)
            throw new ValidationException(UserRegisterError.MISSING_DATA);

        if (Arrays.asList(request.getCountry(), request.getDateOfBirth(), request.getEmail(),
                request.getFirstName(), request.getGender(), request.getLastName(),
                request.getMobileNumber(), request.getPassword(), request.getRegistrationDate(),
                request.getUsername(), request.getVat())
                .stream().anyMatch(Objects::isNull))
                    throw new ValidationException(UserRegisterError.MISSING_DATA);

        if (Arrays.asList(request.getCountry(), request.getEmail(), request.getFirstName(),
                request.getLastName(), request.getMobileNumber(), request.getPassword(),
                request.getUsername(), request.getVat())
                .stream().anyMatch(String::isEmpty))
                    throw new ValidationException(UserRegisterError.MISSING_DATA);

        if (Arrays.asList(request.getStreet(), request.getCity(), request.getPostalCode(), request.getPhoneNumber())
            .stream().filter(Objects::nonNull).anyMatch(String::isEmpty))
                throw new ValidationException(UserRegisterError.INVALID_DATA);

    }
}
