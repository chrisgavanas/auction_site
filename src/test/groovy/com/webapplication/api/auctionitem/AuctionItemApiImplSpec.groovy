package com.webapplication.api.auctionitem

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto
import com.webapplication.dto.auctionitem.AuctionItemResponseDto
import com.webapplication.dto.auctionitem.Status
import com.webapplication.error.auctionitem.AuctionItemError
import com.webapplication.exception.ValidationException
import com.webapplication.service.auctionitem.AuctionItemServiceApi
import com.webapplication.validator.auctionitem.AuctionItemValidator
import spock.lang.Specification
import spock.lang.Unroll

class AuctionItemApiImplSpec extends Specification {

    AuctionItemApiImpl auctionItemApi
    AuctionItemServiceApi mockAuctionItemService
    AuctionItemValidator mockAuctionItemValidator

    def setup() {
        mockAuctionItemService = Mock(AuctionItemServiceApi)
        mockAuctionItemValidator = Mock(AuctionItemValidator)

        auctionItemApi = new AuctionItemApiImpl(auctionItemService: mockAuctionItemService, auctionItemValidator: mockAuctionItemValidator)
    }

    def "Add auction item successfully"() {
        given:
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto()
        AddAuctionItemResponseDto addAuctionItemResponseDto = new AddAuctionItemResponseDto()

        when:
        auctionItemApi.addAuctionItem(addAuctionItemRequestDto)

        then:
        1 * mockAuctionItemValidator.validate(addAuctionItemRequestDto)
        1 * mockAuctionItemService.addAuctionItem(addAuctionItemRequestDto) >> addAuctionItemResponseDto
        0 * _
    }

    @Unroll
    def "Fails to fetch auction items of a specific user due to invalid userId"() {
        when:
        auctionItemApi.getAuctionItemsOfUserByStatus(userId, status)

        then:
        ValidationException e = thrown()
        e.localizedMessage == error.description

        where:
        userId | status        | error
        null   | Status.ACTIVE | AuctionItemError.MISSING_DATA
        null   | null          | AuctionItemError.MISSING_DATA
        ""     | Status.ACTIVE | AuctionItemError.INVALID_DATA
    }

    def "Get user's auctions sucessfully"() {
        given:
        String userId = "578f9c605a61fe0aa84fe8e5"
        List<AuctionItemResponseDto> auctionItemResponseDtoList = []
        Status status = Status.ACTIVE

        when:
        auctionItemApi.getAuctionItemsOfUserByStatus(userId, status)

        then:
        1 * mockAuctionItemService.getAuctionItemsOfUserByStatus(userId, status) >> auctionItemResponseDtoList
        0 * _
    }

    def "Trying to get an auction by id with missing data"() {
        when:
        auctionItemApi.getAuctionItemById(null)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.MISSING_DATA.description
    }

    def "Trying to get an auction by id with invalid data"() {
        given:
        String auctionItemId = ""

        when:
        auctionItemApi.getAuctionItemById(auctionItemId)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.INVALID_DATA.description
    }

    def "Getting auction by auctionItemId"() {
        given:
        String auctionItemId = "578f9c605a61fe0aa84fe8e5"

        when:
        auctionItemApi.getAuctionItemById(auctionItemId)

        then:
        1 * mockAuctionItemService.getAuctionItemById(auctionItemId)
        0 * _
    }

}
