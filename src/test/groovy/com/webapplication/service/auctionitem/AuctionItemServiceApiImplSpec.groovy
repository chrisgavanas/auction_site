package com.webapplication.service.auctionitem

import com.webapplication.dao.AuctionItemRepository
import com.webapplication.dao.UserRepository
import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto
import com.webapplication.dto.auctionitem.AuctionItemResponseDto
import com.webapplication.entity.AuctionItem
import com.webapplication.entity.User
import com.webapplication.error.auctionitem.AuctionItemError
import com.webapplication.exception.UserNotFoundException
import com.webapplication.mapper.AuctionItemMapper
import spock.lang.Specification

class AuctionItemServiceApiImplSpec extends Specification {

    AuctionItemRepository mockAuctionItemRepository
    AuctionItemMapper mockAuctionItemMapper
    UserRepository mockUserRepository
    AuctionItemServiceApi auctionItemServiceImpl

    def setup() {
        mockAuctionItemRepository = Mock(AuctionItemRepository)
        mockAuctionItemMapper = Mock(AuctionItemMapper)
        mockUserRepository = Mock(UserRepository)

        auctionItemServiceImpl = new AuctionItemServiceApiImpl(auctionItemRepository: mockAuctionItemRepository, auctionItemMapper: mockAuctionItemMapper, userRepository: mockUserRepository)
    }

    def "Add an auction item"() {
        given:
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto()
        AuctionItem auctionItem = new AuctionItem(userId: '578f869f5a61a77b7915252a')
        AddAuctionItemResponseDto addAuctionItemResponseDto = new AddAuctionItemResponseDto()

        when:
        auctionItemServiceImpl.addAuctionItem(addAuctionItemRequestDto)

        then:
        1 * mockAuctionItemMapper.addAuctionItemRequestDtoToAuctionItem(addAuctionItemRequestDto) >> auctionItem
        1 * mockUserRepository.findUserByUserId('578f869f5a61a77b7915252a') >> new User()
        1 * mockAuctionItemRepository.save(auctionItem)
        1 * mockAuctionItemMapper.auctionItemToAddAuctionItemResponseDto(auctionItem) >> addAuctionItemResponseDto
        0 * _
    }

    def "Add an auction item from a user that doesn't exit"() {
        given:
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto()
        AuctionItem auctionItem = new AuctionItem(userId: '578f869f5a61a77b7915252a')
        AddAuctionItemResponseDto addAuctionItemResponseDto = new AddAuctionItemResponseDto()

        when:
        auctionItemServiceImpl.addAuctionItem(addAuctionItemRequestDto)

        then:
        1 * mockAuctionItemMapper.addAuctionItemRequestDtoToAuctionItem(addAuctionItemRequestDto) >> auctionItem
        1 * mockUserRepository.findUserByUserId('578f869f5a61a77b7915252a') >> null
        UserNotFoundException e = thrown()
        e.localizedMessage == AuctionItemError.USER_NOT_FOUND.description
    }

    def "Get all auctions of a specific user"() {
        given:
        String userId = '578f869f5a61a77b7915252a'
        List<AuctionItem> auctionItemList = []
        List<AuctionItemResponseDto> auctionItemResponseDtoList = []

        when:
        auctionItemServiceImpl.getAuctionItemsOfUser(userId)

        then:
        1 * mockAuctionItemRepository.findAuctionItemByUserId(userId) >> auctionItemList
        1 * mockAuctionItemMapper.auctionItemsToAuctionItemResponseDto(auctionItemList) >> auctionItemResponseDtoList
    }
}
