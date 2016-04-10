package com.webapplication.service.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.webapplication.dao.UserRepository;
import com.webapplication.dto.user.UserLogInRequestDto;
import com.webapplication.dto.user.UserLogInResponseDto;
import com.webapplication.dto.user.UserRegisterRequestDto;
import com.webapplication.dto.user.UserRegisterResponseDto;
import com.webapplication.dto.user.UserResponseDto;
import com.webapplication.entity.User;
import com.webapplication.error.user.UserError;
import com.webapplication.error.user.UserLogInError;
import com.webapplication.error.user.UserRegisterError;
import com.webapplication.exception.user.EmailUnverifiedException;
import com.webapplication.exception.user.NotFoundException;
import com.webapplication.exception.user.UserAlreadyExistsException;
import com.webapplication.exception.user.UserAlreadyVerifiedException;
import com.webapplication.exception.user.UserNotFoundException;
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
            throw new UserAlreadyExistsException(user.getUsername().equals(userRegisterRequestDto.getUsername())
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

    public void verifyUser(Integer userId) throws Exception {
        User user = userRepository.findUserByUserId(userId);
        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(UserError.USER_DOES_NOT_EXIST));

        if (user.getIsVerified())
            throw new UserAlreadyVerifiedException(UserError.USER_ALREADY_VERIFIED);

        user.setIsVerified(true);
        userRepository.save(user);
    }

}
