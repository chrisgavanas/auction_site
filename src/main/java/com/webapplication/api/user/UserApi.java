package com.webapplication.api.user;

import com.webapplication.dto.user.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public interface UserApi {

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws Exception;

    @RequestMapping(path = "/user", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) throws Exception;

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET, produces = "application/json")
    UserResponseDto getUser(@PathVariable Integer userId) throws Exception;

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    UserResponseDto updateUser(@PathVariable Integer userId, UserUpdateRequestDto userUpdateRequestDto) throws Exception;

    @RequestMapping(path = "/user/{userId}/verifyUser", method = RequestMethod.POST)
    void verifyUser(@PathVariable Integer userId) throws Exception;

    @RequestMapping(path = "/user/{userId}/changePassword", method = RequestMethod.POST, consumes = "application/json")
    void changePassword(@PathVariable Integer userId, ChangePasswordRequestDto changePasswordRequestDto) throws Exception;

}
