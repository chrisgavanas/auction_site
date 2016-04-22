package com.webapplication.service.auctionitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.webapplication.dao.AuctionItemRepository;
import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.entity.Auctionitem;
import com.webapplication.mapper.AuctionItemMapper;

@Transactional
@Component
public class AuctionItemServiceApiImpl implements AuctionItemServiceApi {

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Autowired
    private AuctionItemMapper auctionItemMapper;

    public AddAuctionItemResponseDto addAuctionItem(AddAuctionItemRequestDto auctionItemRequestDto) throws Exception {
        Auctionitem auctionItem = auctionItemMapper.addAuctionItemRequestDtoToAuctionItem(auctionItemRequestDto);
        auctionItemRepository.save(auctionItem);

        return auctionItemMapper.auctionItemToAddAuctionItemResponseDto(auctionItem);
    }

}
