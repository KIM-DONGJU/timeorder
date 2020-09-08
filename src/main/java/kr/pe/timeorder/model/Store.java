package kr.pe.timeorder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kr.pe.timeorder.expression.RegularExpression;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Entity
public class Store {
	@Id
	@GeneratedValue
	private long storeId;

	private String storeName;
	private String storeInfo;
	private String storeNum;

	@ManyToOne
	private Member member;
	@ManyToOne
	private Address address;
	
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<Item> items = new ArrayList<Item>();
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<Review> reviews = new ArrayList<Review>();

	@Builder
	public Store(String storeName, String storeInfo, String storeNum, Member member, Address address) {
		super();
		this.storeName = storeName;
		this.storeInfo = storeInfo;
		this.storeNum = storeNum;

		if (this.member != null) {
			this.member.getStores().remove(this);
		}

		this.member = member;

		if (member != null) {
			member.getStores().add(this);
		}
		
		this.address = address;
	}

	public boolean isVaild() {
		if (this.storeNum == null || !Pattern.matches(RegularExpression.phoneNum, this.storeNum)) {
			return false;
		}

		if (this.storeInfo == null) {
			return false;
		}

		if (this.storeName == null || !Pattern.matches(RegularExpression.storeItemName, this.storeName)) {
			return false;
		}

		if (this.member == null) {
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
		if (this.member != null) {
			this.member.getStores().remove(this);
		}

		this.member = member;

		if (member != null) {
			member.getStores().add(this);
		}
	}
	
	public void setAddress(Address address) {
		if (address != null) {
			this.address = address;
		}
	}
}
