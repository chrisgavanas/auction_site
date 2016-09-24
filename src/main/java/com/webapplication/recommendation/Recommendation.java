package com.webapplication.recommendation;


import com.webapplication.dao.AuctionItemRepository;
import com.webapplication.dao.UserRepository;
import com.webapplication.entity.AuctionItem;
import com.webapplication.entity.Bid;
import com.webapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Recommendation {

    private final static int REFRESH_TIME = 1000 * 60 * 60 * 6;

    @Value("${k}")
    private Integer k;

    private Map<String, Set<String>> preferredAuctionsPerUser = new ConcurrentHashMap<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Scheduled(fixedDelay = REFRESH_TIME)
    private void recommendations() {
        if (k == null || k < 0)
            return;
        preferredAuctionsPerUser.clear();
        List<User> users = userRepository.findAll();
        List<AuctionItem> auctionItems = auctionItemRepository.findAll();
        auctionItems.removeIf(auctionItem -> auctionItem.getBuyerId() == null && auctionItem.getBidsNo() == 0);
        users.forEach(user -> {
            Set<String> boughtAuctionItemIds = new HashSet<>();
            preferredAuctionsPerUser.put(user.getUserId(), boughtAuctionItemIds);
            for (AuctionItem auctionItem : auctionItems) {
                if (auctionItem.getBuyerId() != null && auctionItem.getBuyerId().equals(user.getUserId())) {
                    boughtAuctionItemIds.add(auctionItem.getAuctionItemId());
                    break;
                }
                for (Bid bid : auctionItem.getBids())
                    if (bid.getUserId().equals(user.getUserId())) {
                        boughtAuctionItemIds.add(auctionItem.getAuctionItemId());
                        break;
                    }
                if (boughtAuctionItemIds.contains(auctionItem.getAuctionItemId()))
                    break;
            }
        });
    }

}
