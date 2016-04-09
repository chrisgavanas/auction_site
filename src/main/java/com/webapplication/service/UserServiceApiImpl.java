package com.webapplication.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.webapplication.dao.UserRepository;
import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;
import com.webapplication.dto.UserRegisterRequestDto;
import com.webapplication.dto.UserRegisterResponseDto;
import com.webapplication.dto.UserResponseDto;
import com.webapplication.entity.User;
import com.webapplication.error.UserError;
import com.webapplication.error.UserLogInError;
import com.webapplication.error.UserRegisterError;
import com.webapplication.exception.EmailUnverifiedException;
import com.webapplication.exception.NotFoundException;
import com.webapplication.exception.UserAlreadyExists;
import com.webapplication.exception.UserNotFoundException;
import com.webapplication.mapper.UserMapper;

@Transactional
@Component
public class UserServiceApiImpl implements UserServiceApi {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;


    public UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws Exception {
        User user = userRepository.findUserByUsernameOrEmailAndPassword(userLogInRequestDto.getUsername(), userLogInRequestDto.getEmail(), userLogInRequestDto.getPassword());


        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(UserLogInError.INVALID_CREDENTIALS));
        if (!user.getIsVerified())
            throw new EmailUnverifiedException(UserLogInError.USER_NOT_EMAIL_VERIFIED);

        UserLogInResponseDto responseDto = new UserLogInResponseDto();
        responseDto.setUseId(user.getUserId());
        return responseDto;
    }


    public UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) throws Exception {
        User user = userRepository.findUserByUsernameOrEmail(userRegisterRequestDto.getUsername(),
                userRegisterRequestDto.getEmail());
        if (user != null)
            throw new UserAlreadyExists(user.getUsername().equals(userRegisterRequestDto.getUsername())
                    ? UserRegisterError.USERNAME_ALREADY_IN_USE : UserRegisterError.EMAIL_ALREADY_USED);

        user = userMapper.registerRequestToUser(userRegisterRequestDto);
        userRepository.save(user);

        return userMapper.userToRegisterResponse(user);
    }

    public UserResponseDto getUser(Integer userId) throws Exception {
        User user = userRepository.findUserByUserId(userId);
        if (user == null)
            throw new NotFoundException(UserError.USER_DOES_NOT_EXIST);

        UserResponseDto responseDto = new UserResponseDto();
        responseDto = userMapper.userToUserRepsonse(user);

        return responseDto;

    }

}
