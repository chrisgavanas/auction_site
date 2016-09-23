package com.webapplication.scheduler;


import com.webapplication.dao.AuctionItemRepository;
import com.webapplication.entity.AuctionItem;
import com.webapplication.entity.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Component
public class BidScheduler {

    private final static int SCHEDULED_TIME_MINUTES = 1000 * 60 * 10;

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Scheduled(fixedDelay = SCHEDULED_TIME_MINUTES)
    public void scanForSoldBiddedAuctionItems() {
        List<AuctionItem> auctionItems = auctionItemRepository.findSoldAndBiddedAuctionItems(new Date());
        auctionItems.forEach(auctionItem -> {
            Bid winningBid = auctionItem.getBids().get(0);
            auctionItem.setBuyerId(winningBid.getUserId());
            auctionItemRepository.save(auctionItem);
            //TODO Send message to seller and winner
        });
    }

}
