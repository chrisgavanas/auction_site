package com.webapplication.service;

import java.util.Optional;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.webapplication.dao.CategoryRepository;
import com.webapplication.dao.UserRepository;
import com.webapplication.dto.Gender;
import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;
import com.webapplication.dto.UserRegisterRequestDto;
import com.webapplication.dto.UserRegisterResponseDto;
import com.webapplication.entity.User;
import com.webapplication.error.UserLogInError;

@Transactional
@Component
public class UserServiceApiImpl implements UserServiceApi {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws Exception {
    	UserLogInResponseDto responseDto = new UserLogInResponseDto();
    	User user = userRepository.findByUsername(userLogInRequestDto.getPassword());
    	Optional.ofNullable(user).orElseThrow(() -> new NotFoundException(UserLogInError.INVALID_CREDENTIALS.getDescription()));
    	if (!userLogInRequestDto.getPassword().equals(user.getPassword()))
    		throw new NotFoundException(UserLogInError.INVALID_CREDENTIALS.getDescription());
    	
    	responseDto.setUsername(user.getUsername());
    	
    	return responseDto;
    }

	public UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) throws Exception {
		UserRegisterResponseDto responseDto = new UserRegisterResponseDto();
		responseDto.setGender(Gender.M);
		return responseDto;
	}

}
