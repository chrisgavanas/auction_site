package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u") 
})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "user_id")
	private int userId;

	@Column(name = "bidder_rating")
	private float bidderRating;

	private String city;

	private String country;

	@Temporal(TemporalType.DATE)
	@Column(name = "`Date of Birth`")
	private Date date_of_Birth;

	@Column(name = "`E-mail`")
	private String e_mail;

	@Column(name = "`First Name`")
	private String first_Name;

	private String gender;

	private boolean isAdmin;

	private boolean isVerified;

	@Column(name = "`Last Name`")
	private String last_Name;

	@Column(name = "`Mobile Number`")
	private String mobile_Number;

	private String password;

	@Column(name = "`Postal Code`")
	private String postal_Code;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "`Registration Date`")
	private Date registration_Date;

	@Column(name = "seller_rating")
	private float sellerRating;

	private String street;

	@Column(name = "`Telephone Number`")
	private String telephone_Number;

	private String username;

	private String vat;

	// bi-directional many-to-one association to Auction_item
	@OneToMany(mappedBy = "user")
	private List<Auction_item> auctionItems;

	// bi-directional many-to-one association to Bid
	@OneToMany(mappedBy = "user")
	private List<Bid> bids;

	public User() {
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public float getBidderRating() {
		return this.bidderRating;
	}

	public void setBidderRating(float bidderRating) {
		this.bidderRating = bidderRating;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getDate_of_Birth() {
		return this.date_of_Birth;
	}

	public void setDate_of_Birth(Date date_of_Birth) {
		this.date_of_Birth = date_of_Birth;
	}

	public String getE_mail() {
		return this.e_mail;
	}

	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}

	public String getFirst_Name() {
		return this.first_Name;
	}

	public void setFirst_Name(String first_Name) {
		this.first_Name = first_Name;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean getIsAdmin() {
		return this.isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean getIsVerified() {
		return this.isVerified;
	}

	public void setIsVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getLast_Name() {
		return this.last_Name;
	}

	public void setLast_Name(String last_Name) {
		this.last_Name = last_Name;
	}

	public String getMobile_Number() {
		return this.mobile_Number;
	}

	public void setMobile_Number(String mobile_Number) {
		this.mobile_Number = mobile_Number;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPostal_Code() {
		return this.postal_Code;
	}

	public void setPostal_Code(String postal_Code) {
		this.postal_Code = postal_Code;
	}

	public Date getRegistration_Date() {
		return this.registration_Date;
	}

	public void setRegistration_Date(Date registration_Date) {
		this.registration_Date = registration_Date;
	}

	public float getSellerRating() {
		return this.sellerRating;
	}

	public void setSellerRating(float sellerRating) {
		this.sellerRating = sellerRating;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getTelephone_Number() {
		return this.telephone_Number;
	}

	public void setTelephone_Number(String telephone_Number) {
		this.telephone_Number = telephone_Number;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVat() {
		return this.vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public List<Auction_item> getAuctionItems() {
		return this.auctionItems;
	}

	public void setAuctionItems(List<Auction_item> auctionItems) {
		this.auctionItems = auctionItems;
	}

	public Auction_item addAuctionItem(Auction_item auctionItem) {
		getAuctionItems().add(auctionItem);
		auctionItem.setUser(this);

		return auctionItem;
	}

	public Auction_item removeAuctionItem(Auction_item auctionItem) {
		getAuctionItems().remove(auctionItem);
		auctionItem.setUser(null);

		return auctionItem;
	}

	public List<Bid> getBids() {
		return this.bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public Bid addBid(Bid bid) {
		getBids().add(bid);
		bid.setUser(this);

		return bid;
	}

	public Bid removeBid(Bid bid) {
		getBids().remove(bid);
		bid.setUser(null);

		return bid;
	}

}