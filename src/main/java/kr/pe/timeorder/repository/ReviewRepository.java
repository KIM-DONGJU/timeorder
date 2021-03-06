package kr.pe.timeorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.pe.timeorder.model.Review;
import kr.pe.timeorder.model.Store;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	@Query("select r from Review as r where r.store=:s order by r.writeday desc")
	public List<Review> findReviewByStore(@Param("s") Store store);
	 
	@Query("select r from Review as r order by r.writeday desc")
	public List<Review> findAllByDate();
}
