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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Recommendation {

    private final static int REFRESH_TIME = 1000 * 60 * 60 * 6;

    @Value("${k}")
    private Integer k;

    private Map<String, Set<String>> preferredAuctionsPerUser = new ConcurrentHashMap<>();
    private Map<String, Integer> bidsOrBuyoutPerAuction = new HashMap<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Scheduled(fixedDelay = REFRESH_TIME)
    private void recommendations() {
        if (k == null || k < 0)
            return;
        long a = System.currentTimeMillis();
        preferredAuctionsPerUser.clear();
        bidsOrBuyoutPerAuction.clear();

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
        auctionItems.forEach(auctionItem -> bidsOrBuyoutPerAuction.put(auctionItem.getAuctionItemId(),
                findNumberOfBidsOfAuctionItem(auctionItem.getAuctionItemId())));

        System.out.println(similarity("57e2ae7a63448fff5384304a", "57e2ae7a63448fff538430b5"));
        System.out.println(System.currentTimeMillis() - a);
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

    private Double similarity(String auctionItem1, String auctionItem2) {
        Integer c1 = bidsOrBuyoutPerAuction.get(auctionItem1);
        Integer c2 = bidsOrBuyoutPerAuction.get(auctionItem2);
        Integer userLikesBothAuctions = 0;
        for (Set<String> auctionsPerUser : preferredAuctionsPerUser.values()) {
            Boolean userPreference1 = auctionsPerUser.contains(auctionItem1);
            Boolean userPreference2 = auctionsPerUser.contains(auctionItem2);
            userLikesBothAuctions += userPreference1 && userPreference2 ? 1 : 0;
        }

        return userLikesBothAuctions / Math.sqrt((c1 * c2));
    }

}
