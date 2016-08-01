package com.webapplication.validator.auctionitem

import com.webapplication.dto.auctionitem.StartAuctionDto
import com.webapplication.error.auctionitem.AuctionItemError
import com.webapplication.exception.ValidationException
import org.joda.time.DateTime
import spock.lang.Specification
import spock.lang.Unroll

class StartAuctionValidatorSpec extends Specification {

    StartAuctionValidator startAuctionValidator

    def setup() {
        startAuctionValidator = new StartAuctionValidator()
    }

    @Unroll
    def "Validating null data"() {
        when:
        startAuctionValidator.validate(startAuctionDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.MISSING_DATA.description

        where:
        startAuctionDto << [null, new StartAuctionDto()]
    }

    def "Validating invalid date in the request"() {
        given:
        StartAuctionDto startAuctionDto = new StartAuctionDto(endDate: DateTime.now().minusSeconds(10).toDate())

        when:
        startAuctionValidator.validate(startAuctionDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.INVALID_AUCTION_END_DATE.description
    }

    def "Validating request sucessfully"() {
        given:
        StartAuctionDto startAuctionDto = new StartAuctionDto(endDate:  DateTime.now().plusSeconds(10).toDate())

        when:
        startAuctionValidator.validate(startAuctionDto)

        then:
        ValidationException e = notThrown()
    }

}
