package com.webapplication.service.auctionitem;

import com.webapplication.dao.AuctionItemRepository;
import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import com.webapplication.entity.Auctionitem;
import com.webapplication.mapper.AuctionItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
public class AuctionItemServiceApiImpl implements AuctionItemServiceApi {

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Autowired
    private AuctionItemMapper auctionItemMapper;

    @Override
    public AddAuctionItemResponseDto addAuctionItem(AddAuctionItemRequestDto auctionItemRequestDto) throws Exception {
        Auctionitem auctionItem = auctionItemMapper.addAuctionItemRequestDtoToAuctionItem(auctionItemRequestDto);
        auctionItemRepository.save(auctionItem);

        return auctionItemMapper.auctionItemToAddAuctionItemResponseDto(auctionItem);
    }

    @Override
    public List<AuctionItemResponseDto> getAuctionItemsOfUser(Integer userId) throws Exception {
        List<Auctionitem> auctionItems = auctionItemRepository.findAuctionitemByUser(userId);

        return auctionItemMapper.auctionItemsToAuctionItemResponseDto(auctionItems);
    }

}
