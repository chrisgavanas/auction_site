package com.webapplication.recommendation;

import com.webapplication.entity.AuctionItem;
import com.webapplication.utils.DoubleList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionRecommendation {

    @Value("${k}")
    private Integer k;

    @Value("${recommendedNo}")
    private Integer recommendedItems;

    private Map<String, Set<String>> preferredAuctionsPerUser = null;
    private Map<String, Integer> bidsOrBuyoutPerAuction = null;
    private List<String> kMostCommonUsers = null;
    private List<String> kMostCommonAuctionItems = null;

    public List<AuctionItem> recommendItems(Map<String, Set<String>> preferredAuctionsPerUser, Map<String, Integer> bidsOrBuyoutPerAuction, String userId) {
        if (this.preferredAuctionsPerUser == null)
            this.preferredAuctionsPerUser = preferredAuctionsPerUser;
        if (this.bidsOrBuyoutPerAuction == null)
            this.bidsOrBuyoutPerAuction = bidsOrBuyoutPerAuction;

        return recommendItems(userId);
    }

    private List<AuctionItem> recommendItems(String userId) {
        kMostCommonUsers = findMostCommonUsers(userId);
        kMostCommonAuctionItems = findRecommendedItems(userId, kMostCommonUsers);

        return null;
    }

    private List<String> findRecommendedItems(String userId, List<String> kMostCommonUsers) {
        List<String> kMostCommonAuctionItems = new LinkedList<>();
        List<DoubleList> similaritiesPerAuction = new LinkedList<>();
        Set<String> auctionItemIds = new HashSet<>();

        kMostCommonUsers.forEach(id -> auctionItemIds.addAll(preferredAuctionsPerUser.get(id)));
        

        return kMostCommonAuctionItems;
    }

    private List<String> findMostCommonUsers(String userId) {
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

        return similarities.subList(0, k >= similarities.size() ? similarities.size() : k)
                .stream().map(DoubleList::getId).collect(Collectors.toList());
    }

    public void addBidOrBuyout(String userId, String auctionItemId) {
        if (preferredAuctionsPerUser != null && preferredAuctionsPerUser.get(userId) != null)
            preferredAuctionsPerUser.get(userId).add(auctionItemId);
    }

    private Double similarityOfAuctions(String auctionItem1, String auctionItem2) {
        Integer c1 = bidsOrBuyoutPerAuction.get(auctionItem1);
        Integer c2 = bidsOrBuyoutPerAuction.get(auctionItem2);
        Integer userLikesBothAuctions = 0;
        for (String userId : kMostCommonUsers) {
            Boolean userPreference1 = preferredAuctionsPerUser.get(userId).contains(auctionItem1);
            Boolean userPreference2 = preferredAuctionsPerUser.get(userId).contains(auctionItem2);
            userLikesBothAuctions += userPreference1 && userPreference2 ? 1 : 0;
        }

        Double similarity = 0.0;
        if (c1 != 0 && c2 != 0)
            similarity = userLikesBothAuctions / Math.sqrt(c1 * c2);

        return similarity;
    }

}
