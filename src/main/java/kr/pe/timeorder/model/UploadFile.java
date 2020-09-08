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
	private String fileUri;
	private String fileType;
	private long fileSize;
	@JsonBackReference("sFile")
	@ManyToOne
	private Store store;
	@JsonBackReference("iFile")
	@OneToOne
	private Item item;
	@JsonBackReference("mFile")
	@OneToOne
	private Member member;
	@JsonBackReference("rFile")
	@OneToOne
	private Review review;

	@Builder
	public UploadFile(String fileName, String fileUri, String fileType, long fileSize, Store store, Item item,
			Member member, Review review) {
		super();
		this.fileName = fileName;
		this.fileUri = fileUri;
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

	public void setFileDownloadUri(String fileUri) {
		this.fileUri = fileUri;
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
