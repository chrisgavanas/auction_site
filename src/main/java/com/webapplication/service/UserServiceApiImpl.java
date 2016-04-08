package com.webapplication.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.webapplication.dao.UserRepository;
import com.webapplication.dto.UserIdRequestDto;
import com.webapplication.dto.UserIdResponseDto;
import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;
import com.webapplication.dto.UserRegisterRequestDto;
import com.webapplication.dto.UserRegisterResponseDto;
import com.webapplication.entity.User;
import com.webapplication.error.UserIdError;
import com.webapplication.error.UserLogInError;
import com.webapplication.error.UserRegisterError;
import com.webapplication.exception.EmailUnverifiedException;
import com.webapplication.exception.NotFoundException;
import com.webapplication.exception.UserAlreadyExists;
import com.webapplication.mapper.UserMapper;

@Transactional
@Component
public class UserServiceApiImpl implements UserServiceApi {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	public UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws Exception {
		User user = userRepository.findUserByUsernameOrEmailAndPassword(userLogInRequestDto.getPassword(),
				userLogInRequestDto.getUsername(), userLogInRequestDto.getEmail());

		Optional.ofNullable(user).orElseThrow(() -> new NotFoundException(UserLogInError.INVALID_CREDENTIALS));
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

		user = userMapper.convert(userRegisterRequestDto);
		userRepository.save(user);

		return userMapper.convert(user);
	}

	public UserIdResponseDto getUserId(UserIdRequestDto userIdRequestDto) throws Exception {
		User user = userRepository.findUserByUserId(userIdRequestDto.getId());
		if (user == null)
			throw new NotFoundException(UserIdError.NOT_EXISTS);
		UserIdResponseDto responseDto = new UserIdResponseDto();
		responseDto.setUsername(user.getUsername());

		return responseDto;

	}

}
