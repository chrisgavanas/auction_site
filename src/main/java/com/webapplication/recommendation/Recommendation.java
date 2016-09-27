package com.webapplication.recommendation;


import com.webapplication.dao.AuctionItemRepository;
import com.webapplication.dao.UserRepository;
import com.webapplication.entity.AuctionItem;
import com.webapplication.entity.Bid;
import com.webapplication.entity.User;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Recommendation {

    private final static int REFRESH_TIME = 1000 * 60 * 60 * 6;
    private Map<String, Set<String>> preferredAuctionsPerUser = new ConcurrentHashMap<>();
    private Map<String, Integer> bidsOrBuyoutPerAuction = new ConcurrentHashMap<>();
    private LocalDateTime lastRun;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Scheduled(fixedDelay = REFRESH_TIME)
    private void recommendations() {
        preferredAuctionsPerUser.clear();
        bidsOrBuyoutPerAuction.clear();

        List<User> users = userRepository.findAll();
        List<AuctionItem> auctionItems = auctionItemRepository.findAll();
        auctionItems.removeIf(auctionItem -> auctionItem.getBuyerId() == null && auctionItem.getBidsNo() == 0);
        users.forEach(user -> {
            Set<String> boughtAuctionItemIds = new HashSet<>();
            preferredAuctionsPerUser.put(user.getUserId(), boughtAuctionItemIds);
            for (AuctionItem auctionItem : auctionItems) {
                if (auctionItem.getBuyerId() != null && auctionItem.getBuyerId().equals(user.getUserId()))
                    boughtAuctionItemIds.add(auctionItem.getAuctionItemId());

                for (Bid bid : auctionItem.getBids())
                    if (bid.getUserId().equals(user.getUserId())) {
                        boughtAuctionItemIds.add(auctionItem.getAuctionItemId());
                        break;
                    }
            }
        });
        auctionItems.forEach(auctionItem -> bidsOrBuyoutPerAuction.put(auctionItem.getAuctionItemId(),
                findNumberOfBidsOfAuctionItem(auctionItem.getAuctionItemId())));
        lastRun = LocalDateTime.now();
    }

    public Map<String, Set<String>> getPreferredAuctionsPerUser() {
        return preferredAuctionsPerUser;
    }

    public Map<String, Integer> getBidsOrBuyoutPerAuction() {
        return bidsOrBuyoutPerAuction;
    }

    public LocalDateTime getLastRun() {
        return lastRun;
    }

    private Integer findNumberOfBidsOfAuctionItem(String auctionItem) {
        Optional<Integer> number = preferredAuctionsPerUser.values().stream()
                .map(set -> set.contains(auctionItem) ? 1 : 0)
                .reduce((c1, c2) -> c1 + c2);

        if (number.isPresent())
            return number.get();
        else
            return 0;
    }

}
