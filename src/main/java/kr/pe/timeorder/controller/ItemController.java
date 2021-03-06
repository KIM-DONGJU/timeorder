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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.pe.timeorder.exception.InvalidException;
import kr.pe.timeorder.exception.NotFoundException;
import kr.pe.timeorder.exception.PermissionException;
import kr.pe.timeorder.exception.TokenException;
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
		return iRepository.findItemAll();
	}

	@GetMapping("/items-price")
	public List<Item> allByPrice() {
		return iRepository.findItemByPrice();
	}
	
	@GetMapping("/items-score")
	public List<Item> allByScore() {
		return iRepository.findItemByScore();
	}
	
	@GetMapping("/items-percent")
	public List<Item> allByPercent() {
		return iRepository.findItemByPercent();
	}

	@GetMapping("/items-price/{address}")
	public List<Item> allByPriceAddress(@PathVariable String address) {
		return iRepository.findItemByPriceAddress("%"+address+"%");
	}

	@GetMapping("/items-score/{address}")
	public List<Item> allByScoreAddress(@PathVariable String address) {
		System.out.println(address);
		return iRepository.findItemByScoreAddress("%"+address+"%");
	}
	
	@GetMapping("/items-percent/{address}")
	public List<Item> allByPercentAddress(@PathVariable String address) {
		return iRepository.findItemByPercentAddress("%"+address+"%");
	}
	
	@GetMapping("/items/name/{itemName}")
	public List<Item> storeItems(@PathVariable String itemName) {
		log.info("---- storeItems (name) -----------------");
		return iRepository.findItemByItemNameContaining(itemName);
	}

	@PostMapping("/items/store/{storeId}")
	public ResponseEntity<Item> newItem(HttpServletRequest req, @RequestBody Item newItem, @PathVariable long storeId) {
		log.info("---- newItem () -----------------");
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
			Store store = sRepository.findById(storeId).get();
			
			if (loginMember.getAuthor() != 1 || store.getMember().getMemberId() != loginMember.getMemberId()) {
				throw new PermissionException();
			}
			
			newItem.setStore(store);
			System.out.println(newItem);
			if (!newItem.isVaild()) {
				throw new InvalidException();
			}

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
	
	@PutMapping("/items/{itemId}")
	public ResponseEntity<Item> replaceStore(HttpServletRequest req ,@RequestBody Item newItem, @PathVariable long itemId) {
		HttpStatus status = null;
		Item item = null; 
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(token).get("Member"));
			
			Member loginMember = mRepository.findMemberByPhone((String)l.get("phone"))
					.orElseThrow(()-> new NotFoundException("member"));
			item = iRepository.findById(itemId).orElseThrow(() -> new NotFoundException());

			if (item.getStore().getMember().getMemberId() != loginMember.getMemberId()) {
				throw new PermissionException();
			}
			
			item.setItemCount(newItem.getItemCount());
			iRepository.save(item);
			status = HttpStatus.ACCEPTED;
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Item>(item, status);
	}

	@DeleteMapping("/items/{itemId}")
	public ResponseEntity<Item> deleteItem(HttpServletRequest req, @PathVariable long itemId) {
		log.info("---- deleteItem () -----------------");
		HttpStatus status = null;
		Member loginMember = null;
		Item item = null;
		
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(token).get("Member"));
			
			loginMember = mRepository.findMemberByPhone((String)l.get("phone"))
					.orElseThrow(()-> new NotFoundException("member"));
			item = iRepository.findById(itemId).get();
			Store store = item.getStore();
			
			if (loginMember.getAuthor() > 0 || loginMember.getMemberId() == store.getMember().getMemberId()) {
				iRepository.deleteById(itemId);
				status = HttpStatus.ACCEPTED;
			} else {
				throw new PermissionException();
			}
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Item>(item, status);
	}

}