package com.webapplication.validator.auctionitem

import com.webapplication.dto.auctionitem.AuctionItemUpdateRequestDto
import com.webapplication.dto.user.GeoLocationDto
import com.webapplication.error.auctionitem.AuctionItemError
import com.webapplication.exception.ValidationException
import spock.lang.Specification
import spock.lang.Unroll


class AuctionItemUpdateRequestValidatorSpec extends Specification {

    AuctionItemUpdateRequestValidator auctionItemUpdateRequestValidator

    def setup() {
        auctionItemUpdateRequestValidator = new AuctionItemUpdateRequestValidator()
    }

    def "Update auction with null data"() {
        given:
        AuctionItemUpdateRequestDto auctionItemUpdateRequestDto = null

        when:
        auctionItemUpdateRequestValidator.validate(auctionItemUpdateRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.MISSING_DATA.description
    }

    @Unroll
    def "Update auction with missing data"() {
        given:
        GeoLocationDto geoLocationDto = new GeoLocationDto(latitude: latitude, longitude: longitude)
        AuctionItemUpdateRequestDto auctionItemUpdateRequestDto = new AuctionItemUpdateRequestDto(
                buyout: buyout, description: description, minBid: minBid, name: name,
                geoLocationDto: geoLocationDto, categoryIds: categories
        )

        when:
        auctionItemUpdateRequestValidator.validate(auctionItemUpdateRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.MISSING_DATA.description

        where:
        buyout | minBid | description        | name     | latitude | longitude | categories
        null   | null   | 'Some description' | 'A name' | 20.2     | 31.1      | ['Category1', 'Category2']
        20.1   | 19     | null               | 'A name' | 20.2     | 31.1      | ['Category1', 'Category2']
        20.1   | 19     | 'Some description' | null     | 20.2     | 31.1      | ['Category1', 'Category2']
        20.1   | 19     | 'Some description' | 'A name' | null     | 31.1      | ['Category1', 'Category2']
        20.1   | 19     | 'Some description' | 'A name' | 14.4     | null      | ['Category1', 'Category2']
        20.1   | 19     | 'Some description' | 'A name' | 14.4     | 140       | null
        null   | 19     | 'Some description' | 'A name' | 14.4     | 140       | null
        null   | 19     | 'Some description' | null     | 14.4     | 140       | null
        null   | null   | null               | null     | null     | null      | null
    }

    @Unroll
    def "Update auction with invalid data"() {
        given:
        GeoLocationDto geoLocationDto = new GeoLocationDto(latitude: latitude, longitude: longitude)
        AuctionItemUpdateRequestDto auctionItemUpdateRequestDto = new AuctionItemUpdateRequestDto(
                buyout: buyout, description: description, minBid: minBid, name: name,
                geoLocationDto: geoLocationDto, categoryIds: categories
        )

        when:
        auctionItemUpdateRequestValidator.validate(auctionItemUpdateRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.INVALID_DATA.description

        where:
        buyout | minBid | description        | name     | latitude | longitude | categories
        100    | 120    | 'Some description' | 'A name' | 20.1     | 41.2      | ['Category1', 'Category2']
        100    | 80     | ''                 | 'A name' | 20.1     | 41.2      | ['Category1', 'Category2']
        100    | 80     | 'Some description' | ''       | 20.1     | 41.2      | ['Category1', 'Category2']
        100    | 80     | 'Some description' | 'A name' | 92       | 41.2      | ['Category1', 'Category2']
        100    | 80     | 'Some description' | 'A name' | 44       | -181      | ['Category1', 'Category2']
        100    | 80     | 'Some description' | 'A name' | 44       | -180      | ['', 'Category2']
        100    | 80     | ''                 | ''       | 44       | 180       | ['Category1', 'Category2']
        100    | 123    | ''                 | 'A name' | 44       | 180       | ['Category1', 'Category2']

    }
}
