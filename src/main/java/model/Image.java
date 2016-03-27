package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "Image.findAll", query = "SELECT i FROM Image i") 
})
public class Image implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "image_id")
	private int imageId;

	@Lob
	private byte[] image;

	// bi-directional many-to-one association to Auction_item
	@ManyToOne
	@JoinColumn(name = "auctionItem_id")
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