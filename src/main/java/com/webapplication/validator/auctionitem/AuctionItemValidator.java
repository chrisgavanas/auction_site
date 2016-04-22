package com.webapplication.validator.auctionitem;


import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.Range;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
@Component
public class AuctionItemValidator implements Validator<AddAuctionItemRequestDto> {

    private Range latitudeRange = new DoubleRange(-90.0, 90.0);
    private Range longitudeRange = new DoubleRange(-180.0, 180.0);

	@Override
	public void validate(AddAuctionItemRequestDto request) throws ValidationException {
        Optional.ofNullable(request).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));

        if (Arrays.asList(request.getName(), request.getStartDate(), request.getEndDate(),
        		request.getUserId()).stream().anyMatch(Objects::isNull))
            throw new ValidationException(AuctionItemError.MISSING_DATA);

        if (Arrays.asList(request.getCurrentBid(), request.getBuyout()).stream()
            .filter(Objects::isNull).count() == 0)
            throw new ValidationException(AuctionItemError.MISSING_DATA);

        Double latitude = request.getLatitude();
        Double longitude = request.getLongitude();
        if ((latitude == null && longitude != null) || (latitude != null && longitude == null))
            throw new ValidationException(AuctionItemError.MISSING_DATA);

        if (request.getName().isEmpty())
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        if (Arrays.asList(request.getCurrentBid(), request.getBuyout()).stream()
                .filter(Objects::nonNull)
                .anyMatch(value -> value <= 0))
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        if (request.getStartDate().after(request.getEndDate()))
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        if (!latitudeRange.containsDouble(latitude) || !longitudeRange.containsDouble(longitude))
            throw new ValidationException(AuctionItemError.INVALID_DATA);

    }

}
