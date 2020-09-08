package kr.pe.timeorder.controller;

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
import kr.pe.timeorder.model.Store;
import kr.pe.timeorder.repository.AddressRepository;
import kr.pe.timeorder.repository.MemberRepository;
import kr.pe.timeorder.repository.StoreRepository;
import kr.pe.timeorder.service.JwtService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class StoreController {
	@Autowired
	private StoreRepository sRepository;
	
	@Autowired
	private MemberRepository mRepository;
	
	@Autowired
	private AddressRepository aRepositoty;

	@Autowired
	private JwtService jwtService;
	

	@GetMapping("/stores")
	public ResponseEntity<List<Store>> all(HttpServletRequest req) {
		HttpStatus status = null;
		List<Store> list = null;
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(token).get("Member"));
			
			Member loginMember = mRepository.findMemberByPhone((String)l.get("phone"))
					.orElseThrow(()-> new NotFoundException("member"));
			
			if (loginMember.getAuthor() != 2) {
				throw new PermissionException();
			}
			
			list = sRepository.findAll();
			status = HttpStatus.ACCEPTED;
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Store>>(list, status);
	}
	
	@GetMapping("/stores/{loginMemberId}")
	public ResponseEntity<List<Store>> ownerStores(HttpServletRequest req, @PathVariable long loginMemberId) {
		HttpStatus status = null;
		List<Store> list = null;
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(token).get("Member"));
			
			Member loginMember = mRepository.findMemberByPhone((String)l.get("phone"))
					.orElseThrow(()-> new NotFoundException("member"));
			Member findMember = mRepository.findById(loginMemberId).get();
			
			if (loginMember.getAuthor() > 0 || loginMember.getMemberId() == loginMemberId) {
				list = sRepository.findStoreByMember(findMember);
				status = HttpStatus.ACCEPTED;
			} else {
				throw new PermissionException();
			}
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Store>>(list, status);
	}

	@PostMapping("/stores")
	public ResponseEntity<Store> newStore(HttpServletRequest req, @RequestBody Store newStore) {
		HttpStatus status = null;
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(token).get("Member"));
			
			Member loginMember = mRepository.findMemberByPhone((String)l.get("phone"))
					.orElseThrow(()-> new NotFoundException("mission"));
			
			if (loginMember.getAuthor() != 1) {
				throw new PermissionException();
			}
			
			newStore.setMember(loginMember);

			if (!newStore.isVaild()) {
				throw new InvalidException();
			}
			
			if (newStore.getAddress() != null && aRepositoty.findById(newStore.getAddress().getId()).get() == null) {
				aRepositoty.save(newStore.getAddress());
			}
			sRepository.save(newStore);
			status = HttpStatus.ACCEPTED;
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Store>(newStore, status);
	}

	@GetMapping("/stores/{storeId}")
	public Store one(@PathVariable long storeId) {
		return sRepository.findById(storeId).get();
	}

	@PutMapping("/stores/{storeId}")
	public ResponseEntity<Store> replaceStore(HttpServletRequest req ,@RequestBody Store newStore, @PathVariable long storeId) {
		HttpStatus status = null;
		Store store = null; 
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(token).get("Member"));
			
			Member loginMember = mRepository.findMemberByPhone((String)l.get("phone"))
					.orElseThrow(()-> new NotFoundException("member"));
			store = sRepository.findById(storeId).get();
			
			if (loginMember.getAuthor() != 1 && loginMember.getMemberId() != store.getMember().getMemberId()) {
				throw new PermissionException();
			}
			
			store.setMember(newStore.getMember());
			store.setStoreInfo(newStore.getStoreInfo());
			store.setStoreName(newStore.getStoreName());
			store.setStoreNum(newStore.getStoreNum());
			if (newStore.getAddress() != null && aRepositoty.findById(newStore.getAddress().getId()).get() == null) {
				aRepositoty.save(newStore.getAddress());
			}
			store.setAddress(newStore.getAddress());
			sRepository.save(store);
			status = HttpStatus.ACCEPTED;
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Store>(store, status);
	}

	@DeleteMapping("/stores/{storeId}")
	public ResponseEntity<Store> deleteStore(HttpServletRequest req, @PathVariable long storeId) {
		log.info("---- deleteStore () -----------------");
		HttpStatus status = null;
		Member loginMember = null;
		Store store = null;
		
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(token).get("Member"));
			
			loginMember = mRepository.findMemberByPhone((String)l.get("phone"))
					.orElseThrow(()-> new NotFoundException("member"));
			store = sRepository.findById(storeId).get();
			
			if (loginMember.getAuthor() > 0 || loginMember.getMemberId() == store.getMember().getMemberId()) {
				sRepository.deleteById(storeId);
				status = HttpStatus.ACCEPTED;
			} else {
				throw new PermissionException();
			}
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Store>(store, status);
	}

}