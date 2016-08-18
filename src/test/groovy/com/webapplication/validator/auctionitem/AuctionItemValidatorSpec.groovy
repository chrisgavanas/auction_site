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
                userId: userId, minBid: minBid, buyout: buyout, geoLocationDto: geoLocationDto,
                categoryIds: categories)

        when:
        auctionItemValidator.validate(addAuctionItemRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.MISSING_DATA.description

        where:
        name   | userId                       | minBid | buyout | latitude | longitude | categories
        null   | ['578f869f5a61a77b7915252a'] | 0      | 1      | 2        | 3         | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | ['578f869f5a61a77b7915252a'] | null   | null   | 52.2     | 51.2      | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | ['578f869f5a61a77b7915252a'] | 20.1   | 41     | 52.2     | 51.2      | null
        "name" | ['578f869f5a61a77b7915252a'] | 20.1   | 41     | null     | null      | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | ['578f869f5a61a77b7915252a'] | 20.1   | 41     | 50.12    | null      | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | ['578f869f5a61a77b7915252a'] | 20.1   | 41     | null     | 30.1      | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        null   | null                         | null   | null   | null     | null      | null
    }

    @Unroll
    def "User attempts to create a new auction item with invalid data"() {
        given:
        GeoLocationDto geoLocationDto = new GeoLocationDto(latitude: latitude, longitude: longitude)
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto(name: name,
                userId: userId, minBid: minBid, buyout: buyout, geoLocationDto: geoLocationDto,
                categoryIds: categories, description: description)

        when:
        auctionItemValidator.validate(addAuctionItemRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.INVALID_DATA.description

        where:
        name   | startDate     | endDate       | userId                     | minBid | description   | buyout | latitude | longitude | categories
        ""     | new Date(123) | new Date(124) | '578f869f5a61a77b7915252a' | 0.5    | 'description' | 1      | 2        | 3         | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | new Date(123) | new Date(124) | ''                         | 20     | 'description' | 30     | 52.2     | 51.2      | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | new Date(123) | new Date(124) | '578f869f5a61a77b7915252a' | -1     | 'description' | 41     | 52.2     | 51.2      | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | new Date(123) | new Date(124) | '578f869f5a61a77b7915252a' | 20.1   | 'description' | -2     | 52.2     | 51.2      | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | new Date(123) | new Date(124) | '578f869f5a61a77b7915252a' | 20.1   | 'description' | 24     | 52.2     | 51.2      | ['', '578f8a542e5a3a48cffadsf21']
        "name" | new Date(123) | new Date(124) | '578f869f5a61a77b7915252a' | 20.1   | ''            | -2     | 52.2     | 51.2      | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
    }

    @Unroll
    def "User attempts to create a new auction item with illogical data"() {
        given:
        GeoLocationDto geoLocationDto = new GeoLocationDto(latitude: latitude, longitude: longitude)
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto(name: name,
                userId: userId, minBid: minBid, buyout: buyout, geoLocationDto: geoLocationDto,
                categoryIds: categories, description: description)

        when:
        auctionItemValidator.validate(addAuctionItemRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.INVALID_DATA.description

        where:
        name   | startDate     | endDate       | userId                     | description   | minBid | buyout | latitude | longitude | categories
        "name" | new Date(123) | new Date(124) | '578f869f5a61a77b7915252a' | 'description' | 2      | 1      | 2        | 3         | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | new Date(123) | new Date(124) | '578f869f5a61a77b7915252a' | 'description' | 1      | 2      | -91      | 3         | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | new Date(123) | new Date(124) | '578f869f5a61a77b7915252a' | 'description' | 1      | 2      | 92       | 3         | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | new Date(123) | new Date(124) | '578f869f5a61a77b7915252a' | 'description' | 1      | 2      | 2        | -195      | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | new Date(123) | new Date(124) | '578f869f5a61a77b7915252a' | 'description' | 1      | 2      | 22       | 182       | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | new Date(123) | new Date(124) | '578f869f5a61a77b7915252a' | 'description' | 1      | 2      | -91      | 3         | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
        "name" | new Date(123) | new Date(124) | '578f869f5a61a77b7915252a' | 'description' | 1      | 2      | 92       | 3         | ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21']
    }

    def "User creates a new auction item successfully"() {
        given:
        GeoLocationDto geoLocationDto = new GeoLocationDto(latitude: 20, longitude: 30)
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto(name: "name",
                userId: 1, minBid: 10, description: 'description', buyout: 10.1, geoLocationDto: geoLocationDto,
                categoryIds: ['578f8a542e5a3a48cfbfb070', '578f8a542e5a3a48cffadsf21'])

        when:
        auctionItemValidator.validate(addAuctionItemRequestDto)

        then:
        ValidationException e = notThrown()
    }
}
