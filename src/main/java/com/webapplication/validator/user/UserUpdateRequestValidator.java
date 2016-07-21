package com.webapplication.validator.user;

import com.google.common.base.Strings;
import com.webapplication.dto.user.AddressDto;
import com.webapplication.dto.user.UserUpdateRequestDto;
import com.webapplication.error.user.UserError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class UserUpdateRequestValidator implements Validator<UserUpdateRequestDto> {

    @Override
    public void validate(UserUpdateRequestDto userUpdateRequestDto) throws ValidationException {
        Optional.ofNullable(userUpdateRequestDto).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));

        if (Stream.of(userUpdateRequestDto.getEmail(), userUpdateRequestDto.getFirstName(), userUpdateRequestDto.getPhoneNumber(),
                userUpdateRequestDto.getLastName(), userUpdateRequestDto.getCountry(), userUpdateRequestDto.getMobileNumber(),
                userUpdateRequestDto.getGender(), userUpdateRequestDto.getVat(), userUpdateRequestDto.getDateOfBirth())
                .anyMatch(Objects::isNull))
            throw new ValidationException(UserError.MISSING_DATA);

        if (Stream.of(userUpdateRequestDto.getEmail(), userUpdateRequestDto.getFirstName(), userUpdateRequestDto.getLastName(), userUpdateRequestDto.getPhoneNumber(),
                userUpdateRequestDto.getCountry(), userUpdateRequestDto.getMobileNumber(), userUpdateRequestDto.getVat())
                .anyMatch(String::isEmpty))
            throw new ValidationException(UserError.INVALID_DATA);

        AddressDto addressDto = userUpdateRequestDto.getAddress();
        if (addressDto != null) {
            if (Stream.of(addressDto.getCity(), addressDto.getPostalCode(), addressDto.getStreet()).anyMatch(Objects::isNull))
                throw new ValidationException(UserError.MISSING_DATA);

            if (Stream.of(addressDto.getCity(), addressDto.getPostalCode(), addressDto.getStreet()).anyMatch(Strings::isNullOrEmpty))
                throw new ValidationException(UserError.INVALID_DATA);
        }
    }

}
