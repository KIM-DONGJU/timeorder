package kr.pe.timeorder.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.pe.timeorder.exception.InvalidException;
import kr.pe.timeorder.exception.NotFoundException;
import kr.pe.timeorder.exception.PermissionException;
import kr.pe.timeorder.exception.TokenException;
import kr.pe.timeorder.model.Member;
import kr.pe.timeorder.model.Review;
import kr.pe.timeorder.model.Store;
import kr.pe.timeorder.repository.MemberRepository;
import kr.pe.timeorder.repository.ReviewRepository;
import kr.pe.timeorder.repository.StoreRepository;
import kr.pe.timeorder.service.JwtService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ReviewController {
	@Autowired
	private ReviewRepository rRepository;
	@Autowired
	private MemberRepository mRepository;
	@Autowired
	private StoreRepository sRepository;
	@Autowired
	private JwtService jwtService;

	@GetMapping("/reviews")
	public List<Review> allReviews() {
		log.info("---- allReviews () -----------------");
		return rRepository.findAll();
	}
	
	@GetMapping("/reviews/{storeId}")
	public List<Review> storeReviews(@PathVariable long storeId) {
		log.info("---- storeReviews () -----------------");
		return rRepository.findReviewByStore(sRepository.findById(storeId).get());
	}
	
	@PostMapping("/reviews/{storeId}")
	public ResponseEntity<Review> newReview(HttpServletRequest req, @RequestBody Review newReview, @PathVariable long storeId) {
		log.info("---- newReview () -----------------");
		HttpStatus status = null;
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(token).get("Member"));
			
			Member loginMember = mRepository.findMemberByPhone((String)l.get("phone"))
					.orElseThrow(()-> new NotFoundException("member"));
			
			
			newReview.setStore(sRepository.findById(storeId).get());
			newReview.setMember(loginMember);
			
			if (!newReview.isVaild()) {
				throw new InvalidException();
			}
			
			rRepository.save(newReview);
			status = HttpStatus.ACCEPTED;
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Review>(newReview, status);
	}

	@GetMapping("/reviews/{reviewId}")
	public Review oneReview(@PathVariable long reviewId) {
		log.info("---- oneReview () -----------------");
		return rRepository.findById(reviewId).get();
	}

	@PutMapping("/reviews/{reviewId}")
	public ResponseEntity<Review> replaceReview(HttpServletRequest req, @RequestBody Review newReview, @PathVariable long reviewId) {
		log.info("---- replaceReview () -----------------");
		HttpStatus status = null;
		Review review = null;
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(token).get("Member"));
			
			Member loginMember = mRepository.findMemberByPhone((String)l.get("phone"))
					.orElseThrow(()-> new NotFoundException("member"));
			review = rRepository.findById(reviewId).get();
			
			if (loginMember.getMemberId() != review.getMember().getMemberId()) {
				throw new PermissionException();
			}
			review.setContents(newReview.getContents());
			review.setMember(newReview.getMember());
			review.setScore(newReview.getScore());
			review.setStore(newReview.getStore());
			review.setWriteday();
			rRepository.save(review);
			status = HttpStatus.ACCEPTED;
			
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Review>(review, status);
	}

	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<Review> deleteReview(HttpServletRequest req, @PathVariable long reviewId) {
		log.info("---- deleteReview () -----------------");
		HttpStatus status = null;
		Review review = null;
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(token).get("Member"));
			
			Member loginMember = mRepository.findMemberByPhone((String)l.get("phone"))
					.orElseThrow(()-> new NotFoundException("member"));
			review = rRepository.findById(reviewId).get();
			
			if (loginMember.getAuthor() == 2 || loginMember.getMemberId() == review.getMember().getMemberId()) {
				rRepository.deleteById(reviewId);
			} else {
				throw new PermissionException();
			}
			status = HttpStatus.ACCEPTED;
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Review>(review, status);
	}

}