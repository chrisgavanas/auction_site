package com.webapplication.api.user;

import com.webapplication.dto.user.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api")
public interface UserApi {

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws Exception;

    @RequestMapping(path = "/user", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) throws Exception;

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET, produces = "application/json")
    UserResponseDto getUser(@RequestHeader UUID authToken, @PathVariable Integer userId) throws Exception;

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    UserResponseDto updateUser(@PathVariable Integer userId, UserUpdateRequestDto userUpdateRequestDto) throws Exception;

    @RequestMapping(path = "/user/{userId}/verify-user", method = RequestMethod.POST)
    void verifyUser(@RequestHeader UUID authToken, @PathVariable Integer userId) throws Exception;

    @RequestMapping(path = "/user/{userId}/change-password", method = RequestMethod.POST, consumes = "application/json")
    void changePassword(@RequestHeader UUID authToken, @PathVariable Integer userId, ChangePasswordRequestDto changePasswordRequestDto) throws Exception;

    @RequestMapping(path = "/check", method = RequestMethod.POST, consumes = "application/json")
    void check(@RequestBody List<MultipartFile> files);
}
