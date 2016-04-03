package com.webapplication.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


@Entity
@Table(name="`bid`")
@NamedQueries({
	@NamedQuery(name="Bid.findAll", query="SELECT b FROM Bid b")
})
public class Bid implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="bid_id")
	private int bidId;

	private double amount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bidding_time")
	private Date biddingTime;

	@ManyToOne
	@JoinColumn(name="auctionItem_user_id")
	private Auction_item auctionItem;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public Bid() {
	}

	public int getBidId() {
		return this.bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getBiddingTime() {
		return this.biddingTime;
	}

	public void setBiddingTime(Date biddingTime) {
		this.biddingTime = biddingTime;
	}

	public Auction_item getAuctionItem() {
		return this.auctionItem;
	}

	public void setAuctionItem(Auction_item auctionItem) {
		this.auctionItem = auctionItem;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}