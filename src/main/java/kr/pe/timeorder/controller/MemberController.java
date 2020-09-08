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

import kr.pe.timeorder.exception.NotFoundException;
import kr.pe.timeorder.exception.PermissionException;
import kr.pe.timeorder.exception.TokenException;
import kr.pe.timeorder.model.Member;
import kr.pe.timeorder.repository.AddressRepository;
import kr.pe.timeorder.repository.MemberRepository;
import kr.pe.timeorder.service.JwtService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MemberController {
	@Autowired
	private MemberRepository mRepository;
	@Autowired
	private AddressRepository aRepositoty;
	@Autowired
	private JwtService jwtService;

	// 전체 멤버
	// 관리자만 가능
	@GetMapping("/members")
	public ResponseEntity<List<Member>> allMembers(HttpServletRequest req) {
		log.info("---- allMembers () -----------------");
		HttpStatus status = null;
		Member member = null;
		List<Member> list = null;
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap) (jwtService.get(token).get("Member"));

			member = mRepository.findMemberByPhone((String) l.get("phone"))
					.orElseThrow(() -> new NotFoundException("member"));

			if (member.getAuthor() != 2) {
				throw new PermissionException();
			}
			list = mRepository.findAll();
			status = HttpStatus.ACCEPTED;
		} catch (RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Member>>(list, status);
	}

	// 멤버 추가
	@PostMapping("/members")
	public ResponseEntity<Member> newMember(@RequestBody Member newMember) {
		log.info("---- newMembers () -----------------");
		if (newMember.isVaild()) {
			if (newMember.getAddress() != null && aRepositoty.findById(newMember.getAddress().getId()).get() == null) {
				aRepositoty.save(newMember.getAddress());
			}

			mRepository.save(newMember);
			return new ResponseEntity<Member>(newMember, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<Member>(newMember, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// memberId와 일치하는 멤버 가져오기
	// memberId로 로그인한 계정과 관리자만 가능
	@GetMapping("/members/{memberId}")
	public ResponseEntity<Member> oneMember(@PathVariable long memberId, HttpServletRequest req) {
		log.info("---- oneMember () -----------------");
		HttpStatus status = null;
		Member member = null;
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap) (jwtService.get(token).get("Member"));

			member = mRepository.findMemberByPhone((String) l.get("phone"))
					.orElseThrow(() -> new NotFoundException("member"));
			if (member.getAuthor() == 2 || member.getMemberId() == memberId) {
				status = HttpStatus.ACCEPTED;
			} else {
				member = null;
				throw new PermissionException();
			}
		} catch (RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Member>(member, status);
	}

	// 멤버수정
	// 관리자하고 memberId로그인한 계정만 가능
	@PutMapping("/members/{memberId}")
	public ResponseEntity<Member> replaceMember(HttpServletRequest req, @RequestBody Member newMember,
			@PathVariable long memberId) {
		log.info("---- replaceMember () -----------------");
		HttpStatus status = null;
		Member member = null;
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap) (jwtService.get(token).get("Member"));

			member = mRepository.findMemberByPhone((String) l.get("phone"))
					.orElseThrow(() -> new NotFoundException("member"));

			if (member.getAuthor() == 2 || member.getMemberId() == memberId) {
				member.setPhone(newMember.getPhone());
				member.setPw(newMember.getPw());
				member.setName(newMember.getName());
				member.setAuthor(newMember.getAuthor());

				if (newMember.getAddress() != null
						&& aRepositoty.findById(newMember.getAddress().getId()).get() == null) {
					aRepositoty.save(newMember.getAddress());
				}
				member.setAddress(newMember.getAddress());

				mRepository.save(member);
				status = HttpStatus.ACCEPTED;
			} else {
				throw new PermissionException();
			}
		} catch (RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Member>(member, status);
	}

	// member 삭제
	// 본인 , 관리자만 가능
	@DeleteMapping("/members/{memberId}")
	public ResponseEntity<Member> deleteMember(HttpServletRequest req, @PathVariable long memberId) {
		log.info("---- deleteMember () -----------------");
		HttpStatus status = null;
		Member member = null;
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap) (jwtService.get(token).get("Member"));

			member = mRepository.findMemberByPhone((String) l.get("phone"))
					.orElseThrow(() -> new NotFoundException("member"));
			if (member.getAuthor() == 2 || member.getMemberId() == memberId) {
				mRepository.deleteById(memberId);
				status = HttpStatus.ACCEPTED;
			} else {
				throw new PermissionException();
			}
		} catch (RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Member>(member, status);
	}

	@PostMapping("/members/signin")
	public ResponseEntity<Map<String, Object>> signin(@RequestBody Member user, HttpServletResponse res) {
		log.info("--- signin () ---");
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;

		try {
			if (user.getPhone().length() == 0 || user.getPw().length() == 0) {
				throw new RuntimeException();
			}

			Member loginMember = mRepository.findMemberByPhoneAndPw(user.getPhone(), user.getPw())
					.orElseThrow(() -> new NotFoundException("member"));
			System.out.println(loginMember);
			// 로그인 성공시 토큰 생성
			String token = jwtService.create(loginMember);

			// 토큰 정보는 response의 헤더로 보내고 나머지는 map에 담아두기
			res.setHeader("jwt-auth-token", token);

			System.out.println("생성된 token -- " + token);

			resultMap.put("auth_token", token);
			resultMap.put("status", true);
			resultMap.put("data", loginMember);
			status = HttpStatus.ACCEPTED;

		} catch (RuntimeException e) {
			log.error("로그인 실패", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		log.info("**************************" + resultMap);

		// 리턴시 map과 상태 메세지를 함께 전송
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

}