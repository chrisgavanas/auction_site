package com.webapplication.validator.user

import com.webapplication.dto.user.ChangePasswordRequestDto
import com.webapplication.dto.user.UserLogInRequestDto
import com.webapplication.dto.user.UserRegisterRequestDto
import com.webapplication.dto.user.UserUpdateRequestDto
import spock.lang.Specification


class UserRequestValidatorSpec extends Specification {

    UserLogInValidator mockUserLogInValidator;
    UserRegisterValidator mockUserRegisterValidator;
    UserUpdateRequestValidator mockUserUpdateRequestValidator;
    ChangePasswordValidator mockChangePasswordValidator;
    UserRequestValidator userRequestValidator

    def setup() {
        mockUserLogInValidator = Mock(UserLogInValidator)
        mockUserRegisterValidator = Mock(UserRegisterValidator)
        mockUserUpdateRequestValidator = Mock(UserUpdateRequestValidator)
        mockChangePasswordValidator = Mock(ChangePasswordValidator)

        userRequestValidator = new UserRequestValidator(userLogInValidator: mockUserLogInValidator, userRegisterValidator: mockUserRegisterValidator,
                userUpdateRequestValidator: mockUserUpdateRequestValidator, changePasswordValidator: mockChangePasswordValidator)
    }

    def "Validate a log in request"() {
        given:
        UserLogInRequestDto userLogInRequestDto = new UserLogInRequestDto()

        when:
        userRequestValidator.validate(userLogInRequestDto)

        then:
        1 * mockUserLogInValidator.validate(userLogInRequestDto)
        0 * _

    }


    def "Validate a register request"() {
        given:
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto()

        when:
        userRequestValidator.validate(userRegisterRequestDto)

        then:
        1 * mockUserRegisterValidator.validate(userRegisterRequestDto)
        0 * _
    }

    def "Validate an update request"() {
        given:
        UserUpdateRequestDto updateRequestDto = new UserUpdateRequestDto()

        when:
        userRequestValidator.validate(updateRequestDto)

        then:
        1 * mockUserUpdateRequestValidator.validate(updateRequestDto)
        0 * _
    }

    def "Validate a change password request"() {
        given:
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto()

        when:
        userRequestValidator.validate(changePasswordRequestDto)

        then:
        1 * mockChangePasswordValidator.validate(changePasswordRequestDto)
        0 * _
    }
}
