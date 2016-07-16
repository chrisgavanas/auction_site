package com.webapplication.validator.user;

import com.webapplication.dto.user.AddressDto;
import com.webapplication.dto.user.UserRegisterRequestDto;
import com.webapplication.error.user.UserRegisterError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class UserRegisterValidator implements Validator<UserRegisterRequestDto> {

    @Override
    public void validate(UserRegisterRequestDto request) throws ValidationException {
        Optional.ofNullable(request).orElseThrow(() -> new ValidationException(UserRegisterError.MISSING_DATA));

        if (Stream.of(request.getCountry(), request.getDateOfBirth(), request.getEmail(),
                request.getFirstName(), request.getGender(), request.getLastName(),
                request.getMobileNumber(), request.getPassword(), request.getRegistrationDate(),
                request.getUsername(), request.getVat()).anyMatch(Objects::isNull))
            throw new ValidationException(UserRegisterError.MISSING_DATA);

        if (Stream.of(request.getCountry(), request.getEmail(), request.getFirstName(),
                request.getLastName(), request.getMobileNumber(), request.getPassword(),
                request.getUsername(), request.getVat(), request.getPhoneNumber()).filter(Objects::nonNull).anyMatch(String::isEmpty))
            throw new ValidationException(UserRegisterError.INVALID_DATA);

        AddressDto address = request.getAddress();
        if (address != null) {
            if (Stream.of(address.getCity(), address.getPostalCode(), address.getStreet())
                    .anyMatch(Objects::isNull))
                throw new ValidationException(UserRegisterError.MISSING_DATA);
            if (Stream.of(address.getCity(), address.getPostalCode(), address.getStreet())
                    .filter(Objects::nonNull).anyMatch(String::isEmpty))
                throw new ValidationException(UserRegisterError.INVALID_DATA);
        }

    }
}
