package com.xmlparser.entitylist;


import com.xmlparser.entity.BidAuction;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Bids")
public class BidAuctionList {
    @XmlElement(name = "Bid")
    private List<BidAuction> bids;

    public List<BidAuction> getBids() {
        return bids;
    }

    public void setBids(List<BidAuction> bids) {
        this.bids = bids;
    }

    public Boolean isEmpty() {
        return bids == null;
    }

}
