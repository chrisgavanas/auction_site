package com.webapplication.service.auctionitem

import com.webapplication.dao.AuctionItemRepository
import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto
import com.webapplication.dto.auctionitem.AuctionItemResponseDto
import com.webapplication.entity.Auctionitem
import com.webapplication.mapper.AuctionItemMapper
import spock.lang.Specification

class AuctionItemServiceApiImplSpec extends Specification {

    AuctionItemRepository mockAuctionItemRepository
    AuctionItemMapper mockAuctionItemMapper
    AuctionItemServiceApi auctionItemServiceImpl

    def setup() {
        mockAuctionItemRepository = Mock(AuctionItemRepository)
        mockAuctionItemMapper = Mock(AuctionItemMapper)

        auctionItemServiceImpl = new AuctionItemServiceApiImpl(auctionItemRepository: mockAuctionItemRepository, auctionItemMapper: mockAuctionItemMapper)
    }

    def "Add an auction item"() {
        given:
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto()
        Auctionitem auctionitem = new Auctionitem()
        AddAuctionItemResponseDto addAuctionItemResponseDto = new AddAuctionItemResponseDto()

        when:
        auctionItemServiceImpl.addAuctionItem(addAuctionItemRequestDto)

        then:
        1 * mockAuctionItemMapper.addAuctionItemRequestDtoToAuctionItem(addAuctionItemRequestDto) >> auctionitem
        1 * mockAuctionItemRepository.save(auctionitem)
        1 * mockAuctionItemMapper.auctionItemToAddAuctionItemResponseDto(auctionitem) >> addAuctionItemResponseDto
        0 * _
    }

    def "Get all auctions of a specific user"() {
        given:
        Integer userId = 5
        List<Auctionitem> auctionitemList = []
        List<AuctionItemResponseDto> auctionItemResponseDtoList = []

        when:
        auctionItemServiceImpl.getAuctionItemsOfUser(userId)

        then:
        1 * mockAuctionItemRepository.findAuctionitemByUser(userId) >> auctionitemList
        1 * mockAuctionItemMapper.auctionItemsToAuctionItemResponseDto(auctionitemList) >> auctionItemResponseDtoList
    }
}
