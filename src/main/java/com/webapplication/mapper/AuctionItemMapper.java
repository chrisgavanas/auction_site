package com.webapplication.mapper;

import com.google.common.collect.Lists;
import com.webapplication.dao.CategoryRepository;
import com.webapplication.dao.ImageRepository;
import com.webapplication.dao.UserRepository;
import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import com.webapplication.entity.Auctionitem;
import com.webapplication.entity.Category;
import com.webapplication.entity.Image;
import com.webapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AuctionItemMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageRepository imageRepository;

    public Auctionitem addAuctionItemRequestDtoToAuctionItem(AddAuctionItemRequestDto auctionItemRequestDto) {
        if (auctionItemRequestDto == null)
            return null;

        Auctionitem auctionItem = new Auctionitem();
        auctionItem.setName(auctionItemRequestDto.getName());
        auctionItem.setCurrentBid(auctionItemRequestDto.getCurrentBid());
        auctionItem.setBuyout(auctionItemRequestDto.getBuyout());
        auctionItem.setMinBid(auctionItemRequestDto.getCurrentBid());
        auctionItem.setBidsNo(0);
        auctionItem.setStartDate(auctionItemRequestDto.getStartDate());
        auctionItem.setEndDate(auctionItemRequestDto.getEndDate());
        auctionItem.setDescription(auctionItemRequestDto.getDescription());
        auctionItem.setLatitude(auctionItemRequestDto.getLatitude());
        auctionItem.setLongitude(auctionItemRequestDto.getLongitude());
        User user = userRepository.findUserByUserId(auctionItemRequestDto.getUserId());
        auctionItem.setUser(user);

        Optional.ofNullable(auctionItemRequestDto.getCategories()).ifPresent(categories -> {
            List<Category> cat = Lists.newArrayList(categoryRepository.findAll(categories));
            cat.forEach(category -> {
                category.getAuctionitems().add(auctionItem);
            });
            auctionItem.setCategories(cat);
        });
        Optional.ofNullable(auctionItemRequestDto.getImages()).ifPresent(images -> {
            auctionItem.setImages(images.stream()
                    .map(image -> {
                        Image auctionItemImage = new Image(image, auctionItem);
                        imageRepository.save(auctionItemImage);
                        return auctionItemImage;
                    }).collect(Collectors.toList())
            );
        });

        return auctionItem;
    }

    public AddAuctionItemResponseDto auctionItemToAddAuctionItemResponseDto(Auctionitem auctionItem) {
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
        addAuctionItemResponseDto.setLatitude(auctionItem.getLatitude());
        addAuctionItemResponseDto.setLongitutde(auctionItem.getLongitude());
        Optional.ofNullable(auctionItem.getUser()).ifPresent(user -> {
            addAuctionItemResponseDto.setUsername(user.getUsername());
            addAuctionItemResponseDto.setRatingAsSeller(user.getRatingAsSeller());
        });
        Optional.ofNullable(auctionItem.getCategories()).ifPresent(categories -> {
            addAuctionItemResponseDto.setCategories(categories);
        });
        Optional.ofNullable(auctionItem.getImages()).ifPresent(images -> {
            addAuctionItemResponseDto.setImages(images);
        });

        return addAuctionItemResponseDto;
    }

    public List<AuctionItemResponseDto> auctionItemsToAuctionItemResponseDto(List<Auctionitem> auctionItems) {
        if (auctionItems == null)
            return Lists.newArrayList();

        return auctionItems.stream()
                .map(auctionItem -> auctionItemToAuctionitemResponseDto(auctionItem))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    private AuctionItemResponseDto auctionItemToAuctionitemResponseDto(Auctionitem auctionItem) {
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
        auctionItemResponseDto.setLatitude(auctionItem.getLatitude());
        auctionItemResponseDto.setLongitude(auctionItem.getLongitude());
        auctionItemResponseDto.setUserId(auctionItem.getUser() == null ? null : auctionItem.getUser().getUserId());

        return auctionItemResponseDto;
    }

}
