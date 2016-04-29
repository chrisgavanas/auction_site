package com.webapplication.validator.user;

import com.webapplication.dto.user.ChangePasswordRequestDto;
import com.webapplication.error.user.UserError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class ChangePasswordValidator implements Validator<ChangePasswordRequestDto> {

    @Override
    public void validate(ChangePasswordRequestDto request) throws ValidationException {
        Optional.ofNullable(request).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        System.out.println(request.getAuthToken());
        System.out.println(request.getPassword());
        if (Arrays.asList(request.getPassword(), request.getAuthToken())
                .stream().anyMatch(Objects::isNull))
            throw new ValidationException(UserError.MISSING_DATA);

        if (request.getPassword().isEmpty())
            throw new ValidationException(UserError.INVALID_DATA);
    }
}
