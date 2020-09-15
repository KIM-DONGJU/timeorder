package kr.pe.timeorder.controller;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.pe.timeorder.exception.InvalidException;
import kr.pe.timeorder.exception.NotFoundException;
import kr.pe.timeorder.exception.TokenException;
import kr.pe.timeorder.model.BuyItem;
import kr.pe.timeorder.model.Item;
import kr.pe.timeorder.model.Member;
import kr.pe.timeorder.model.Store;
import kr.pe.timeorder.repository.BuyItemRepository;
import kr.pe.timeorder.repository.ItemRepository;
import kr.pe.timeorder.repository.MemberRepository;
import kr.pe.timeorder.repository.StoreRepository;
import kr.pe.timeorder.service.JwtService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BuyItemController {
	@Autowired
	private BuyItemRepository bRepository;
	@Autowired
	private ItemRepository iRepository;
	@Autowired
	private MemberRepository mRepository;
	@Autowired
	private StoreRepository sRepository;
	@Autowired
	private JwtService jwtService;
	
	@GetMapping("/buyitems")
	public List<BuyItem> allBuyItems() {
		log.info("---- allBuyItems () -----------------");
		return bRepository.findAll();
	}
	
	@GetMapping("/buyitems/store/{storeId}")
	public ResponseEntity<List<BuyItem>> storeBuyItems (@PathVariable long storeId) {
		log.info("---- storeBuyItems () -----------------");
		Store store = null;
		HttpStatus status = null;
		List<BuyItem> list = null; 
		try {
			store = sRepository.findById(storeId).orElseThrow(() -> new NotFoundException());
			list = bRepository.findBuyItemByStore(store);
			status = HttpStatus.ACCEPTED;
		} catch (RuntimeException e) {
			log.error("storeBuyItems error : ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<BuyItem>>(list, status);
	}
	
	@GetMapping("/buyitems/store/{memberId}")
	public ResponseEntity<List<BuyItem>> memberBuyItems (@PathVariable long memberId) {
		log.info("---- memberBuyItems () -----------------");
		Member member = null;
		HttpStatus status = null;
		List<BuyItem> list = null; 
		try {
			member = mRepository.findById(memberId).orElseThrow(() -> new NotFoundException());
			list = bRepository.findBuyItemByMember(member);
			status = HttpStatus.ACCEPTED;
		} catch (RuntimeException e) {
			log.error("storeBuyItems error : ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<BuyItem>>(list, status);
	}
	
	@PostMapping("/buyitems/item/{itemId}")
	public ResponseEntity<BuyItem> addBuyItem(HttpServletRequest req, @PathVariable long itemId) {
		log.info("---- newBuyItem () -----------------");
		HttpStatus status = null;
		BuyItem newBuyItem = null;
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(token).get("Member"));
			
			Member loginMember = mRepository.findMemberByPhone((String)l.get("phone"))
					.orElseThrow(()-> new NotFoundException("member"));
			Item item = iRepository.findById(itemId).orElseThrow(() -> new NotFoundException("아이템"));
			if (item.getItemCount() == 0) {
				throw new InvalidException();
			}
			Store store = item.getStore();
			item.setItemCount(item.getItemCount()-1);
			iRepository.save(item);
			newBuyItem = BuyItem.builder().item(item).store(store).member(loginMember).build();
			bRepository.save(newBuyItem);
			status = HttpStatus.ACCEPTED;
		} catch(RuntimeException e) {
			log.error("정보 조회 실패 ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<BuyItem>(newBuyItem, status);
	}
	
	@GetMapping("/buyitems/{buyItemId}")
	public BuyItem oneBuyItems(@PathVariable long buyItemId) {
		log.info("---- oneBuyItems (id) -----------------");
		return bRepository.findById(buyItemId).orElseThrow(() -> new NotFoundException());
	}
	
	@DeleteMapping("/buyitems/{buyItemId}")
	public ResponseEntity<BuyItem> deleteBuyItems(HttpServletRequest req, @PathVariable long buyItemId) {
		log.info("---- deleteBuyItems (id) -----------------");
		HttpStatus status = null;
		BuyItem buyItem = null;
		try {
			String token = req.getHeader("jwt-auth-token");
			if (token == null || token.length() == 0) {
				throw new TokenException();
			}
			jwtService.checkValid(token);
			LinkedHashMap l = (LinkedHashMap)(jwtService.get(token).get("Member"));
			
			Member loginMember = mRepository.findMemberByPhone((String)l.get("phone"))
					.orElseThrow(()-> new NotFoundException("member"));
			buyItem = bRepository.findById(buyItemId).orElseThrow(() -> new NotFoundException());
			
			if (loginMember.getMemberId() != buyItem.getStore().getMember().getMemberId()) {
				throw new InvalidException();
			}
			
			bRepository.delete(buyItem);
			status = HttpStatus.ACCEPTED;
		} catch(RuntimeException e) {
			log.error("deleteBuyItems error : ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<BuyItem>(buyItem, status);
	}
}
