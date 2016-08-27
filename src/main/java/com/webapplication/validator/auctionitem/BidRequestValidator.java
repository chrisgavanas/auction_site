package com.webapplication.validator.auctionitem;


import com.webapplication.dto.auctionitem.BidRequestDto;
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class BidRequestValidator implements Validator<BidRequestDto> {

    @Override
    public void validate(BidRequestDto request) throws ValidationException {
        Optional.ofNullable(request).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        if (Stream.of(request.getAmount(), request.getUserId()).anyMatch(Objects::isNull))
            throw new ValidationException(AuctionItemError.MISSING_DATA);

        if (request.getAmount() <= 0)
            throw new ValidationException(AuctionItemError.INVALID_DATA);
    }

}
