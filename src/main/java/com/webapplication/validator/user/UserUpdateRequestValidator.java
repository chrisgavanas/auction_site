package com.webapplication.validator.user;

import com.webapplication.dto.user.UserUpdateRequestDto;
import com.webapplication.error.user.UserError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserUpdateRequestValidator implements Validator<UserUpdateRequestDto> {

    @Override
    public void validate(UserUpdateRequestDto userUpdateRequestDto) throws ValidationException {
        Optional.ofNullable(userUpdateRequestDto).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));

        if (Arrays.asList(userUpdateRequestDto.getEmail(), userUpdateRequestDto.getFirstName(),
                userUpdateRequestDto.getLastName(), userUpdateRequestDto.getCountry(), userUpdateRequestDto.getMobileNumber(),
                userUpdateRequestDto.getGender(), userUpdateRequestDto.getVat(), userUpdateRequestDto.getDateOfBirth())
                .stream().anyMatch(Objects::isNull))
            throw new ValidationException(UserError.MISSING_DATA);

        if (Arrays.asList(userUpdateRequestDto.getEmail(), userUpdateRequestDto.getFirstName(), userUpdateRequestDto.getLastName(),
                userUpdateRequestDto.getCountry(), userUpdateRequestDto.getMobileNumber(), userUpdateRequestDto.getVat())
                .stream().anyMatch(String::isEmpty))
            throw new ValidationException(UserError.INVALID_DATA);
    }

}
