package com.webapplication.api.auctionitem

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto
import com.webapplication.dto.auctionitem.AuctionItemResponseDto
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
        auctionItemApi.getAuctionItemsOfUser(userId)

        then:
        ValidationException e = thrown()
        e.localizedMessage == error.description

        where:
        userId | error
        null   | AuctionItemError.MISSING_DATA
        -1     | AuctionItemError.INVALID_DATA
        0      | AuctionItemError.INVALID_DATA
    }

    def "Get user's auctions sucessfully"() {
        given:
        Integer userId = 1
        List<AuctionItemResponseDto> auctionItemResponseDtoList = []

        when:
        auctionItemApi.getAuctionItemsOfUser(userId)

        then:
        1 * mockAuctionItemService.getAuctionItemsOfUser(userId) >> auctionItemResponseDtoList
        0 * _
    }

}
