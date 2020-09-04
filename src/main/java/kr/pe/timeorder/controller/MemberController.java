package kr.pe.timeorder.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import kr.pe.timeorder.model.Member;
import kr.pe.timeorder.repository.MemberRepository;
import kr.pe.timeorder.service.JwtService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MemberController {
	@Autowired
	private MemberRepository repository;
	
	@Autowired
	private JwtService jwtService;
	
	//전체 멤버
	//관리자만 가능
	@GetMapping("/members")
	public ResponseEntity<List<Member>> all(HttpServletRequest req) {
		HttpStatus status = null;
		Member member = null;
		List<Member> list = null;
		try {
			jwtService.checkValid(req.getHeader("jwt-auth-token"));
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(req.getHeader("jwt-auth-token")).get("Member"));
			member = repository.findMemberByPhone((String)l.get("phone"));
			if (member.getAuthor() != 2) {
				throw new RuntimeException();
			}
			System.out.println(member);
			status = HttpStatus.ACCEPTED;
			list = repository.findAll();
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Member>>(list, status);
	}
	
	//멤버 추가
	@PostMapping("/members")
	public Member newMember(@RequestBody Member newMember) {
		return repository.save(newMember);
	}
	
	//memberId와 일치하는 멤버 가져오기
	//memberId로 로그인한 계정과 관리자만 가능
	@GetMapping("/members/{memberId}")
	public ResponseEntity<Member> one(@PathVariable long memberId, HttpServletRequest req) {
		HttpStatus status = null;
		Member member = null;
		try {
			jwtService.checkValid(req.getHeader("jwt-auth-token"));
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(req.getHeader("jwt-auth-token")).get("Member"));
			member = repository.findMemberByPhone((String)l.get("phone"));
			if (member.getAuthor() == 2 || member.getMemberId() == memberId) {
				status = HttpStatus.ACCEPTED;
				System.out.println(member);
			} else {
				member = null;
				throw new RuntimeException();
			}
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Member>(member, status);
	}
	
	//멤버수정
	//관리자하고 memberId로그인한 계정만 가능
	@PutMapping("/members/{memberId}")
	public ResponseEntity<Member> replaceMember(HttpServletRequest req, @RequestBody Member newMember, @PathVariable long memberId) {
		HttpStatus status = null;
		Member member = null;
		try {
			jwtService.checkValid(req.getHeader("jwt-auth-token"));
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(req.getHeader("jwt-auth-token")).get("Member"));
			member = repository.findMemberByPhone((String)l.get("phone"));
			if (member.getAuthor() == 2 || member.getMemberId() == memberId) {
				member.setPhone(newMember.getPhone());
				member.setPw(newMember.getPw());
				member.setName(newMember.getName());
				member.setStores(newMember.getStores());
				member.setAuthor(newMember.getAuthor());
				member.setReviews(newMember.getReviews());
				repository.save(member);
				status = HttpStatus.ACCEPTED;
			} else {
				throw new RuntimeException();
			}
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Member>(member, status);
	}
	
	//member 삭제
	//본인 , 관리자만 가능
	@DeleteMapping("/members/{memberId}")
	public ResponseEntity<Member> deleteMember(HttpServletRequest req, @PathVariable long memberId) {
		log.info("---- updateEmployees () -----------------");
		HttpStatus status = null;
		Member member = null;
		try {
			jwtService.checkValid(req.getHeader("jwt-auth-token"));
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(req.getHeader("jwt-auth-token")).get("Member"));
			member = repository.findMemberByPhone((String)l.get("phone"));
			if (member.getAuthor() == 2 || member.getMemberId() == memberId) {
				repository.deleteById(memberId);
				status = HttpStatus.ACCEPTED;
			} else {
				throw new RuntimeException();
			}
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Member>(member, status);
	}
	
	@PostMapping("/members/signin")
	public ResponseEntity<Map<String, Object>> signin(@RequestBody Member user, HttpServletResponse res){
		System.out.println("signin");
		log.info("--- 로그인 버튼 클릭시에 실행되는 메소드 ---");
		System.out.println(user);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		
		try {
			Member loginMember = repository.findMemberByPhoneAndPw(user.getPhone(), user.getPw());
			System.out.println(loginMember);
			//로그인 성공시 토큰 생성
			String token = jwtService.create(loginMember);
			
			//토큰 정보는 response의 헤더로 보내고 나머지는 map에 담아두기
			res.setHeader("jwt-auth-token", token);
			
			System.out.println("생성된 token -- " + token);
			
			resultMap.put("auth_token", token);
			resultMap.put("status", true);
			resultMap.put("data", loginMember);
			status = HttpStatus.ACCEPTED;
		
		}catch(RuntimeException e) {
			log.error("로그인 실패", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		log.info("**************************" + resultMap);
		
		//리턴시 map과 상태 메세지를 함께 전송
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

}