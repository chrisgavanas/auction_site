package com.webapplication.recommendation;


import com.webapplication.dao.AuctionItemRepository;
import com.webapplication.dao.UserRepository;
import com.webapplication.entity.AuctionItem;
import com.webapplication.entity.Bid;
import com.webapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Recommendation {

    private final static int REFRESH_TIME = 1000 * 60 * 60 * 6;

    private Map<String, Set<String>> preferredAuctionsPerUser = new ConcurrentHashMap<>();
    private Map<String, Integer> bidsOrBuyoutPerAuction = new ConcurrentHashMap<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Autowired
    private SessionRecommendation sessionRecommendation;

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

        System.out.println("done");
//        findNearestNeighbours(auctionItems, "57e2ae9c63448fff5384532a");
//        System.out.println(similarity("57e2ae9c63448fff5384532a", "57e2ae9f63448fff538454f7"));
    }

    public Map<String, Set<String>> get1(String userId) {
        return preferredAuctionsPerUser;
    }

    public Map<String, Integer> get2(String userId) {
        return bidsOrBuyoutPerAuction;
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


    public List<AuctionItem> recommendAuctionItemsForUser(String userId) {
        return sessionRecommendation.recommendItems(preferredAuctionsPerUser, bidsOrBuyoutPerAuction, userId);
    }

}
