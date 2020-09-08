package kr.pe.timeorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.pe.timeorder.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{
	
}
