package com.webapplication.mapper

import com.webapplication.dao.CategoryRepository
import com.webapplication.dao.ImageRepository
import com.webapplication.dao.UserRepository
import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto
import com.webapplication.dto.auctionitem.AuctionItemResponseDto
import com.webapplication.dto.user.GeoLocationDto
import com.webapplication.entity.Auctionitem
import com.webapplication.entity.Category
import com.webapplication.entity.User
import spock.lang.Specification

class AuctionItemMapperSpec extends Specification {

    CategoryMapper mockCategoryMapper
    UserRepository mockUserRepository
    CategoryRepository mockCategoryRepository
    ImageRepository mockImageRepository
    AuctionItemMapper auctionItemMapper

    def setup() {
        mockCategoryMapper = Mock(CategoryMapper)
        mockUserRepository = Mock(UserRepository)
        mockCategoryRepository = Mock(CategoryRepository)
        mockImageRepository = Mock(ImageRepository)

        auctionItemMapper = new AuctionItemMapper(categoryMapper: mockCategoryMapper, userRepository: mockUserRepository,
                categoryRepository: mockCategoryRepository, imageRepository: mockImageRepository)
    }

    def "Convert AddAuctionItemRequestDto to Auctionitem with null data"() {
        given:
        AddAuctionItemResponseDto addAuctionItemResponseDto = null

        when:
        Auctionitem auctionitem = auctionItemMapper.addAuctionItemRequestDtoToAuctionItem(addAuctionItemResponseDto)

        then:
        auctionitem == null
    }

    def "Convert Auctionitem to AddAuctionItemResponseDto with null data"() {
        given:
        Auctionitem auctionitem = null

        when:
        AddAuctionItemResponseDto addAuctionItemResponseDto = auctionItemMapper.auctionItemToAddAuctionItemResponseDto(auctionitem)

        then:
        addAuctionItemResponseDto == null
    }

    def "Convert a list of Auctionitems to a list of AuctionItemResponseDtos with null data"() {
        given:
        List<Auctionitem> auctionitems = null

        when:
        List<AuctionItemResponseDto> auctionItemResponseDtos = auctionItemMapper.auctionItemsToAuctionItemResponseDto(auctionitems)

        then:
        auctionItemResponseDtos == []
        0 * _
    }

    def "Convert AddAucitonItemRequestDto to Auctionitem"() {
        given:
        GeoLocationDto geoLocationDto = new GeoLocationDto(latitude: 15.2, longitude: 123.12341)
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto(name: 'An auction',
                minBid: 20.1, buyout: 40, startDate: new Date(), endDate: new Date(),
                description: "some description", geoLocationDto: geoLocationDto, userId: 5, categories: [1, 2, 3])

        when:
        Auctionitem auctionitem = auctionItemMapper.addAuctionItemRequestDtoToAuctionItem(addAuctionItemRequestDto)

        then:
        1 * mockUserRepository.findUserByUserId(addAuctionItemRequestDto.userId)
        1 * mockCategoryRepository.findAll(addAuctionItemRequestDto.categories)
        with(auctionitem) {
            bidsNo == 0
            buyout == addAuctionItemRequestDto.buyout
            currentBid == 0
            description == addAuctionItemRequestDto.description
            startDate == addAuctionItemRequestDto.startDate
            endDate == addAuctionItemRequestDto.endDate
            latitude == geoLocationDto.latitude
            longitude == geoLocationDto.longitude
            minBid == addAuctionItemRequestDto.minBid
            name == addAuctionItemRequestDto.name
        }
        0 * _
    }

    def "Convert Auctionitem to AddAuctionItemResponseDto"() {
        given:
        Auctionitem auctionitem = new Auctionitem(auctionItemId: 1, bidsNo: 12, buyout: 150,
                currentBid: 44.2, description: 'Some description', latitude: 12.51, longitude: -20.11,
                minBid: 10.0, name: 'PokemonGoApk', startDate: new Date(), endDate: new Date(),
                user: new User(userId: 5), categories: [new Category(categoryId: 1), new Category(categoryId: 2)])

        when:
        AddAuctionItemResponseDto addAuctionItemResponseDto = auctionItemMapper.auctionItemToAddAuctionItemResponseDto(auctionitem)

        then:
        with(addAuctionItemResponseDto) {
            auctionItemId == auctionitem.auctionItemId
            name == auctionitem.name
            currentBid == auctionitem.currentBid
            buyout == auctionitem.buyout
            minBid == auctionitem.minBid
            bidsNo == auctionitem.bidsNo
            startDate == auctionitem.startDate
            endDate == auctionitem.endDate
            description == auctionitem.description
            geoLocationDto.latitude == auctionitem.latitude
            geoLocationDto.longitude == auctionitem.longitude
            userId == auctionitem.user.userId
            categories.size == auctionitem.categories.size
        }
        0 * _
    }

    def "Convert a list of Auctionitems to a list of AuctionItemResponseDtos"() {
        given:
        List<Category> categoryLists = [
                [new Category(categoryId: 1), new Category(categoryId: 2)],
                [new Category(categoryId: 1), new Category(categoryId: 3)]
        ]
        List<Auctionitem> auctionitems = [
                new Auctionitem(auctionItemId: 1, bidsNo: 12, buyout: 150,
                        currentBid: 44.2, description: 'Some description', latitude: 12.51, longitude: -20.11,
                        minBid: 10.0, name: 'PokemonGoApk', startDate: new Date(), endDate: new Date(),
                        user: new User(userId: 5), categories: categoryLists[0]),
                new Auctionitem(auctionItemId: 2, bidsNo: 3, buyout: 1400,
                        currentBid: 12.5, description: 'Another description', latitude: 90, longitude: 180,
                        minBid: 10.0, name: 'DigimonApk', startDate: new Date(), endDate: new Date(),
                        user: null, categories: categoryLists[1]),
                null,
                null
        ]

        when:
        List<AuctionItemResponseDto> auctionItemResponseDtos = auctionItemMapper.auctionItemsToAuctionItemResponseDto(auctionitems)

        then:
        1 * mockCategoryMapper.categoryListToCategoryResponseDto(categoryLists[0])
        1 * mockCategoryMapper.categoryListToCategoryResponseDto(categoryLists[1])
        with (auctionItemResponseDtos[0]) {
            auctionItemId == auctionitems[0].auctionItemId
            bidsNo == auctionitems[0].bidsNo
            buyout == auctionitems[0].buyout
            currentBid == auctionitems[0].currentBid
            description == auctionitems[0].description
            geoLocationDto.latitude == auctionitems[0].latitude
            geoLocationDto.longitude == auctionitems[0].longitude
            minBid == auctionitems[0].minBid
            name == auctionitems[0].name
            startDate == auctionitems[0].startDate
            endDate == auctionitems[0].endDate
            userId == auctionitems[0].user.userId
        }
        with (auctionItemResponseDtos[1]) {
            auctionItemId == auctionitems[1].auctionItemId
            bidsNo == auctionitems[1].bidsNo
            buyout == auctionitems[1].buyout
            currentBid == auctionitems[1].currentBid
            description == auctionitems[1].description
            geoLocationDto.latitude == auctionitems[1].latitude
            geoLocationDto.longitude == auctionitems[1].longitude
            minBid == auctionitems[1].minBid
            name == auctionitems[1].name
            startDate == auctionitems[1].startDate
            endDate == auctionitems[1].endDate
            userId == null
        }
        0 * _
    }

}