package com.webapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;

@Component
public class UserServiceApiImpl implements UserServiceApi {
	
//	@Autowired
//	private UserRepository userRepository;
	
	@Override
	public UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) {
//		System.out.println(userRepository.findByUsername("Xristos"));
		return new UserLogInResponseDto();
	}

}
