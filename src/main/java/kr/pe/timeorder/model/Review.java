package kr.pe.timeorder.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
public class Review {
	@Id
	@GeneratedValue
	private long reviewId;
	private String contents;
	private int score;
	@Temporal(TemporalType.DATE)
	private Date writeday;
	
	@ManyToOne
	private Member member;
	@ManyToOne
	private Store store;
	@Builder
	public Review(String contents, int score, Date writeday, Member member, Store store) {
		super();
		this.contents = contents;
		this.score = score;
		this.writeday = writeday;
		this.member = member;
		this.store = store;
	}
}
