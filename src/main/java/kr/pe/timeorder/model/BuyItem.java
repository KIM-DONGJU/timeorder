package kr.pe.timeorder.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class BuyItem {
	@Id
	@GeneratedValue
	private long id;
	private Date buyDate;

	@ManyToOne
	private Item item;
	@JsonBackReference("mBuyItem")
	@ManyToOne
	private Member member;
	@JsonBackReference("sBuyItem")
	@ManyToOne
	private Store store;
	@JsonManagedReference("bReview")
	@OneToOne(mappedBy = "buyItem")
	private Review review;
	@Builder
	public BuyItem(Item item, Member member, Store store, Review review) {
		super();
		this.buyDate = new Date();
		this.item = item;
		this.member = member;
		if (member != null) {
			member.getBuyItem().add(this);
		}
		this.store = store;
		if (store != null) {
			store.getBuyItem().add(this);
		}
		this.review = review;
		if (review != null) {
			
		}
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = new Date();
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setMember(Member member) {
		if (this.member != null) {
			this.member.getBuyItem().remove(this);
		}

		this.member = member;

		if (member != null) {
			member.getBuyItem().add(this);
		}
	}

	public void setStore(Store store) {
		if (this.store != null) {
			this.store.getBuyItem().remove(this);
		}

		this.store = store;

		if (store != null) {
			store.getBuyItem().add(this);
		}
	}

	public void setReivew(Review reivew) {
		this.review = reivew;
	}
	
}
