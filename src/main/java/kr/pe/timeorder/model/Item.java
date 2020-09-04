package kr.pe.timeorder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
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
	
	@ManyToOne
	private Store store;
	
	@Builder
	public Item(String itemName, int beforePrice, int afterPrice, String itemInfo, int itemCount, Store store) {
		super();
		this.itemName = itemName;
		this.beforePrice = beforePrice;
		this.afterPrice = afterPrice;
		this.itemInfo = itemInfo;
		this.itemCount = itemCount;
		this.store = store;
	}
	
}
