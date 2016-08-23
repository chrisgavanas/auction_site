package com.webapplication.api.auctionitem

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto
import com.webapplication.dto.auctionitem.AuctionItemResponseDto
import com.webapplication.dto.auctionitem.StartAuctionDto
import com.webapplication.dto.auctionitem.AuctionStatus
import com.webapplication.error.auctionitem.AuctionItemError
import com.webapplication.exception.ValidationException
import com.webapplication.service.auctionitem.AuctionItemServiceApi
import com.webapplication.validator.auctionitem.AuctionItemRequestValidator
import spock.lang.Specification
import spock.lang.Unroll

class AuctionItemApiImplSpec extends Specification {

    AuctionItemApiImpl auctionItemApi
    AuctionItemServiceApi mockAuctionItemService
    AuctionItemRequestValidator mockAuctionItemRequestValidator

    def setup() {
        mockAuctionItemService = Mock(AuctionItemServiceApi)
        mockAuctionItemRequestValidator = Mock(AuctionItemRequestValidator)

        auctionItemApi = new AuctionItemApiImpl(auctionItemService: mockAuctionItemService, auctionItemRequestValidator: mockAuctionItemRequestValidator)
    }

    def "Add auction item successfully"() {
        given:
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto()
        AddAuctionItemResponseDto addAuctionItemResponseDto = new AddAuctionItemResponseDto()

        when:
        auctionItemApi.addAuctionItem(addAuctionItemRequestDto)

        then:
        1 * mockAuctionItemRequestValidator.validate(addAuctionItemRequestDto)
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
        userId | status               | error
        null   | AuctionStatus.ACTIVE | AuctionItemError.MISSING_DATA
        null   | null                 | AuctionItemError.MISSING_DATA
        ""     | AuctionStatus.ACTIVE | AuctionItemError.INVALID_DATA
    }

    def "Get user's auctions sucessfully"() {
        given:
        String userId = "578f9c605a61fe0aa84fe8e5"
        List<AuctionItemResponseDto> auctionItemResponseDtoList = []
        AuctionStatus status = AuctionStatus.ACTIVE

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

    def "User tries to start his auction with missing auctionItemId"() {
        given:
        String auctionItemId = null
        StartAuctionDto startAuctionDto = new StartAuctionDto()

        when:
        auctionItemApi.startAuction(auctionItemId, startAuctionDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.MISSING_DATA.description
    }

    def "User tries to start his auction with invalid auctionItemId"() {
        given:
        String auctionItemId = ''
        StartAuctionDto startAuctionDto = new StartAuctionDto()

        when:
        auctionItemApi.startAuction(auctionItemId, startAuctionDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == AuctionItemError.INVALID_DATA.description
    }

    def "User starts his auction"() {
        given:
        String auctionItemId = '578f9c605a61fe0aa84fe8e5'
        StartAuctionDto startAuctionDto = new StartAuctionDto()

        when:
        auctionItemApi.startAuction(auctionItemId, startAuctionDto)

        then:
        1 * mockAuctionItemRequestValidator.validate(startAuctionDto)
        1 * mockAuctionItemService.startAuction(auctionItemId, startAuctionDto)
        0 * _
    }


}
