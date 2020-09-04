package kr.pe.timeorder.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
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
	@OneToMany
	private List<Item> items;
	@OneToMany
	private List<Review> reviews;
	
	@Builder
	public Store(String storeName, String storeInfo, String storeNum, Member member, List<Item> items,
			List<Review> reviews) {
		super();
		this.storeName = storeName;
		this.storeInfo = storeInfo;
		this.storeNum = storeNum;
		this.member = member;
		this.items = items;
		this.reviews = reviews;
	}
	
}
