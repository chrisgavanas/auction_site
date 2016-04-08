package com.webapplication.api;




import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webapplication.dto.UserIdRequestDto;
import com.webapplication.dto.UserIdResponseDto;
import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;
import com.webapplication.dto.UserRegisterRequestDto;
import com.webapplication.dto.UserRegisterResponseDto;

@RestController
@RequestMapping(path = "/api")
public interface UserApi {

	@RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws Exception;

	@RequestMapping(path = "/user", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) throws Exception;
	
	@RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
	UserIdResponseDto getUserId(@PathVariable String userId) throws Exception;
}
	