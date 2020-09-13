package kr.pe.timeorder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.pe.timeorder.model.Item;
import kr.pe.timeorder.model.Member;
import kr.pe.timeorder.model.Store;
import kr.pe.timeorder.model.UploadFile;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long>{
	Optional<UploadFile> findUploadFileByItem(Item item);
	
	List<UploadFile> findUploadFileByStore(Store store);
}
