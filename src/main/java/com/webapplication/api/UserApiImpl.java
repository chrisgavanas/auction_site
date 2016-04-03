package com.webapplication.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;
import com.webapplication.service.UserServiceApi;

@Component
public class UserApiImpl implements UserApi {

    @Autowired
    private UserServiceApi userServiceApi;

    public UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) {
        return userServiceApi.login(userLogInRequestDto);
    }
}
