package com.webapplication.mapper;

import com.google.common.collect.Lists;
import com.webapplication.dao.AuctionItemRepository;
import com.webapplication.dao.CategoryRepository;
import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemUpdateRequestDto;
import com.webapplication.dto.user.GeoLocationDto;
import com.webapplication.entity.AuctionItem;
import com.webapplication.entity.Category;
import com.webapplication.entity.GeoLocation;
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.error.category.CategoryError;
import com.webapplication.exception.CategoryHierarchyException;
import com.webapplication.exception.CategoryNotFoundException;
import com.webapplication.exception.InvalidAuctionException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AuctionItemMapper {

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    public AuctionItem addAuctionItemRequestDtoToAuctionItem(AddAuctionItemRequestDto auctionItemRequestDto, List<String> imagesPath) throws Exception {
        if (auctionItemRequestDto == null)
            return null;

        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setAuctionItemId(auctionItemRequestDto.getAuctionItemId());
        auctionItem.setName(auctionItemRequestDto.getName());
        auctionItem.setMinBid(auctionItemRequestDto.getMinBid());
        auctionItem.setBuyout(auctionItemRequestDto.getBuyout());
        auctionItem.setCurrentBid(auctionItemRequestDto.getMinBid());
        auctionItem.setBidsNo(0);
        auctionItem.setDescription(auctionItemRequestDto.getDescription());
        GeoLocationDto geoLocationDto = auctionItemRequestDto.getGeoLocationDto();
        if (geoLocationDto != null) {
            GeoLocation geoLocation = new GeoLocation(geoLocationDto.getLatitude(), geoLocationDto.getLongitude());
            auctionItem.setGeoLocation(geoLocation);
        }
        auctionItem.setUserId(auctionItemRequestDto.getUserId());
        List<String> categoryIds = auctionItemRequestDto.getCategoryIds();
        validateCategoryIds(auctionItemRequestDto.getCategoryIds());
        auctionItem.setCategories(categoryIds);
        auctionItem.setBids(new ArrayList<>());
        auctionItem.setImages(imagesPath);
        auctionItem.setTtl(null);

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
        addAuctionItemResponseDto.setDescription(auctionItem.getDescription());
        GeoLocation geoLocation = auctionItem.getGeoLocation();
        if (geoLocation != null) {
            GeoLocationDto geoLocationDto = new GeoLocationDto(geoLocation.getLatitude(), geoLocation.getLongitude());
            addAuctionItemResponseDto.setGeoLocationDto(geoLocationDto);
        }
        addAuctionItemResponseDto.setUserId(auctionItem.getUserId());
        List<Category> categories = categoryRepository.findCategoriesByIds(auctionItem.getCategoriesId());
        addAuctionItemResponseDto.setCategories(categoryMapper.categoriesToCategoryResponseDtoList(categories));
//        Optional.ofNullable(auctionItem.getImages()).ifPresent(addAuctionItemResponseDto::setImages); //TODO

        return addAuctionItemResponseDto;
    }

    public List<AuctionItemResponseDto> auctionItemsToAuctionItemResponseDto(List<AuctionItem> auctionItems) {
        if (auctionItems == null)
            return Lists.newArrayList();

        return auctionItems.stream()
                .map(this::auctionItemToAuctionItemResponseDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    public AuctionItemResponseDto auctionItemToAuctionItemResponseDto(AuctionItem auctionItem) {
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
        List<Category> categories = categoryRepository.findCategoriesByIds(auctionItem.getCategoriesId());
        auctionItemResponseDto.setCategories(categoryMapper.categoriesToCategoryResponseDtoList(categories));
        auctionItemResponseDto.setUserId(auctionItem.getUserId());
        //TODO images

        return auctionItemResponseDto;
    }

    public void update(AuctionItem auctionItem, Date startDate, Date endDate) {
        auctionItem.setStartDate(startDate);
        auctionItem.setEndDate(endDate);
    }

    public void update(AuctionItem auctionItem, AuctionItemUpdateRequestDto auctionItemUpdateRequestDto) throws Exception {
        auctionItem.setBuyout(auctionItemUpdateRequestDto.getBuyout());
        auctionItem.setDescription(auctionItemUpdateRequestDto.getDescription());
        auctionItem.setMinBid(auctionItemUpdateRequestDto.getMinBid());
        auctionItem.setName(auctionItemUpdateRequestDto.getName());
        GeoLocationDto geoLocationDto = auctionItemUpdateRequestDto.getGeoLocationDto();
        if (geoLocationDto != null) {
            GeoLocation geoLocation = new GeoLocation();
            geoLocation.setLatitude(geoLocationDto.getLatitude());
            geoLocation.setLongitude(geoLocationDto.getLongitude());
        } else
            auctionItem.setGeoLocation(null);
        validateCategoryIds(auctionItemUpdateRequestDto.getCategoryIds());
        auctionItem.setCategories(auctionItemUpdateRequestDto.getCategoryIds());
        //TODO images
    }

    private void validateCategoryIds(List<String> categoryIds) throws Exception {
        List<Category> categories = categoryRepository.findCategoriesByIds(categoryIds);
        if (categoryIds.size() != categories.size())
            throw new CategoryNotFoundException(CategoryError.CATEGORY_NOT_FOUND);

        for (int i = 1; i < categories.size(); i++) {
            Optional.ofNullable(categories.get(i).getParentId()).orElseThrow(() -> new CategoryHierarchyException(CategoryError.INVALID_CATEGORY_HIERARCHY));
            if (!categories.get(i).getParentId().equals(categories.get(i - 1).getCategoryId()))
                throw new CategoryHierarchyException(CategoryError.INVALID_CATEGORY_HIERARCHY);
        }
    }

    public AuctionItem initializeAuctionItemWithImage(String imagePath, String auctionItemId, String userId) throws Exception {
        AuctionItem auctionItem = auctionItemRepository.findAuctionItemByAuctionItemId(auctionItemId);
        if (auctionItem != null && !auctionItem.getUserId().equals(userId))
            throw new InvalidAuctionException(AuctionItemError.INVALID_AUCTION);
        else if (auctionItem == null) {
            auctionItem = new AuctionItem();
            auctionItem.setImages(new LinkedList<>());
        }
        auctionItem.setAuctionItemId(ObjectId.get().toString());
        auctionItem.getImages().add(imagePath);
        auctionItem.setTtl(new Date());

        return auctionItem;
    }

}
