package com.webapplication.validator.auctionitem;


import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class AuctionItemValidator implements Validator<AddAuctionItemRequestDto> {

	@Override
	public void validate(AddAuctionItemRequestDto request) throws ValidationException {
        Optional.ofNullable(request).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));

        if (Arrays.asList(request.getName(), request.getStartDate(), request.getEndDate(),
        		request.getUserId()).stream().anyMatch(Objects::isNull))
            throw new ValidationException(AuctionItemError.MISSING_DATA);
        		
        if (request.getName().isEmpty())
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        if (Arrays.asList(request.getCurrentBid(), request.getBuyout()).stream()
                .filter(Objects::nonNull)
                .anyMatch(value -> value <= 0))
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        if (request.getStartDate().after(request.getEndDate()))
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        if (request.getLatitude() != null && request.getLongitude() != null)
            if (request.getLatitude() > 90 || request.getLatitude() < -90
                    || request.getLongitude() > 180 || request.getLongitude() < -180)
                throw new ValidationException(AuctionItemError.INVALID_DATA);

    }

}
