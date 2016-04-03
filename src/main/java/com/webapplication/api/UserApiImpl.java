package com.webapplication.api;

import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;
import com.webapplication.service.UserServiceApi;
import com.webapplication.validator.UserValidator;

@Component
public class UserApiImpl implements UserApi {

    @Autowired
    private UserServiceApi userServiceApi;

    @Autowired
    private UserValidator userValidator;
    
    public UserLogInResponseDto login(@RequestBody UserLogInRequestDto userLogInRequestDto) throws ValidationException {
    	userValidator.validate(userLogInRequestDto);
        return userServiceApi.login(userLogInRequestDto);
    }
}
