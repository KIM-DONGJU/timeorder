package kr.pe.timeorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.pe.timeorder.model.Item;
import kr.pe.timeorder.model.Store;

public interface ItemRepository extends JpaRepository<Item, Long>{
	public List<Item> findItemByStore(Store store);
	public List<Item> findItemByItemNameContaining(String itemName);
}
