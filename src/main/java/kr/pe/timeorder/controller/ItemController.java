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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.pe.timeorder.model.Item;
import kr.pe.timeorder.model.Member;
import kr.pe.timeorder.model.Store;
import kr.pe.timeorder.repository.ItemRepository;
import kr.pe.timeorder.repository.MemberRepository;
import kr.pe.timeorder.repository.StoreRepository;
import kr.pe.timeorder.service.JwtService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ItemController {
	@Autowired
	private ItemRepository iRepository;
	@Autowired
	private MemberRepository mRepository;
	@Autowired
	private StoreRepository sRepository;
	@Autowired
	private JwtService jwtService;

	@GetMapping("/items")
	public List<Item> all() {
		return iRepository.findAll();
	}
	
	@GetMapping("/items/{storeId}")
	public List<Item> storeItems(@PathVariable long storeId) {
		return iRepository.findItemByStore(sRepository.findById(storeId).get());
	}
	
	@GetMapping("/items/{itemName}")
	public List<Item> storeItems(@PathVariable String itemName) {
		return iRepository.findItemByItemNameContaining(itemName);
	}

	@PostMapping("/items/{storeId}")
	public ResponseEntity<Item> newItem(HttpServletRequest req, @RequestBody Item newItem, @PathVariable long storeId) {
		HttpStatus status = null;
		try {
			jwtService.checkValid(req.getHeader("jwt-auth-token"));
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(req.getHeader("jwt-auth-token")).get("Member"));
			Member loginMember = mRepository.findMemberByPhone((String)l.get("phone"));
			Store store = sRepository.findById(storeId).get();
			
			if (loginMember.getAuthor() != 1 || store.getMember().getMemberId() != loginMember.getMemberId()) {
				throw new RuntimeException();
			}
			
			newItem.setStore(store);
			iRepository.save(newItem);
			status = HttpStatus.ACCEPTED;
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Item>(newItem, status);
	}

	@GetMapping("/items/{itemId}")
	public Item one(@PathVariable long itemId) {
		return iRepository.findById(itemId).get();
	}

	@DeleteMapping("/items/{itemId}")
	public ResponseEntity<Item> deleteItem(HttpServletRequest req, @PathVariable long itemId) {
		log.info("---- updateEmployees () -----------------");
		HttpStatus status = null;
		Member loginMember = null;
		Item item = null;
		
		try {
			jwtService.checkValid(req.getHeader("jwt-auth-token"));
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(req.getHeader("jwt-auth-token")).get("Member"));
			loginMember = mRepository.findMemberByPhone((String)l.get("phone"));
			item = iRepository.findById(itemId).get();
			Store store = item.getStore();
			if (loginMember.getAuthor() > 0 || loginMember.getMemberId() == store.getMember().getMemberId()) {
				iRepository.deleteById(itemId);
				status = HttpStatus.ACCEPTED;
			} else {
				throw new RuntimeException();
			}
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Item>(item, status);
	}

}