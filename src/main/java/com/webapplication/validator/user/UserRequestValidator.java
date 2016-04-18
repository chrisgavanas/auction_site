package com.webapplication.validator.user;

import com.webapplication.dto.user.UserRequestDto;
import com.webapplication.error.user.UserError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserRequestValidator implements Validator<UserRequestDto> {

    @Override
    public void validate(UserRequestDto userRequestDto) throws ValidationException {
        Optional.ofNullable(userRequestDto).orElseThrow(() -> new ValidationException(UserError.INVALID_DATA));

        if (Arrays.asList(userRequestDto.getUserId(),userRequestDto.getEmail(), userRequestDto.getFirstName(),
                userRequestDto.getLastName(), userRequestDto.getCountry(), userRequestDto.getMobileNumber(),
                userRequestDto.getRegistrationDate(), userRequestDto.getGender(), userRequestDto.getVat(),
                userRequestDto.getDateOfBirth()).stream().anyMatch(Objects::isNull))
            throw new ValidationException(UserError.INVALID_DATA);

        if (userRequestDto.getUserId() <= 0)
            throw new ValidationException(UserError.INVALID_DATA);

        if (Arrays.asList(userRequestDto.getEmail(), userRequestDto.getFirstName(), userRequestDto.getLastName(),
                userRequestDto.getCountry(),userRequestDto.getMobileNumber(), userRequestDto.getVat())
                .stream().anyMatch(String::isEmpty))
            throw new ValidationException(UserError.INVALID_DATA);
    }

}
