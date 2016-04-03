package com.webapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.webapplication.dao.CategoryRepository;
import com.webapplication.dao.UserRepository;
import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;
import com.webapplication.dto.UserRegisterRequestDto;
import com.webapplication.dto.UserRegisterResponseDto;

@Transactional
@Component
public class UserServiceApiImpl implements UserServiceApi {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;	

    public UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) {
    	UserLogInResponseDto responseDto = new UserLogInResponseDto();
    	responseDto.setUsername("lala");
    	return responseDto;
    }

	@Override
	public UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) {
		return null;
	}

}
