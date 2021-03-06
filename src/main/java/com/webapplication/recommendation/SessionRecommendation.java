package com.webapplication.recommendation;

import com.webapplication.dao.AuctionItemRepository;
import com.webapplication.entity.AuctionItem;
import com.webapplication.utils.DoubleList;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionRecommendation {


    @Value("${recommendedNo}")
    private Integer recommendedItems;

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    private Map<String, Set<String>> preferredAuctionsPerUser;
    private Map<String, Integer> bidsOrBuyoutPerAuction;
    private List<String> kMostCommonUsers;
    private List<String> kMostCommonAuctionItems = new LinkedList<>();
    private LocalDateTime lastRun;
    private String recommendedForUser;

    public List<String> recommend() {
        return kMostCommonAuctionItems;
    }

    public void recommendItems(Map<String, Set<String>> preferredAuctionsPerUser, Map<String, Integer> bidsOrBuyoutPerAuction, LocalDateTime lastRun, String userId) {
        if (this.lastRun == null || this.lastRun.isBefore(lastRun) || !userId.equals(recommendedForUser)) {
            this.lastRun = lastRun;
            this.preferredAuctionsPerUser = preferredAuctionsPerUser;
            this.bidsOrBuyoutPerAuction = bidsOrBuyoutPerAuction;
            recommendItems(userId);
            recommendedForUser = userId;
        }
    }

    private void recommendItems(String userId) {
        if (preferredAuctionsPerUser.get(userId) == null) {
            setRandomAuctionItems();
            return;
        }

        kMostCommonUsers = findKMostCommonUsers(userId);
        kMostCommonAuctionItems = findRecommendedItems(userId);
    }

    private List<String> findRecommendedItems(String userId) {
        List<String> kMostCommonAuctionItems = new LinkedList<>();
        List<String> auctionsOfKUsers = new LinkedList<>();

        kMostCommonUsers.forEach(commonUser -> {
            Set<String> auctionsOfCommonUser = preferredAuctionsPerUser.get(commonUser);
            List<String> recommendedAuctions = auctionsOfCommonUser.stream().filter(auctionOfCommonUser -> {
                AuctionItem auction = auctionItemRepository.findAuctionItemByAuctionItemId(auctionOfCommonUser);
                return auction.getBuyerId() == null && !preferredAuctionsPerUser.get(userId).contains(auctionOfCommonUser);
            }).collect(Collectors.toList());
            kMostCommonAuctionItems.addAll(recommendedAuctions);
        });

        kMostCommonAuctionItems.sort((item1, item2) -> {
            Integer bidsNoOfItem1 = auctionItemRepository.findBidsNoOfAuction(item1).getBidsNo();
            Integer bidsNoOfItem2 = auctionItemRepository.findBidsNoOfAuction(item2).getBidsNo();
            return Integer.compare(bidsNoOfItem2, bidsNoOfItem1);
        });

        if (kMostCommonAuctionItems.size() < recommendedItems) {
            List<AuctionItem> activeAuctions = auctionItemRepository.findAuctionItemIdsOfActiveAuctions(new Date());
            Set<String> auctionItemIds = activeAuctions.stream().map(AuctionItem::getAuctionItemId).collect(Collectors.toSet());
            int itemNo = recommendedItems - kMostCommonAuctionItems.size();
            Random rng = new Random();
            while (itemNo > 0) {
                int pos = rng.nextInt(auctionItemIds.size());
                if (!kMostCommonAuctionItems.contains(activeAuctions.get(pos).getAuctionItemId())) {
                    itemNo--;
                    kMostCommonAuctionItems.add(activeAuctions.get(pos).getAuctionItemId());
                }
            }
        }

        return kMostCommonAuctionItems.subList(0, recommendedItems >= kMostCommonAuctionItems.size()
                ? kMostCommonAuctionItems.size() : recommendedItems);
    }

    private List<String> findKMostCommonUsers(String userId) {
        List<DoubleList> similarities = new LinkedList<>();
        Set<String> userIds = new HashSet<>(preferredAuctionsPerUser.keySet());
        Set<String> biddedOrBuyoutItemsByUser1 = preferredAuctionsPerUser.get(userId);
        userIds.remove(userId);
        Integer numberOfBidsOrBuyout1 = biddedOrBuyoutItemsByUser1.size();
        if (numberOfBidsOrBuyout1 == 0)
            return new LinkedList<>();

        for (String id : userIds) {
            Set<String> biddedOrBuyoutItemsByUser2 = new HashSet<>(preferredAuctionsPerUser.get(id));
            Integer numberOfBidsOrBuyout2 = biddedOrBuyoutItemsByUser2.size();
            biddedOrBuyoutItemsByUser2.retainAll(biddedOrBuyoutItemsByUser1);
            Integer itemsBiddedOrBuyoutByBothUsers = biddedOrBuyoutItemsByUser2.size();
            Double similarity = 0.0;
            if (numberOfBidsOrBuyout2 > 0)
                similarity = itemsBiddedOrBuyoutByBothUsers / Math.sqrt(numberOfBidsOrBuyout1 * numberOfBidsOrBuyout2);
            similarities.add(new DoubleList(similarity, id));
        }
        Collections.sort(similarities);

        int k = (int) Math.sqrt(preferredAuctionsPerUser.size() - 1);
        return similarities.subList(0, k >= similarities.size() ? similarities.size() : k)
                .stream().map(DoubleList::getId).collect(Collectors.toList());
    }

    private Double similarityOfAuctions(String auctionItem1, String auctionItem2) {
        Integer c1 = bidsOrBuyoutPerAuction.get(auctionItem1);
        Integer c2 = bidsOrBuyoutPerAuction.get(auctionItem2);
        if (c1 == null || c2 == null || c1 == 0.0 || c2 == 0.0)
            return 0.0;

        Integer userLikesBothAuctions = 0;
        for (String userId : kMostCommonUsers) {
            Boolean userPreference1 = preferredAuctionsPerUser.get(userId).contains(auctionItem1);
            Boolean userPreference2 = preferredAuctionsPerUser.get(userId).contains(auctionItem2);
            userLikesBothAuctions += userPreference1 && userPreference2 ? 1 : 0;
        }

        return userLikesBothAuctions / Math.sqrt(c1 * c2);
    }

    private void setRandomAuctionItems() {
        Integer totalAuctionItems = (int) auctionItemRepository.count();
        Random rng = new Random();
        Integer random = rng.nextInt(totalAuctionItems / recommendedItems);
        List<AuctionItem> auctionItems = auctionItemRepository.findActiveAuctions(new Date(),
                new PageRequest(random, recommendedItems));
        auctionItems.stream().map(AuctionItem::getAuctionItemId)
                .forEach(auctionItemId -> kMostCommonAuctionItems.add(auctionItemId));
    }


}
