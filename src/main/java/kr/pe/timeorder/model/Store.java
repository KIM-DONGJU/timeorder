package kr.pe.timeorder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import kr.pe.timeorder.expression.RegularExpression;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Store {
	@Id
	@GeneratedValue
	private long storeId;

	private String storeName;
	private String storeInfo;
	private String storeNum;
	private int openTime;
	private int closeTime;
	private int score = 0; 
	private int scoreCnt = 0;
	private double x;
	private double y;
	private String address_name;
	private String detail_address;
	@JsonBackReference("mStore")
	@OneToOne
	private Member member;

	@JsonManagedReference("sItem")
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<Item> items = new ArrayList<Item>();
	@JsonManagedReference("sReview")
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<Review> reviews = new ArrayList<Review>();
	@JsonManagedReference("sFile")
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<UploadFile> uploadFile;
	@JsonManagedReference("sBuyItem")
	@OneToMany(mappedBy = "store")
	private List<BuyItem> buyItem = new ArrayList<>();
	
	@Builder
	public Store(String storeName, int openTime, int closeTime, String storeInfo, String storeNum, double x, double y, String address_name, String detail_address, Member member) {
		super();
		this.storeName = storeName;
		this.storeInfo = storeInfo;
		this.storeNum = storeNum;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.x = x;
		this.y = y;
		this.address_name = address_name;
		this.detail_address = detail_address;
		
		this.member = member;
		if (member != null) {
			member.setStore(this);
		}
		
	}

	
	public boolean isVaild() {
		if (this.storeNum == null || !Pattern.matches(RegularExpression.phoneNum, this.storeNum)) {
			return false;
		}

		if (this.storeName == null || !Pattern.matches(RegularExpression.storeItemName, this.storeName)) {
			return false;
		}

		if (this.member == null) {
			return false;
		}
		
		if (this.closeTime < 0 || this.closeTime > 24) {
			return false;
		}
		
		if (this.openTime < 0 || this.openTime > 24) {
			return false;
		}

		return true;
	}

	public void setStoreName(String storeName) {
		if (storeName != null && Pattern.matches(RegularExpression.storeItemName, storeName)) {
			this.storeName = storeName;
		}
	}

	public void setStoreInfo(String storeInfo) {
		if (storeInfo != null) {
			this.storeInfo = storeInfo;
		}
	}

	public void setStoreNum(String storeNum) {
		if (storeNum != null && Pattern.matches(RegularExpression.phoneNum, storeNum)) {
			this.storeNum = storeNum;
		}
	}

	public void setMember(Member member) {
		this.member = member;
		if (this.member != null) {
			this.member.setStore(this);
		}
	}
	
	public void setOpenTime(int openTime) {
		if (openTime > 0 && openTime <= 24) {
			this.openTime = openTime;
		}
	}

	public void setCloseTime(int closeTime) {
		if (closeTime > 0 && closeTime <= 24) {
			this.closeTime = closeTime;
		}
	}
	
	public void setScore(int score) {
		if (score > 0 || score <= 5) {
			this.score = score;
		}
	}
	
	public void addScore(int score) {
		if (score > 0 || score <= 5) {
			this.score += score;
			this.scoreCnt++;
		}
	}
	
	public void setX(double x) {
		if (x != 0.0) {
			this.x = x;
		}
	}


	public void setY(double y) {
		if (y != 0.0) {
		this.y = y;
		}
	}


	public void setAddress_name(String address_name) {
		if (address_name != null) {
		this.address_name = address_name;
		}
	}


	public void setDetail_address(String detail_address) {
		if (detail_address != null) {
			this.detail_address = detail_address;
		}
	}
}
