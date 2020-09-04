package kr.pe.timeorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.pe.timeorder.model.Review;
import kr.pe.timeorder.model.Store;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	public List<Review> findReviewByStore(Store store);
}