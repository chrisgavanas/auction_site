package com.webapplication.service.user;

import com.webapplication.dto.user.*;
import com.webapplication.validator.user.ChangePasswordValidator;

import java.util.UUID;


public interface UserServiceApi {

    UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws Exception;

    UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) throws Exception;

    UserResponseDto getUser(UUID authToken, Integer userId) throws Exception;

    void verifyUser(UUID authToken, Integer userId) throws Exception;

    UserResponseDto updateUser(Integer userId, UserUpdateRequestDto userUpdateRequestDto) throws Exception;

    void changePassword(Integer userId, ChangePasswordRequestDto changePasswordRequestDto) throws Exception;
}
