package com.webapplication.recommendation;

import com.webapplication.dao.AuctionItemRepository;
import com.webapplication.entity.AuctionItem;
import com.webapplication.utils.DoubleList;
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
    private List<String> kMostCommonAuctionItems;

    public List<AuctionItem> recommendItems(Map<String, Set<String>> preferredAuctionsPerUser, Map<String, Integer> bidsOrBuyoutPerAuction, String userId) {
        if (this.preferredAuctionsPerUser == null)
            this.preferredAuctionsPerUser = preferredAuctionsPerUser;
        if (this.bidsOrBuyoutPerAuction == null)
            this.bidsOrBuyoutPerAuction = bidsOrBuyoutPerAuction;

        return recommendItems(userId);
    }

    private List<AuctionItem> recommendItems(String userId) {
        kMostCommonUsers = findKMostCommonUsers(userId);
        kMostCommonAuctionItems = findRecommendedItems(userId, kMostCommonUsers);

        return null;
    }

    private List<String> findRecommendedItems(String userId, List<String> kMostCommonUsers) {
        List<String> kMostCommonAuctionItems = new LinkedList<>();
        List<AuctionItem> activeAuctions = auctionItemRepository.findAuctionItemIdsOfActiveAuctions(new Date());
        Set<String> auctionItemIds = activeAuctions.stream().map(AuctionItem::getAuctionItemId).collect(Collectors.toSet());
        List<DoubleList> similaritiesPerAuction = new LinkedList<>();
        Set<String> auctionItemIdsOfUsers = new HashSet<>();
        kMostCommonUsers.forEach(id -> auctionItemIdsOfUsers.addAll(preferredAuctionsPerUser.get(id)));
        for (String auctionItemId : auctionItemIds) {
            similaritiesPerAuction.clear();
            auctionItemIdsOfUsers.forEach(auctionItemIdOfUsers -> {
                Double similarity = similarityOfAuctions(auctionItemId, auctionItemIdOfUsers);
                similaritiesPerAuction.add(new DoubleList(similarity, auctionItemIdOfUsers));
            });
            Collections.sort(similaritiesPerAuction);
            int k = (int) Math.sqrt(auctionItemIds.size());
            List<String> kMostCommonItems = similaritiesPerAuction.subList(0,
                    k >= similaritiesPerAuction.size() ? similaritiesPerAuction.size() : k)
                    .stream().map(DoubleList::getId).collect(Collectors.toList());
            int matched = 0;
            for (String commonItemId : kMostCommonItems) {
                if (preferredAuctionsPerUser.get(userId).contains(commonItemId))
                    matched++;
                if (matched > kMostCommonItems.size() / 2)
                    break;
            }
            if (matched > kMostCommonItems.size() / 2)
                kMostCommonAuctionItems.add(auctionItemId);

            if (kMostCommonAuctionItems.size() == recommendedItems)
                break;
        }

        int i = 0;
        if (kMostCommonAuctionItems.size() < recommendedItems) {

        }

        System.out.println(kMostCommonAuctionItems.size());
        return kMostCommonAuctionItems;
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

    public void addBidOrBuyout(String userId, String auctionItemId) {   //TODO maybe it will be removed
        if (preferredAuctionsPerUser != null && preferredAuctionsPerUser.get(userId) != null)
            preferredAuctionsPerUser.get(userId).add(auctionItemId);
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

}
