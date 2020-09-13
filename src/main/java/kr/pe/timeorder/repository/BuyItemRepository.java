package kr.pe.timeorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.pe.timeorder.model.BuyItem;
import kr.pe.timeorder.model.Member;
import kr.pe.timeorder.model.Store;

public interface BuyItemRepository extends JpaRepository<BuyItem, Long>{
	public List<BuyItem> findBuyItemByStore(Store store);
	public List<BuyItem> findBuyItemByMember(Member member);
	
}
