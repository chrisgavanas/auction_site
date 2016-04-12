package com.webapplication.validator.user;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.webapplication.error.user.UserError;
import com.webapplication.exception.NotFoundException;
import com.webapplication.exception.ValidationException;

@Component
public class UserRequestValidator {

    public void validate(Integer userId) throws Exception {

        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.USER_DOES_NOT_EXIST));
        if (userId <= 0)
            throw new NotFoundException(UserError.USER_DOES_NOT_EXIST);

    }
}
