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
        ""     | UserError.INVALID_DATA.description
    }

    def "Fetching a user successfully"() {
        given:
        UUID uuid = UUID.randomUUID()
        String userId = "578f9c605a61fe0aa84fe8e5"
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
        ""     | UserError.INVALID_DATA.description
    }

    def "VerifyUser a user successfully"() {
        given:
        UUID uuid = UUID.randomUUID()
        String userId = "578f9c605a61fe0aa84fe8e5"

        when:
        userApi.verifyUser(uuid, userId)

        then:
        1 * mockUserService.verifyUser(uuid, userId)
        0 * _
    }

    @Unroll
    def "Updates a user fails due to invalid userId"() {
        given:
        UserUpdateRequestDto updateRequestDto = new UserUpdateRequestDto()

        when:
        userApi.updateUser(userId, updateRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == error

        where:
        userId | error
        null   | UserError.MISSING_DATA.description
        ""     | UserError.INVALID_DATA.description
    }

    def "Update a user successfully"() {
        given:
        String userId = "578f9c605a61fe0aa84fe8e5"
        UserUpdateRequestDto updateRequestDto = new UserUpdateRequestDto()
        UserResponseDto userResponseDto = new UserResponseDto()

        when:
        userApi.updateUser(userId, updateRequestDto)

        then:
        1 * mockUserRequestValidator.validate(updateRequestDto)
        1 * mockUserService.updateUser(userId, updateRequestDto) >> userResponseDto
        0 * _
    }

    @Unroll
    def "Change user's password fails due to invalid userId"() {
        given:
        UUID authToken = UUID.randomUUID()
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto()

        when:
        userApi.changePassword(authToken, userId, changePasswordRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == error

        where:
        userId | error
        null   | UserError.MISSING_DATA.description
        ""     | UserError.INVALID_DATA.description
    }

    def "Change user's password fails as user did not provide an authentication token"() {
        given:
        String userId = "578f9c605a61fe0aa84fe8e5"
        UUID authToken = null
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto()

        when:
        userApi.changePassword(authToken, userId, changePasswordRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserError.MISSING_DATA.description
    }

    def "User changes password successfully"() {
        given:
        String userId = "578f9c605a61fe0aa84fe8e5"
        UUID authToken = UUID.randomUUID()
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto()

        when:
        userApi.changePassword(authToken, userId, changePasswordRequestDto)

        then:
        1 * mockUserRequestValidator.validate(changePasswordRequestDto)
        1 * mockUserService.changePassword(userId, changePasswordRequestDto)
        0 * _
    }

    @Unroll
    def "Admin requests for a specific number of unverified users with missing data"() {
        given:
        UUID authToken = UUID.randomUUID()
        Integer from = fromValue
        Integer to = toValue

        when:
        userApi.getUnverifiedUsers(authToken, from, to)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserError.MISSING_DATA.description

        where:
        fromValue | toValue
        null      | null
        null      | 2
        2         | null
    }

    @Unroll
    def "Admin requests for a specific number of unverified users with invalid data"() {
        given:
        UUID authToken = UUID.randomUUID()

        when:
        userApi.getUnverifiedUsers(authToken, from, to)

        then:
        ValidationException e = thrown()
        e.localizedMessage == error

        where:
        from | to | error
        -1   | 2  | UserError.INVALID_DATA.description
        0    | 3  | UserError.INVALID_DATA.description
        1    | -3 | UserError.INVALID_DATA.description
        5    | 0  | UserError.INVALID_DATA.description
        2    | 1  | UserError.INVALID_PAGINATION_VALUES.description
    }

    def "Admin requests for a specific number of unverified users successfully"() {
        given:
        UUID authToken = UUID.randomUUID()
        Integer from = 2
        Integer to = 12

        when:
        userApi.getUnverifiedUsers(authToken, from, to)

        then:
        ValidationException e = notThrown()
        1 * mockUserService.getUnverifiedUsers(authToken, from, to)
        0 * _
    }
}
