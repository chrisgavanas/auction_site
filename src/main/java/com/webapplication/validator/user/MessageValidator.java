package com.webapplication.validator.user;


import com.google.common.base.Strings;
import com.webapplication.dto.user.MessageRequestDto;
import com.webapplication.error.user.UserError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class MessageValidator implements Validator<MessageRequestDto> {

    @Override
    public void validate(MessageRequestDto messageRequestDto) throws ValidationException {
        Optional.ofNullable(messageRequestDto).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (Stream.of(messageRequestDto.getSubject(), messageRequestDto.getMessage(), messageRequestDto.getFrom(), messageRequestDto.getTo()).anyMatch(Objects::isNull))
            throw new ValidationException(UserError.MISSING_DATA);

        if (Stream.of(messageRequestDto.getSubject(), messageRequestDto.getMessage(), messageRequestDto.getFrom(), messageRequestDto.getTo()).anyMatch(Strings::isNullOrEmpty))
            throw new ValidationException(UserError.MISSING_DATA);
    }

}
