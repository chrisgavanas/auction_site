package com.webapplication.mapper

import com.webapplication.dao.CategoryRepository
import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto
import com.webapplication.dto.auctionitem.AuctionItemResponseDto
import com.webapplication.dto.category.CategoryResponseDto
import com.webapplication.dto.user.GeoLocationDto
import com.webapplication.entity.AuctionItem
import com.webapplication.entity.Category
import com.webapplication.entity.GeoLocation
import com.webapplication.error.category.CategoryError
import com.webapplication.exception.CategoryNotFoundException
import spock.lang.Specification

class AuctionItemMapperSpec extends Specification {

    CategoryMapper mockCategoryMapper
    CategoryRepository mockCategoryRepository
    AuctionItemMapper auctionItemMapper

    def setup() {
        mockCategoryMapper = Mock(CategoryMapper)
        mockCategoryRepository = Mock(CategoryRepository)

        auctionItemMapper = new AuctionItemMapper(categoryMapper: mockCategoryMapper, categoryRepository: mockCategoryRepository)
    }

    def "Convert AddAuctionItemRequestDto to AuctionItem with null data"() {
        given:
        AddAuctionItemRequestDto addAuctionItemRequestDto = null

        when:
        AuctionItem auctionItem = auctionItemMapper.addAuctionItemRequestDtoToAuctionItem(addAuctionItemRequestDto)

        then:
        auctionItem == null
    }

    def "Convert AuctionItem to AddAuctionItemResponseDto with null data"() {
        given:
        AuctionItem auctionItem = null

        when:
        AddAuctionItemResponseDto addAuctionItemResponseDto = auctionItemMapper.auctionItemToAddAuctionItemResponseDto(auctionItem)

        then:
        addAuctionItemResponseDto == null
    }

    def "Convert a list of AuctionItems to a list of AuctionItemResponseDto with null data"() {
        given:
        List<AuctionItem> auctionItemList = null

        when:
        List<AuctionItemResponseDto> auctionItemResponseDtos = auctionItemMapper.auctionItemsToAuctionItemResponseDto(auctionItemList)

        then:
        auctionItemResponseDtos == []
        0 * _
    }

    def "Convert AddAuctionItemRequestDto to AuctionItem with invalid categories"() {
        given:
        GeoLocationDto geoLocationDto = new GeoLocationDto(latitude: 15.2, longitude: 123.12341)
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto(name: 'An auction',
                minBid: 20.1, buyout: 40, startDate: new Date(), endDate: new Date(),
                description: "some description", geoLocationDto: geoLocationDto, userId: 5, categories: ["578f8a542e5a3a48cfbfb070", "578f8a542e5a3a48ca3a3124"])

        when:
        AuctionItem auctionItem = auctionItemMapper.addAuctionItemRequestDtoToAuctionItem(addAuctionItemRequestDto)

        then:
        1 * mockCategoryRepository.findCategoriesByIds(addAuctionItemRequestDto.categories) >> [new Category(), new Category()]
        with(auctionItem) {
            bidsNo == 0
            buyout == addAuctionItemRequestDto.buyout
            currentBid == addAuctionItemRequestDto.minBid
            description == addAuctionItemRequestDto.description
            startDate == addAuctionItemRequestDto.startDate
            endDate == addAuctionItemRequestDto.endDate
            geoLocation.latitude == geoLocationDto.latitude
            geoLocationDto.longitude == geoLocationDto.longitude
            minBid == addAuctionItemRequestDto.minBid
            name == addAuctionItemRequestDto.name
        }
        0 * _
    }

    def "Convert AddAuctionItemRequestDto to AuctionItem"() {
        given:
        GeoLocationDto geoLocationDto = new GeoLocationDto(latitude: 15.2, longitude: 123.12341)
        AddAuctionItemRequestDto addAuctionItemRequestDto = new AddAuctionItemRequestDto(name: 'An auction',
                minBid: 20.1, buyout: 40, startDate: new Date(), endDate: new Date(),
                description: "some description", geoLocationDto: geoLocationDto, userId: 5, categories: ["578f8a542e5a3a48cfbfb070", "578f8a542e5a3a48ca3a3124"])

        when:
        AuctionItem auctionItem = auctionItemMapper.addAuctionItemRequestDtoToAuctionItem(addAuctionItemRequestDto)

        then:
        1 * mockCategoryRepository.findCategoriesByIds(addAuctionItemRequestDto.categories) >> [new Category()]
        CategoryNotFoundException e = thrown()
        e.localizedMessage == CategoryError.CATEGORY_NOT_FOUND.description;
    }

    def "Convert AuctionItem to AddAuctionItemResponseDto"() {
        given:
        GeoLocation geoLocation = new GeoLocation(latitude: 12.51, longitude: -20.11)
        AuctionItem auctionItem = new AuctionItem(auctionItemId: "421das12dada1d1d1", bidsNo: 12, buyout: 150,
                currentBid: 44.2, description: 'Some description', geoLocation: geoLocation,
                minBid: 10.0, name: 'PokemonGoApk', startDate: new Date(), endDate: new Date(),
                userId: "578f869f5a61a77b7915252a", categories: ["578f8a542e5a3a48cfbfb070", "578f8a542e5a3a48ca3a3124"])
        List<Category> categoriesList = [new Category(), new Category()]
        List<CategoryResponseDto> categoryResponseDtoList = [new CategoryResponseDto(), new CategoryResponseDto()]

        when:
        AddAuctionItemResponseDto addAuctionItemResponseDto = auctionItemMapper.auctionItemToAddAuctionItemResponseDto(auctionItem)

        then:
        1 * mockCategoryRepository.findCategoriesByIds(auctionItem.categories) >> categoriesList
        1 * mockCategoryMapper.categoryListToCategoryResponseDto(categoriesList) >> categoryResponseDtoList
        with(addAuctionItemResponseDto) {
            auctionItemId == auctionItem.auctionItemId
            name == auctionItem.name
            currentBid == auctionItem.currentBid
            buyout == auctionItem.buyout
            minBid == auctionItem.minBid
            bidsNo == auctionItem.bidsNo
            startDate == auctionItem.startDate
            endDate == auctionItem.endDate
            description == auctionItem.description
            geoLocationDto.latitude == auctionItem.geoLocation.latitude
            geoLocationDto.longitude == auctionItem.geoLocation.longitude
            userId == auctionItem.userId
            categories.size == categoriesList.size
        }
        0 * _
    }

    def "Convert a list of AuctionItems to a list of AuctionItemResponseDtos"() {
        given:
        GeoLocation geoLocation1 = new GeoLocation(latitude: 12.51, longitude: -20.11)
        GeoLocation geoLocation2 = new GeoLocation(latitude: 90, longitude: 180)
        List<String> categoryList = ["578f8a542e5a3a48cfbfb070", "578f8a542e5a3a48ca3a3124"]
        List<Category> categories = [new Category(), new Category()]
        List<CategoryResponseDto> categoryResponseDtoList = [new CategoryResponseDto(), new CategoryResponseDto()]
        List<AuctionItem> auctionItemList = [
                new AuctionItem(auctionItemId: "421das12dada1d1d1", bidsNo: 12, buyout: 150,
                        currentBid: 44.2, description: 'Some description', geoLocation: geoLocation1,
                        minBid: 10.0, name: 'PokemonGoApk', startDate: new Date(), endDate: new Date(),
                        userId: "578f869f5a61a77b7915252a", categories: categoryList),

                new AuctionItem(auctionItemId: "421das12dada1d1d2", bidsNo: 3, buyout: 1400,
                        currentBid: 12.5, description: 'Another description', geoLocation: geoLocation2,
                        minBid: 10.0, name: 'DigimonApk', startDate: new Date(), endDate: new Date(),
                        userId: "578f869f5a61a77b7915252b", categories: categoryList),
                null,
                null
        ]

        when:
        List<AuctionItemResponseDto> auctionItemResponseDto = auctionItemMapper.auctionItemsToAuctionItemResponseDto(auctionItemList)

        then:
        2 * mockCategoryRepository.findCategoriesByIds(categoryList) >> categories
        2 * mockCategoryMapper.categoryListToCategoryResponseDto(categories) >> categoryResponseDtoList
        with(auctionItemResponseDto[0]) {
            auctionItemId == auctionItemList[0].auctionItemId
            bidsNo == auctionItemList[0].bidsNo
            buyout == auctionItemList[0].buyout
            currentBid == auctionItemList[0].currentBid
            description == auctionItemList[0].description
            geoLocationDto.latitude == auctionItemList[0].geoLocation.latitude
            geoLocationDto.longitude == auctionItemList[0].geoLocation.longitude
            minBid == auctionItemList[0].minBid
            name == auctionItemList[0].name
            startDate == auctionItemList[0].startDate
            endDate == auctionItemList[0].endDate
            userId == auctionItemList[0].userId
            auctionItemResponseDto[0].categoryResponseDtoList == categoryResponseDtoList
        }
        with(auctionItemResponseDto[1]) {
            auctionItemId == auctionItemList[1].auctionItemId
            bidsNo == auctionItemList[1].bidsNo
            buyout == auctionItemList[1].buyout
            currentBid == auctionItemList[1].currentBid
            description == auctionItemList[1].description
            geoLocationDto.latitude == auctionItemList[1].geoLocation.latitude
            geoLocationDto.longitude == auctionItemList[1].geoLocation.longitude
            minBid == auctionItemList[1].minBid
            name == auctionItemList[1].name
            startDate == auctionItemList[1].startDate
            endDate == auctionItemList[1].endDate
            userId == auctionItemList[1].userId
            auctionItemResponseDto[1].categoryResponseDtoList == categoryResponseDtoList
        }
        0 * _
    }

}