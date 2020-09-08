package kr.pe.timeorder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
public class Member {
	@Id
	@GeneratedValue
	private long memberId;

	@Column(unique = true, length = 20)
	private String phone;
	@Column(length = 20)
	private String pw;

	@Column(unique = true, length = 12)
	private String name;

	private int author;
	@JsonBackReference
	@ManyToOne
	private Address address;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<Store> stores = new ArrayList<Store>();
	@JsonManagedReference
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<Review> reviews = new ArrayList<Review>();
	@JsonManagedReference
	@OneToOne(mappedBy = "member")
	private UploadFile uploadFile;
	
	@Builder
	public Member(String phone, String pw, String name, int author, Address address) {
		super();
		this.phone = phone;
		this.pw = pw;
		this.name = name;
		this.author = author;
		this.address = address;
	}

	public boolean isVaild() {
		if (this.phone == null || !Pattern.matches(RegularExpression.phoneNum, this.phone)) {
			return false;
		}

		if (this.pw == null || !Pattern.matches(RegularExpression.pw, this.pw)) {
			return false;
		}

		if (this.name == null || !Pattern.matches(RegularExpression.memberName, this.name)) {
			return false;
		}

		if (author < 0 || author > 2) {
			return false;
		}

		return true;
	}

	public void setPhone(String phone) {
		if (phone != null && Pattern.matches(RegularExpression.phoneNum, phone)) {
			this.phone = phone;
		}
	}

	public void setPw(String pw) {
		if (pw != null && Pattern.matches(RegularExpression.pw, pw)) {
			this.pw = pw;
		}
	}

	public void setName(String name) {
		if (name != null && Pattern.matches(RegularExpression.memberName, name)) {
			this.name = name;
		}
	}

	public void setAuthor(int author) {
		if (author >= 0 || author < 3) {
			this.author = author;
		}
	}

	public void setAddress(Address address) {
		if (address != null) {
			this.address = address;
		}
	}
	
	public void setUploadFile(UploadFile uploadFile) {
		if (uploadFile != null) {
			this.uploadFile = uploadFile;
		}
	}

}
