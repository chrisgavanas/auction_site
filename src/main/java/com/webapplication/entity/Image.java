package com.webapplication.entity;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name="`image`")
@NamedQueries({
	@NamedQuery(name="Image.findAll", query="SELECT i FROM Image i")
})
public class Image implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="image_id")
	private int imageId;

	@Lob
	private byte[] image;

	@ManyToOne
	@JoinColumn(name="auctionItem_id")
	private Auction_item auctionItem;

	public Image() {
	}

	public int getImageId() {
		return this.imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public byte[] getImage() {
		return this.image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Auction_item getAuctionItem() {
		return this.auctionItem;
	}

	public void setAuctionItem(Auction_item auctionItem) {
		this.auctionItem = auctionItem;
	}

}