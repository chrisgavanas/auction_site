package com.webapplication.service.user;

import com.webapplication.dto.user.*;


public interface UserServiceApi {

    UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws Exception;

    UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) throws Exception;

    UserResponseDto getUser(Integer userId) throws Exception;

    void verifyUser(Integer userId) throws Exception;

    UserResponseDto updateUser(UserRequestDto userRequestDto) throws Exception;
}
