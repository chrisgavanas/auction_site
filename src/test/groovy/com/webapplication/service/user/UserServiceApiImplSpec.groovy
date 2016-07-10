package com.webapplication.service.user

import com.webapplication.authentication.Authenticator
import com.webapplication.dao.UserRepository
import com.webapplication.dto.user.*
import com.webapplication.entity.User
import com.webapplication.error.user.UserError
import com.webapplication.error.user.UserLogInError
import com.webapplication.error.user.UserRegisterError
import com.webapplication.exception.*
import com.webapplication.mapper.UserMapper
import org.joda.time.DateTime
import spock.lang.Specification
import spock.lang.Unroll

class UserServiceApiImplSpec extends Specification {

    UserRepository mockUserRepository
    UserMapper mockUserMapper
    UserServiceApi userServiceApiImpl
    Authenticator mockAuthenticator

    def setup() {
        mockUserRepository = Mock(UserRepository)
        mockUserMapper = Mock(UserMapper)
        mockAuthenticator = Mock(Authenticator)

        userServiceApiImpl = new UserServiceApiImpl(userRepository: mockUserRepository, userMapper: mockUserMapper, authenticator: mockAuthenticator)
    }

    def "User attempts to log in with invalid credentials"() {
        given:
        UserLogInRequestDto userLogInRequestDto = new UserLogInRequestDto(username: 'chris', password: 'some password')

        when:
        userServiceApiImpl.login(userLogInRequestDto)

        then:
        1 * mockUserRepository.findUserByUsernameOrEmailAndPassword(userLogInRequestDto.username, userLogInRequestDto.email, userLogInRequestDto.getPassword()) >> null
        UserNotFoundException e = thrown()
        e.localizedMessage == UserLogInError.INVALID_CREDENTIALS.description
        0 * _

    }

    def "User attempts to log in with an unverified account"() {
        given:
        UserLogInRequestDto userLogInRequestDto = new UserLogInRequestDto(email: 'chrisgavanas@gmail.com', password: 'some password')
        User user = new User(isVerified: false)

        when:
        userServiceApiImpl.login(userLogInRequestDto)

        then:
        1 * mockUserRepository.findUserByUsernameOrEmailAndPassword(userLogInRequestDto.username, userLogInRequestDto.email, userLogInRequestDto.getPassword()) >> user
        EmailUnverifiedException e = thrown()
        e.localizedMessage == UserLogInError.USER_NOT_EMAIL_VERIFIED.description
        0 * _
    }

    def "User logs in successfully"() {
        given:
        UserLogInRequestDto userLogInRequestDto = new UserLogInRequestDto(username: 'chris', email: 'chrisgavanas@gmail.com', password: 'some password')
        User user = new User(userId: 4, isAdmin: false, isVerified: true)
        UUID uuid = UUID.randomUUID()

        when:
        UserLogInResponseDto userLogInResponseDto = userServiceApiImpl.login(userLogInRequestDto)

        then:
        1 * mockUserRepository.findUserByUsernameOrEmailAndPassword(userLogInRequestDto.username, userLogInRequestDto.email, userLogInRequestDto.getPassword()) >> user
        1 * mockAuthenticator.createSession(_) >> { args ->
            assert args[0].userId == user.userId
            assert args[0].isAdmin == false
            return uuid
        }
        with(userLogInResponseDto) {
            userId == user.userId
            authToken == uuid
        }
        0 * _
    }

    def "User tries to register with a username which is already in use"() {
        given:
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto(username: 'chris', email: 'chrisgavanas@gmail.com')
        User user = new User('username': 'chris')

        when:
        userServiceApiImpl.register(userRegisterRequestDto)

        then:
        1 * mockUserRepository.findUserByUsernameOrEmail(userRegisterRequestDto.username, userRegisterRequestDto.email) >> user
        UserAlreadyExistsException e = thrown()
        e.localizedMessage == UserRegisterError.USERNAME_ALREADY_IN_USE.description
        0 * _
    }

    def "User tries to register with an email which is already in use"() {
        given:
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto(username: 'chris', email: 'chrisgavanas@gmail.com')
        User user = new User('email': 'chrisgavanas@gmail.com', username: 'chris2')

        when:
        userServiceApiImpl.register(userRegisterRequestDto)

        then:
        1 * mockUserRepository.findUserByUsernameOrEmail(userRegisterRequestDto.username, userRegisterRequestDto.email) >> user
        UserAlreadyExistsException e = thrown()
        e.localizedMessage == UserRegisterError.EMAIL_ALREADY_USED.description
        0 * _
    }

    def "User registers successfully"() {
        given:
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto()
        User user = new User()
        UserRegisterResponseDto userRegisterResponseDto = new UserRegisterResponseDto()

        when:
        userServiceApiImpl.register(userRegisterRequestDto)

        then:
        1 * mockUserRepository.findUserByUsernameOrEmail(userRegisterRequestDto.username, userRegisterRequestDto.email) >> null
        1 * mockUserMapper.registerRequestToUser(userRegisterRequestDto) >> user
        1 * mockUserRepository.save(user)
        1 * mockUserMapper.userToRegisterResponse(user) >> userRegisterResponseDto
        0 * _
    }

    def "User fails to fetch a user's info as his session has expired"() {
        given:
        Integer userId = 2
        UUID authToken = UUID.randomUUID()

        when:
        userServiceApiImpl.getUser(authToken, userId)

        then:
        1 * mockAuthenticator.getSession(authToken) >> null
        NotAuthenticatedException e = thrown()
        e.localizedMessage == UserError.NOT_AUTHENTICATED.description
        0 * _
    }

    def "User fails to fetch a user's info as he does not exist"() {
        given:
        Integer userId = 2
        UUID authToken = UUID.randomUUID()
        SessionInfo sessionInfo = new SessionInfo(userId, DateTime.now(), false)

        when:
        userServiceApiImpl.getUser(authToken, userId)

        then:
        1 * mockAuthenticator.getSession(authToken) >> sessionInfo
        1 * mockUserRepository.findUserByUserId(userId) >> null
        UserNotFoundException e = thrown()
        e.localizedMessage == UserError.USER_DOES_NOT_EXIST.description
        0 * _
    }

    def "User fails to fetch another user's info as he's not admin"() {
        given:
        Integer userId = 2
        UUID authToken = UUID.randomUUID()
        SessionInfo sessionInfo = new SessionInfo(userId, DateTime.now(), false)
        User user = new User()

        when:
        userServiceApiImpl.getUser(authToken, userId)

        then:
        1 * mockAuthenticator.getSession(authToken) >> sessionInfo
        1 * mockUserRepository.findUserByUserId(userId) >> user
        NotAuthorizedException e = thrown()
        e.localizedMessage == UserError.UNAUTHORIZED.description
        0 * _
    }

    @Unroll
    def "User fetches user's info successfully"() {
        given:
        Integer userId = 2
        UUID authToken = UUID.randomUUID()
        SessionInfo sessionInfo = new SessionInfo(sessionUserId, DateTime.now(), isAdmin)
        User user = new User(userId: userId)
        UserResponseDto userResponseDto = new UserResponseDto()

        when:
        userServiceApiImpl.getUser(authToken, userId)

        then:
        1 * mockAuthenticator.getSession(authToken) >> sessionInfo
        1 * mockUserRepository.findUserByUserId(userId) >> user
        1 * mockUserMapper.userToUserResponse(user) >> userResponseDto
        0 * _

        where:
        sessionUserId | isAdmin
        123           | true
        2             | false

    }

    def "Admin fails to verify a user as his session has expired"() {
        given:
        Integer userId = 2
        UUID authToken = UUID.randomUUID()

        when:
        userServiceApiImpl.getUser(authToken, userId)

        then:
        1 * mockAuthenticator.getSession(authToken) >> null
        NotAuthenticatedException e = thrown()
        e.localizedMessage == UserError.NOT_AUTHENTICATED.description
        0 * _
    }

    def "User fails to verify a user as he is not an admin"() {
        given:
        Integer userId = 2
        UUID authToken = UUID.randomUUID()
        SessionInfo sessionInfo = new SessionInfo(4, DateTime.now(), false)

        when:
        userServiceApiImpl.verifyUser(authToken, userId)

        then:
        1 * mockAuthenticator.getSession(authToken) >> sessionInfo
        NotAuthorizedException e = thrown()
        e.localizedMessage == UserError.UNAUTHORIZED.description
        0 * _
    }

    def "Admin fails to verify a user as he does not exist"() {
        given:
        Integer userId = 2
        UUID authToken = UUID.randomUUID()
        SessionInfo sessionInfo = new SessionInfo(5, DateTime.now(), true)

        when:
        userServiceApiImpl.verifyUser(authToken, userId)

        then:
        1 * mockAuthenticator.getSession(authToken) >> sessionInfo
        1 * mockUserRepository.findUserByUserId(userId) >> null
        UserNotFoundException e = thrown()
        e.localizedMessage == UserError.USER_DOES_NOT_EXIST.description
        0 * _
    }

    def "Admin fails to verify a user as he's already verified"() {
        given:
        Integer userId = 2
        UUID authToken = UUID.randomUUID()
        SessionInfo sessionInfo = new SessionInfo(5, DateTime.now(), true)
        User user = new User(isVerified: true)

        when:
        userServiceApiImpl.verifyUser(authToken, userId)

        then:
        1 * mockAuthenticator.getSession(authToken) >> sessionInfo
        1 * mockUserRepository.findUserByUserId(userId) >> user
        UserAlreadyVerifiedException e = thrown()
        e.localizedMessage == UserError.USER_ALREADY_VERIFIED.description
        0 * _
    }

    def "Admin verifies a user successfully"() {
        given:
        Integer userId = 2
        UUID authToken = UUID.randomUUID()
        SessionInfo sessionInfo = new SessionInfo(5, DateTime.now(), true)
        User user = new User(isVerified: false)

        when:
        userServiceApiImpl.verifyUser(authToken, userId)

        then:
        1 * mockAuthenticator.getSession(authToken) >> sessionInfo
        1 * mockUserRepository.findUserByUserId(userId) >> user
        1 * mockUserRepository.save(user)
        with(user) {
            isVerified == true
        }
        0 * _
    }

    def "User fails to update a non existing user"() {
        given:
        Integer userId = 2
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto()

        when:
        userServiceApiImpl.updateUser(userId, userUpdateRequestDto)

        then:
        1 * mockUserRepository.findUserByUserId(userId) >> null
        UserNotFoundException e = thrown()
        e.localizedMessage == UserError.USER_DOES_NOT_EXIST.description
        0 * _
    }

    def "User fails to change his email because it's already in use"() {
        given:
        Integer userId = 2
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto(email: 'chrisgavanas@gmail.com')
        User user = new User(email: 'chris@gmail.com')

        when:
        userServiceApiImpl.updateUser(userId, userUpdateRequestDto)

        then:
        1 * mockUserRepository.findUserByUserId(userId) >> user
        1 * mockUserRepository.countByEmail(userUpdateRequestDto.email) >> 1
        EmailAlreadyInUseException e = thrown()
        e.localizedMessage == UserError.EMAIL_ALREADY_IN_USE.description
        0 * _
    }

    def "User updates his info successfully"() {
        given:
        Integer userId = 2
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto(email: 'chrisgavanas@gmail.com')
        User user = new User(email: 'chris@gmail.com')
        UserResponseDto userResponseDto = new UserResponseDto()

        when:
        userServiceApiImpl.updateUser(userId, userUpdateRequestDto)

        then:
        1 * mockUserRepository.findUserByUserId(userId) >> user
        1 * mockUserRepository.countByEmail(userUpdateRequestDto.email) >> 0
        1 * mockUserMapper.update(user, userUpdateRequestDto)
        1 * mockUserRepository.save(user)
        1 * mockUserMapper.userToUserResponse(user) >> userResponseDto
        0 * _
    }

    def "User fails to change a password of a non existent user"() {
        given:
        Integer userId = 2
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto()

        when:
        userServiceApiImpl.changePassword(userId, changePasswordRequestDto)

        then:
        1 * mockUserRepository.findUserByUserId(userId) >> null
        UserNotFoundException e = thrown()
        e.localizedMessage == UserError.USER_DOES_NOT_EXIST.description
        0 * _
    }

    def "User fails to change password as he didn't insert correctly his old password"() {
        given:
        Integer userId = 2
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(oldPassword: 'some password')
        User user = new User(password: 'some other password')

        when:
        userServiceApiImpl.changePassword(userId, changePasswordRequestDto)

        then:
        1 * mockUserRepository.findUserByUserId(userId) >> user
        ForbiddenException e = thrown()
        e.localizedMessage == UserError.PASSWORD_MISSMATCH.description
        0 * _
    }

    def "User successfully changes his password"() {
        given:
        Integer userId = 2
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(oldPassword: 'some password', newPassword: 'new password')
        User user = new User(password: 'some password')

        when:
        userServiceApiImpl.changePassword(userId, changePasswordRequestDto)

        then:
        1 * mockUserRepository.findUserByUserId(userId) >> user
        1 * mockUserRepository.save(user)
        with(user) {
            password == changePasswordRequestDto.newPassword
        }
        0 * _
    }

}
