package com.webapplication.validator.auctionitem;

import com.webapplication.dto.auctionitem.StartAuctionDto;
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class StartAuctionValidator implements Validator<StartAuctionDto> {

    @Override
    public void validate(StartAuctionDto request) throws ValidationException {
        Optional.ofNullable(request).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        Optional.ofNullable(request.getEndDate()).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        if (new Date().after(request.getEndDate()))
            throw new ValidationException(AuctionItemError.INVALID_AUCTION_END_DATE);
    }
}
