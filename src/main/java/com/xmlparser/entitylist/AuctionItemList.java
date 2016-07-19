package com.xmlparser.entitylist;


import com.xmlparser.entity.AuctionItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Items")
public class AuctionItemList {
    @XmlElement(name = "Item")
    private List<AuctionItem> auctionItemList;

    public List<AuctionItem> getAuctionItemList() {
        return auctionItemList;
    }

    public void setAuctionItemList(List<AuctionItem> auctionItemList) {
        this.auctionItemList = auctionItemList;
    }


}


