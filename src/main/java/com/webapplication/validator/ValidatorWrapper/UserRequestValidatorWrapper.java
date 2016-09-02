package com.webapplication.validator.ValidatorWrapper;

import com.webapplication.dto.user.ChangePasswordRequestDto;
import com.webapplication.dto.user.MessageDto;
import com.webapplication.dto.user.UserLogInRequestDto;
import com.webapplication.dto.user.UserRegisterRequestDto;
import com.webapplication.dto.user.UserUpdateRequestDto;
import com.webapplication.exception.ValidationException;

public interface UserRequestValidatorWrapper {

    void validate(UserLogInRequestDto userLogInRequestDto) throws ValidationException;

    void validate(UserRegisterRequestDto userRegisterRequestDto) throws ValidationException;

    void validate(ChangePasswordRequestDto changePasswordRequestDto) throws ValidationException;

    void validate(UserUpdateRequestDto userUpdateRequestDto) throws ValidationException;

    void validate(MessageDto messageDto) throws ValidationException;

}
