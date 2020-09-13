package kr.pe.timeorder.model;

import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import kr.pe.timeorder.expression.RegularExpression;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class Item {
	@Id
	@GeneratedValue
	private long itemId;
	private String itemName;
	private int beforePrice;
	private int afterPrice;
	private String itemInfo;
	private int itemCount;
	
	@JsonBackReference("sItem")
	@ManyToOne
	private Store store;
	@JsonManagedReference("iFile")
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private UploadFile uploadFile;
 
	@Builder
	public Item(String itemName, int beforePrice, int afterPrice, String itemInfo, int itemCount, Store store) {
		super();
		this.itemName = itemName;
		this.beforePrice = beforePrice;
		this.afterPrice = afterPrice;
		this.itemInfo = itemInfo;
		this.itemCount = itemCount;

		if (this.store != null) {
			this.store.getItems().remove(this);
		}

		this.store = store;

		if (store != null) {
			store.getItems().add(this);
		}
	}

	public boolean isVaild() {
		if (this.beforePrice <= 0 || this.afterPrice <= 0) {
			System.out.println("1");
			return false;
		}

		if (this.afterPrice > this.beforePrice) {
			System.out.println("2");
			return false;
		}

		if (this.itemName == null || !Pattern.matches(RegularExpression.storeItemName, this.itemName)) {
			return false;
		}

		if (this.itemCount < 0) {
			System.out.println("5");
			return false;
		}

		if (this.store == null) {
			System.out.println("6");
			return false;
		}

		return true;
	}

	public void setItemName(String itemName) {
		if (itemName != null && Pattern.matches(RegularExpression.storeItemName, itemName)) {
			this.itemName = itemName;
		}
	}

	public void setBeforePrice(int beforePrice) {
		if (beforePrice > 0 || beforePrice < this.afterPrice) {
			this.beforePrice = beforePrice;
		}
	}

	public void setAfterPrice(int afterPrice) {
		if (afterPrice > 0 || afterPrice < this.beforePrice) {
			this.afterPrice = afterPrice;
		}
	}

	public void setItemInfo(String itemInfo) {
		if (itemInfo != null) {
			this.itemInfo = itemInfo;
		}
	}

	public void setItemCount(int itemCount) {
		if (itemCount >= 0) {
			this.itemCount = itemCount;
		}
	}

	public void setStore(Store store) {
		if (this.store != null) {
			this.store.getItems().remove(this);
		}

		this.store = store;

		if (store != null) {
			store.getItems().add(this);
		}
	}
	
	public void setUploadFile(UploadFile uploadFile) {
		if (uploadFile != null) {
			this.uploadFile = uploadFile;
		}
	}
}
