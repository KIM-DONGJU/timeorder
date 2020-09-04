package kr.pe.timeorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.pe.timeorder.model.Member;
import kr.pe.timeorder.model.Store;

public interface StoreRepository extends JpaRepository<Store, Long>{
	public List<Store> findStoreByMember(Member member);
}
