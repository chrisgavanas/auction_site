package com.webapplication.mapper;

import com.google.common.collect.Lists;
import com.webapplication.dao.CategoryRepository;
import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import com.webapplication.dto.user.GeoLocationDto;
import com.webapplication.entity.AuctionItem;
import com.webapplication.entity.Category;
import com.webapplication.entity.GeoLocation;
import com.webapplication.error.category.CategoryError;
import com.webapplication.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AuctionItemMapper {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    public AuctionItem addAuctionItemRequestDtoToAuctionItem(AddAuctionItemRequestDto auctionItemRequestDto) throws Exception {
        if (auctionItemRequestDto == null)
            return null;

        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setName(auctionItemRequestDto.getName());
        auctionItem.setMinBid(auctionItemRequestDto.getMinBid());
        auctionItem.setBuyout(auctionItemRequestDto.getBuyout());
        auctionItem.setCurrentBid(auctionItemRequestDto.getMinBid());
        auctionItem.setBidsNo(0);
        auctionItem.setStartDate(auctionItemRequestDto.getStartDate());
        auctionItem.setEndDate(auctionItemRequestDto.getEndDate());
        auctionItem.setDescription(auctionItemRequestDto.getDescription());
        GeoLocationDto geoLocationDto = auctionItemRequestDto.getGeoLocationDto();
        if (geoLocationDto != null) {
            GeoLocation geoLocation = new GeoLocation(geoLocationDto.getLatitude(), geoLocationDto.getLongitude());
            auctionItem.setGeoLocation(geoLocation);
        }
        auctionItem.setUserId(auctionItemRequestDto.getUserId());
        List<String> categoryIds = auctionItemRequestDto.getCategories();
        List<Category> categories = categoryRepository.findCategoriesByIds(categoryIds);
        if (categoryIds.size() != categories.size())
            throw new CategoryNotFoundException(CategoryError.CATEGORY_NOT_FOUND);
        auctionItem.setCategories(categoryIds);
        auctionItem.setBids(new ArrayList<>());
        auctionItem.setImages(new ArrayList<>());       //TODO

        return auctionItem;
    }

    public AddAuctionItemResponseDto auctionItemToAddAuctionItemResponseDto(AuctionItem auctionItem) {
        if (auctionItem == null)
            return null;

        AddAuctionItemResponseDto addAuctionItemResponseDto = new AddAuctionItemResponseDto();
        addAuctionItemResponseDto.setAuctionItemId(auctionItem.getAuctionItemId());
        addAuctionItemResponseDto.setName(auctionItem.getName());
        addAuctionItemResponseDto.setCurrentBid(auctionItem.getCurrentBid());
        addAuctionItemResponseDto.setBuyout(auctionItem.getBuyout());
        addAuctionItemResponseDto.setMinBid(auctionItem.getMinBid());
        addAuctionItemResponseDto.setBidsNo(auctionItem.getBidsNo());
        addAuctionItemResponseDto.setStartDate(auctionItem.getStartDate());
        addAuctionItemResponseDto.setEndDate(auctionItem.getEndDate());
        addAuctionItemResponseDto.setDescription(auctionItem.getDescription());
        GeoLocation geoLocation = auctionItem.getGeoLocation();
        if (geoLocation != null) {
            GeoLocationDto geoLocationDto = new GeoLocationDto(geoLocation.getLatitude(), geoLocation.getLongitude());
            addAuctionItemResponseDto.setGeoLocationDto(geoLocationDto);
        }
        addAuctionItemResponseDto.setUserId(auctionItem.getUserId());
        List<Category> categories = categoryRepository.findCategoriesByIds(auctionItem.getCategories());
        addAuctionItemResponseDto.setCategories(categoryMapper.categoryListToCategoryResponseDto(categories));
//        Optional.ofNullable(auctionItem.getImages()).ifPresent(addAuctionItemResponseDto::setImages); //TODO

        return addAuctionItemResponseDto;
    }

    public List<AuctionItemResponseDto> auctionItemsToAuctionItemResponseDto(List<AuctionItem> auctionItems) {
        if (auctionItems == null)
            return Lists.newArrayList();

        return auctionItems.stream()
                .map(this::auctionItemToAuctionitemResponseDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    private AuctionItemResponseDto auctionItemToAuctionitemResponseDto(AuctionItem auctionItem) {
        if (auctionItem == null)
            return null;

        AuctionItemResponseDto auctionItemResponseDto = new AuctionItemResponseDto();
        auctionItemResponseDto.setAuctionItemId(auctionItem.getAuctionItemId());
        auctionItemResponseDto.setName(auctionItem.getName());
        auctionItemResponseDto.setCurrentBid(auctionItem.getCurrentBid());
        auctionItemResponseDto.setBuyout(auctionItem.getBuyout());
        auctionItemResponseDto.setMinBid(auctionItem.getMinBid());
        auctionItemResponseDto.setBidsNo(auctionItem.getBidsNo());
        auctionItemResponseDto.setDescription(auctionItem.getDescription());
        auctionItemResponseDto.setStartDate(auctionItem.getStartDate());
        auctionItemResponseDto.setEndDate(auctionItem.getEndDate());
        GeoLocation geoLocation = auctionItem.getGeoLocation();
        if (geoLocation != null) {
            GeoLocationDto geoLocationDto = new GeoLocationDto(geoLocation.getLatitude(), geoLocation.getLongitude());
            auctionItemResponseDto.setGeoLocationDto(geoLocationDto);
        }
        List<Category> categories = categoryRepository.findCategoriesByIds(auctionItem.getCategories());
        auctionItemResponseDto.setCategoryResponseDtoList(categoryMapper.categoryListToCategoryResponseDto(categories));
        auctionItemResponseDto.setUserId(auctionItem.getUserId());
        //TODO images

        return auctionItemResponseDto;
    }

}
