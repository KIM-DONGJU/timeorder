package kr.pe.timeorder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class UploadFile {
	@Id
	@GeneratedValue
	private long id;
	private String fileName;
	private String fileDownloadUri;
	private String fileType;
	private long fileSize;
	@JsonBackReference
	@ManyToOne
	private Store store;
	@JsonBackReference
	@OneToOne
	private Item item;
	@JsonBackReference
	@OneToOne
	private Member member;
	@JsonBackReference
	@OneToOne
	private Review review;

	@Builder
	public UploadFile(String fileName, String fileDownloadUri, String fileType, long fileSize, Store store, Item item,
			Member member, Review review) {
		super();
		this.fileName = fileName;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.store = store;
		this.item = item;
		this.member = member;
		this.review = review;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public void setItem(Item item) {
		if (item != null) {
			item.setUploadFile(this);
		}
		this.item = item;
	}

	public void setMember(Member member) {
		if (member != null) {
			member.setUploadFile(this);
		}
		this.member = member;
	}

	public void setReview(Review review) {
		if (review != null) {
			review.setUploadFile(this);
		}
		this.review = review;
	}
	
	public void setStore(Store store) {
		if (this.store != null) {
			this.store.getUploadFile().remove(this);
		}
		
		this.store = store;
		
		if (store != null) {
			store.getUploadFile().add(this);
		}
	}

}
