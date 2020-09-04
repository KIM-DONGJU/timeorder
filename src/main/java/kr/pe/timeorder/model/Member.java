package kr.pe.timeorder.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Member {
	@Id
	@GeneratedValue
	private long memberId;
	
	@Column(unique=true)
	private String phone;
	
	private String pw;
	@Column(unique = true)
	private String name;
	
	private int author;
	
	@OneToMany
	private List<Store> stores;
	@OneToMany
	private List<Review> reviews;
	
	@Builder
	public Member(String phone, String pw, String name, int author, List<Store> stores, List<Review> reviews) {
		super();
		this.phone = phone;
		this.pw = pw;
		this.name = name;
		this.author = author;
		this.stores = stores;
		this.reviews = reviews;
	}
	
}
