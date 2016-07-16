package com.webapplication.validator.user

import com.webapplication.dto.user.ChangePasswordRequestDto
import com.webapplication.error.user.UserError
import com.webapplication.exception.ValidationException
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


class ChangePasswordValidatorSpec extends Specification {

    @Shared
    UUID uuid = UUID.randomUUID()
    ChangePasswordValidator changePasswordValidator

    def setup() {
        changePasswordValidator = new ChangePasswordValidator()
    }

    def "User requests for a password with null data"() {
        given:
        ChangePasswordRequestDto changePasswordRequestDto = null

        when:
        changePasswordValidator.validate(changePasswordRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserError.MISSING_DATA.description
    }

    @Unroll
    def "User requests for a password change with missing data"() {
        given:
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(oldPassword: oldPassword,
                newPassword: newPassword, authToken: authToken)

        when:
        changePasswordValidator.validate(changePasswordRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserError.MISSING_DATA.description

        where:
        oldPassword | newPassword | authToken
        null        | "newPass"   | uuid
        "oldPass"   | null        | uuid
        "oldPass"   | "newPass"   | null
        null        | null        | uuid
        null        | "newPass"   | null
        "oldPass"   | null        | null
        null        | null        | null
    }

    @Unroll
    def "User requests for a password change with empty data"() {
        given:
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(oldPassword: oldPassword,
                newPassword: newPassword, authToken: authToken)

        when:
        changePasswordValidator.validate(changePasswordRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserError.INVALID_DATA.description

        where:
        oldPassword | newPassword | authToken
        ""          | "newPass"   | uuid
        "oldPass"   | ""          | uuid
        ""          | ""          | uuid
    }

    def "User requests for a password change with the new password to be the same with the old one"() {
        given:
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(oldPassword: "A password",
                newPassword: "A password", authToken: uuid)

        when:
        changePasswordValidator.validate(changePasswordRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserError.NEW_PASSWORD_DO_NOT_DIFFER.description
    }

    def "User requests for a password change successfully"() {
        given:
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(oldPassword: "A password",
                newPassword: "Another password", authToken: uuid)

        when:
        changePasswordValidator.validate(changePasswordRequestDto)

        then:
        ValidationException e = notThrown()
    }
}
