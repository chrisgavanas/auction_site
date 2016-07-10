package com.webapplication.api.user

import com.webapplication.dto.user.*
import com.webapplication.error.user.UserError
import com.webapplication.exception.ValidationException
import com.webapplication.service.user.UserServiceApi
import com.webapplication.validator.user.UserRequestValidator
import spock.lang.Specification
import spock.lang.Unroll

class UserApiImplSpec extends Specification {

    UserApiImpl userApi
    UserServiceApi mockUserService
    UserRequestValidator mockUserRequestValidator

    def setup() {
        mockUserService = Mock(UserServiceApi)
        mockUserRequestValidator = Mock(UserRequestValidator)

        userApi = new UserApiImpl(userService: mockUserService, userRequestValidator: mockUserRequestValidator)
    }

    def "User successfully logs into his account"() {
        given:
        UserLogInRequestDto userLogInRequestDto = new UserLogInRequestDto()
        UserLogInResponseDto userLogInResponseDto = new UserLogInResponseDto(null, null)

        when:
        userApi.login(userLogInRequestDto)

        then:
        1 * mockUserRequestValidator.validate(userLogInRequestDto)
        1 * mockUserService.login(userLogInRequestDto) >> userLogInResponseDto
        0 * _
    }

    def "User successfully registers"() {
        given:
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto()
        UserRegisterResponseDto userRegisterResponseDto = new UserRegisterResponseDto()

        when:
        userApi.register(userRegisterRequestDto)

        then:
        1 * mockUserRequestValidator.validate(userRegisterRequestDto)
        1 * mockUserService.register(userRegisterRequestDto) >> userRegisterResponseDto
        0 * _
    }

    @Unroll
    def "Fetching a user fails due to non existing userId"() {
        given:
        UUID uuid = UUID.randomUUID()

        when:
        userApi.getUser(uuid, userId)

        then:
        ValidationException e = thrown()
        e.localizedMessage == error

        where:
        userId | error
        null   | UserError.MISSING_DATA.description
        -1     | UserError.INVALID_DATA.description
        0      | UserError.INVALID_DATA.description
    }

    def "Fetching a user successfully"() {
        given:
        UUID uuid = UUID.randomUUID()
        Integer userId = 4
        UserResponseDto userResponseDto = new UserResponseDto()

        when:
        userApi.getUser(uuid, userId)

        then:
        1 * mockUserService.getUser(uuid, userId) >> userResponseDto
        0 * _
    }

    @Unroll
    def "Verifying a user fails due to non existing userId"() {
        given:
        UUID uuid = UUID.randomUUID()

        when:
        userApi.verifyUser(uuid, userId)

        then:
        ValidationException e = thrown()
        e.localizedMessage == error

        where:
        userId | error
        null   | UserError.MISSING_DATA.description
        -1     | UserError.INVALID_DATA.description
        0      | UserError.INVALID_DATA.description
    }

    def "VerifyUser a user successfully"() {
        given:
        UUID uuid = UUID.randomUUID()
        Integer userId = 4

        when:
        userApi.verifyUser(uuid, userId)

        then:
        1 * mockUserService.verifyUser(uuid, userId)
        0 * _
    }

    @Unroll
    def "Updates a user fails due to invalid userId"() {
        given:
        UserUpdateRequestDto updateRequestDto

        when:
        userApi.updateUser(userId, updateRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == error

        where:
        userId | error
        null   | UserError.MISSING_DATA.description
        -1     | UserError.INVALID_DATA.description
        0      | UserError.INVALID_DATA.description

    }

    def "Update a user successfully"() {
        given:
        Integer userId = 4
        UserUpdateRequestDto updateRequestDto = new UserUpdateRequestDto()
        UserResponseDto userResponseDto = new UserResponseDto()

        when:
        userApi.updateUser(userId, updateRequestDto)

        then:
        1 * mockUserRequestValidator.validate(updateRequestDto)
        1 * mockUserService.updateUser(userId, updateRequestDto) >> userResponseDto
        0 * _
    }
}
