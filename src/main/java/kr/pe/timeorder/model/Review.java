package kr.pe.timeorder.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Review {
	@Id
	@GeneratedValue
	private long reviewId;
	private String contents;
	private int score;
	
	private Date writeday;
	
	@JsonBackReference("mReview")
	@ManyToOne
	private Member member;
	@JsonBackReference("sReview")
	@ManyToOne
	private Store store;
	@JsonManagedReference("rFile")
	@OneToOne(mappedBy = "review")
	private UploadFile uploadFile;
	@JsonBackReference("bReview")
	@OneToOne
	private BuyItem buyItem;
	@Builder
	public Review(String contents, int score, Member member, Store store, BuyItem buyItem) {
		super();
		this.contents = contents;
		this.score = score;
		this.writeday = new Date();
		this.member = member;
		if (member != null) {
			member.getReviews().add(this);
		}
		this.store = store;
		if (store != null) {
			store.getReviews().add(this);
		}
		this.buyItem = buyItem;
		if (buyItem != null) {
			buyItem.setReivew(this);
		}
	}

	public boolean isVaild() {
		if (this.contents == null || this.contents.length() == 0) {
			return false;
		}

		if (this.score < 0 || this.score > 5) {
			return false;
		}

		if (this.member == null) {
			return false;
		}

		if (this.store == null) {
			return false;
		}

		return true;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public void setScore(int score) {
		if (this.score >= 0 || this.score <= 5) {
			this.score = score;
		}
	}

	public void setWriteday() {
		this.writeday = new Date();
	}

	public void setMember(Member member) {
		if (this.member != null) {
			this.member.getReviews().remove(this);
		}

		this.member = member;

		if (member != null) {
			member.getReviews().add(this);
		}
	}

	public void setStore(Store store) {
		if (this.store != null) {
			this.store.getReviews().remove(this);
		}

		this.store = store;

		if (store != null) {
			store.getReviews().add(this);
		}
	}
	
	public void setUploadFile(UploadFile uploadFile) {
		if (uploadFile != null) {
			this.uploadFile = uploadFile;
		}
	}

	public void setBuyItem(BuyItem buyItem) {
		this.buyItem = buyItem;
		if (buyItem != null) {
			buyItem.setReivew(this);
		}
	}
	

}
