package kr.pe.timeorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.pe.timeorder.model.Item;
import kr.pe.timeorder.model.Store;

public interface ItemRepository extends JpaRepository<Item, Long>{
	public List<Item> findItemByStore(Store store);
	public List<Item> findItemByItemNameContaining(@Param("address") String itemName);
	@Query("select i from Item as i, Store as s where i.store = s and s.address_name like :address and i.itemCount > 0 order by (i.beforePrice-i.afterPrice)/i.beforePrice desc")
	public List<Item> findItemByPercentAddress(@Param("address") String address_name); 
	@Query("select i from Item as i order by (i.beforePrice-i.afterPrice)/i.beforePrice desc")
	public List<Item> findItemByPercent();
	@Query("select i from Item as i, Store as s where i.store = s and s.address_name like :address and i.itemCount > 0 order by s.score desc")
	public List<Item> findItemByScoreAddress(@Param("address") String address_name);
	@Query("select i from Item as i, Store as s where i.store = s order by s.score desc")
	public List<Item> findItemByScore();
	@Query("select i from Item as i, Store as s where i.store = s and s.address_name like :address and i.itemCount > 0 order by i.afterPrice asc")
	public List<Item> findItemByPriceAddress(@Param("address") String address_name);
	@Query("select i from Item as i where i.itemCount > 0 order by i.afterPrice asc")
	public List<Item> findItemByPrice();
	@Query("select i from Item as i where i.itemCount > 0")
	public List<Item> findItemAll();
	
}
