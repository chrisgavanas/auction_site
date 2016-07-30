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
import org.springframework.data.domain.PageRequest
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
        ForbiddenException e = thrown()
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
        String userId = '578f869f5a61a77b7915252a'
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
        String userId = '578f869f5a61a77b7915252a'
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
        String userId = '578f869f5a61a77b79123123b'
        UUID authToken = UUID.randomUUID()
        SessionInfo sessionInfo = new SessionInfo('578f869f5a61a77b7915252a', DateTime.now(), false)
        User user = new User(userId: userId)

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
        String userId = '578f869f5a61a77b79123123b'
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
        sessionUserId               | isAdmin
        '578f869f5a61a77b7918fa81c' | true
        '578f869f5a61a77b79123123b' | false

    }

    def "Admin fails to verify a user as his session has expired"() {
        given:
        String userId = '578f869f5a61a77b79123123b'
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
        String userId = '578f869f5a61a77b79123123b'
        UUID authToken = UUID.randomUUID()
        SessionInfo sessionInfo = new SessionInfo('578f869f5a61a77b79fffaff', DateTime.now(), false)

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
        String userId = '578f869f5a61a77b79123123b'
        UUID authToken = UUID.randomUUID()
        SessionInfo sessionInfo = new SessionInfo('578f869f5a61a77b79ffffff', DateTime.now(), true)

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
        String userId = '578f869f5a61a77b79123123b'
        UUID authToken = UUID.randomUUID()
        SessionInfo sessionInfo = new SessionInfo('578f869f5a61a77b79ffffff', DateTime.now(), true)
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
        String userId = '578f869f5a61a77b79123123b'
        UUID authToken = UUID.randomUUID()
        SessionInfo sessionInfo = new SessionInfo('578f869f5a61a77b79ffffff', DateTime.now(), true)
        User user = new User(isVerified: false)

        when:
        userServiceApiImpl.verifyUser(authToken, userId)

        then:
        1 * mockAuthenticator.getSession(authToken) >> sessionInfo
        1 * mockUserRepository.findUserByUserId(userId) >> user
        1 * mockUserRepository.save(user)
        with(user) {
            isVerified
        }
        0 * _
    }

    def "User fails to update a non existing user"() {
        given:
        String userId = '578f869f5a61a77b79123123b'
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
        String userId = '578f869f5a61a77b79123123b'
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
        String userId = '578f869f5a61a77b79123123b'
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
        String userId = '578f869f5a61a77b79123123b'
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
        String userId = '578f869f5a61a77b79123123b'
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
        String userId = '578f869f5a61a77b79123123b'
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

    def "Not logged in user without admin privileges asks for unverified users"() {
        given:
        UUID authToken = UUID.randomUUID()
        Integer from = 1
        Integer to = 2

        when:
        userServiceApiImpl.getUnverifiedUsers(authToken, from, to)

        then:
        1 * mockAuthenticator.getSession(authToken) >> null
        NotAuthenticatedException e = thrown()
        e.localizedMessage == UserError.NOT_AUTHENTICATED.description
    }

    def "Logged in user without admin privileges asks for unverified users"() {
        given:
        UUID authToken = UUID.randomUUID()
        Integer from = 1
        Integer to = 2
        SessionInfo sessionInfo = new SessionInfo('578f869f5a61a77b79123123b', DateTime.now(), false)

        when:
        userServiceApiImpl.getUnverifiedUsers(authToken, from, to)

        then:
        1 * mockAuthenticator.getSession(authToken) >> sessionInfo
        NotAuthorizedException e = thrown()
        e.localizedMessage == UserError.UNAUTHORIZED.description
    }

    def "Admin requests for a specific number of unverified users"() {
        given:
        UUID authToken = UUID.randomUUID()
        Integer from = 1
        Integer to = 2
        List<User> userList = [new User(userId: 1), new User(userId: 2)]
        SessionInfo sessionInfo = new SessionInfo('578f869f5a61a77b79123123b', DateTime.now(), true)

        when:
        List<UserResponseDto> userResponseDtoList = userServiceApiImpl.getUnverifiedUsers(authToken, from, to)

        then:
        1 * mockAuthenticator.getSession(authToken) >> sessionInfo
        1 * mockUserRepository.findUserByIsVerified(false, new PageRequest(from - 1, to - from + 1)) >> userList
        1 * mockUserMapper.userListToUserResponseList(userList) >> { args ->
            assert args[0][0].userId == userList[0].userId
            assert args[0][1].userId == userList[1].userId
            return [new UserResponseDto(userId: '578f869f5a61a77b79123123b'), new UserResponseDto(userId: '578f869f5a61a77b79ffffff')]
        }
        with(userResponseDtoList[0]) {
            userId == '578f869f5a61a77b79123123b'
        }
        with(userResponseDtoList[1]) {
            userId == '578f869f5a61a77b79ffffff'
        }
    }

}
