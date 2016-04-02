package com.webapplication.dao;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


@Entity
@Table(name="`auction item`")
@NamedQueries({
	@NamedQuery(name="Auction_item.findAll", query="SELECT a FROM Auction_item a")
})
public class Auction_item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int auctionItem_id;

	private double buy_price;

	private double currently;

	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	private Date ended;

	private double first_bid;

	private double latitude;

	private double longtitude;

	private String name;

	private int number_of_bids;

	@Temporal(TemporalType.TIMESTAMP)
	private Date started;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	@ManyToMany(mappedBy="auctionItems")
	private List<Category> categories;

	@OneToMany(mappedBy="auctionItem")
	private List<Bid> bids;

	@OneToMany(mappedBy="auctionItem")
	private List<Image> images;

	public Auction_item() {
	}

	public int getAuctionItem_id() {
		return this.auctionItem_id;
	}

	public void setAuctionItem_id(int auctionItem_id) {
		this.auctionItem_id = auctionItem_id;
	}

	public double getBuy_price() {
		return this.buy_price;
	}

	public void setBuy_price(double buy_price) {
		this.buy_price = buy_price;
	}

	public double getCurrently() {
		return this.currently;
	}

	public void setCurrently(double currently) {
		this.currently = currently;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEnded() {
		return this.ended;
	}

	public void setEnded(Date ended) {
		this.ended = ended;
	}

	public double getFirst_bid() {
		return this.first_bid;
	}

	public void setFirst_bid(double first_bid) {
		this.first_bid = first_bid;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return this.longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber_of_bids() {
		return this.number_of_bids;
	}

	public void setNumber_of_bids(int number_of_bids) {
		this.number_of_bids = number_of_bids;
	}

	public Date getStarted() {
		return this.started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Bid> getBids() {
		return this.bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public Bid addBid(Bid bid) {
		getBids().add(bid);
		bid.setAuctionItem(this);

		return bid;
	}

	public Bid removeBid(Bid bid) {
		getBids().remove(bid);
		bid.setAuctionItem(null);

		return bid;
	}

	public List<Image> getImages() {
		return this.images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Image addImage(Image image) {
		getImages().add(image);
		image.setAuctionItem(this);

		return image;
	}

	public Image removeImage(Image image) {
		getImages().remove(image);
		image.setAuctionItem(null);

		return image;
	}

}