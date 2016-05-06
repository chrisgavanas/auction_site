package com.webapplication.validator.user;

import com.webapplication.dto.user.ChangePasswordRequestDto;
import com.webapplication.error.user.UserError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class ChangePasswordValidator implements Validator<ChangePasswordRequestDto> {

    @Override
    public void validate(ChangePasswordRequestDto request) throws ValidationException {
        Optional.ofNullable(request).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (Arrays.asList(request.getOldPassword(), request.getNewPassword(), request.getAuthToken())
                .stream().anyMatch(Objects::isNull))
            throw new ValidationException(UserError.MISSING_DATA);

        if (Arrays.asList(request.getOldPassword(), request.getNewPassword())
                .stream().anyMatch(String::isEmpty))
            throw new ValidationException(UserError.INVALID_DATA);

        if (request.getOldPassword().equals(request.getNewPassword()))
            throw new ValidationException(UserError.NEW_PASSWORD_DO_NOT_DIFFER);
    }
}
