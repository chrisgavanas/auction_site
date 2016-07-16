package com.webapplication.validator.auctionitem

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto
import com.webapplication.dto.user.GeoLocationDto
import com.webapplication.error.auctionitem.AuctionItemError
import com.webapplication.exception.ValidationException
import spock.lang.Specification
import spock.lang.Unroll


class AuctionItemValidatorSpec extends Specification {

    AuctionItemValidator auctionItemValidator

    def setup() {
        auctionItemValidator = new AuctionItemValidator()
    }

    def "User attemtps to create a new auction item with null data"() {
        given:
        AddAuctionItemRequestDto addAuctionItemRequestDto = null

        when:
        auctionItemValidator.validate(addAuctionItemRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.MISSING_DATA.description
    }

    @Unroll
    def "User attempts to create a new auction item with missing data"() {
        given:
        GeoLocationDto geoLocationDto = new GeoLocationDto(latitude: latitude, longitude: longitude)
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto(name: name,
                startDate: startDate, endDate: endDate, userId: userId, minBid: minBid,
                buyout: buyout, geoLocationDto: geoLocationDto, categories: categories)

        when:
        auctionItemValidator.validate(addAuctionItemRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.MISSING_DATA.description

        where:
        name   | startDate  | endDate    | userId | minBid | buyout | latitude | longitude | categories
        null   | new Date() | new Date() | 1      | 0      | 1      | 2        | 3         | [1, 2]
        "name" | null       | new Date() | 1      | 0      | 1      | 2        | 3         | [1, 2]
        "name" | new Date() | null       | 1      | 0      | 1      | 2        | 3         | [1, 2]
        "name" | new Date() | new Date() | 1      | null   | null   | 52.2     | 51.2      | [1, 2]
        "name" | new Date() | new Date() | 1      | 20.1   | 41     | 52.2     | 51.2      | null
        "name" | new Date() | new Date() | 1      | 20.1   | 41     | 52.2     | 51.2      | []
        "name" | new Date() | new Date() | 1      | 20.1   | 41     | null     | null      | [1]
        "name" | new Date() | new Date() | 1      | 20.1   | 41     | 50.12    | null      | [1]
        "name" | new Date() | new Date() | 1      | 20.1   | 41     | null     | 30.1      | [1]
        null   | null       | null       | null   | null   | null   | null     | null      | null
    }

    @Unroll
    def "User attempts to create a new auction item with invalid data"() {
        given:
        GeoLocationDto geoLocationDto = new GeoLocationDto(latitude: latitude, longitude: longitude)
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto(name: name,
                startDate: startDate, endDate: endDate, userId: userId, minBid: minBid,
                buyout: buyout, geoLocationDto: geoLocationDto, categories: categories)

        when:
        auctionItemValidator.validate(addAuctionItemRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.INVALID_DATA.description

        where:
        name   | startDate     | endDate       | userId | minBid | buyout | latitude | longitude | categories
        ""     | new Date(123) | new Date(124) | 1      | 0.5    | 1      | 2        | 3         | [1, 2]
        "name" | new Date(123) | new Date(124) | 0      | 20     | 30     | 52.2     | 51.2      | [1, 2]
        "name" | new Date(123) | new Date(124) | 1      | -1     | 41     | 52.2     | 51.2      | [1, 2]
        "name" | new Date(123) | new Date(124) | 1      | 20.1   | -2     | 52.2     | 51.2      | [1, 2]
        "name" | new Date(123) | new Date(124) | 1      | 20.1   | 24     | 52.2     | 51.2      | [0, 1]
    }

    @Unroll
    def "User attempts to create a new auction item with illogical data"() {
        given:
        GeoLocationDto geoLocationDto = new GeoLocationDto(latitude: latitude, longitude: longitude)
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto(name: name,
                startDate: startDate, endDate: endDate, userId: userId, minBid: minBid,
                buyout: buyout, geoLocationDto: geoLocationDto, categories: categories)

        when:
        auctionItemValidator.validate(addAuctionItemRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.INVALID_DATA.description

        where:
        name   | startDate     | endDate       | userId | minBid | buyout | latitude | longitude | categories
        "name" | new Date(123) | new Date(124) | 1      | 2      | 1      | 2        | 3         | [1, 2]
        "name" | new Date(500) | new Date(200) | 1      | 1      | 2      | 2        | 3         | [1, 2]
        "name" | new Date(123) | new Date(124) | 1      | 1      | 2      | -91      | 3         | [1, 2]
        "name" | new Date(123) | new Date(124) | 1      | 1      | 2      | 92       | 3         | [1, 2]
        "name" | new Date(123) | new Date(124) | 1      | 1      | 2      | 2        | -195      | [1, 2]
        "name" | new Date(123) | new Date(124) | 1      | 1      | 2      | 22       | 182       | [1, 2]
        "name" | new Date(123) | new Date(124) | 1      | 1      | 2      | -91      | 3         | [1, 2]
        "name" | new Date(123) | new Date(124) | 1      | 1      | 2      | 92       | 3         | [1, 2]
    }

    def "User creates a new auction item successfully"() {
        given:
        GeoLocationDto geoLocationDto = new GeoLocationDto(latitude: 20, longitude: 30)
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto(name: "name",
                startDate: new Date(123), endDate: new Date(124), userId: 1, minBid: 10,
                buyout: 10.1, geoLocationDto: geoLocationDto, categories: [1, 2, 3])

        when:
        auctionItemValidator.validate(addAuctionItemRequestDto)

        then:
        ValidationException e = notThrown()
    }
}
