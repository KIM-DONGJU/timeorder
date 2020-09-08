package kr.pe.timeorder.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.pe.timeorder.exception.NotFoundException;
import kr.pe.timeorder.model.Item;
import kr.pe.timeorder.model.Member;
import kr.pe.timeorder.model.Review;
import kr.pe.timeorder.model.Store;
import kr.pe.timeorder.model.UploadFile;
import kr.pe.timeorder.repository.ItemRepository;
import kr.pe.timeorder.repository.MemberRepository;
import kr.pe.timeorder.repository.ReviewRepository;
import kr.pe.timeorder.repository.StoreRepository;
import kr.pe.timeorder.repository.UploadFileRepository;
import kr.pe.timeorder.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FileController {
	@Autowired
	private UploadFileRepository fRepository;
	
	@Autowired
	private MemberRepository mRepository;
	
	@Autowired
	private StoreRepository sRepository;
	
	@Autowired
	private ItemRepository iRepository;
	
	@Autowired
	private ReviewRepository rRepository;
	
	@Autowired
	private FileStorageService fileService;
	
	@PostMapping("/upload/member/{id}")
	public ResponseEntity<UploadFile> uploadMemberFile(@PathVariable long id, @RequestParam("file") MultipartFile file) {
		UploadFile uploadFile = null;
		HttpStatus status = null;
		try {
			String fileName = fileService.storeFile(file);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
								.path("/post")
								.path("/downloadFile/")
								.path(fileName)
								.toUriString();
			uploadFile = UploadFile.builder().fileName(fileName).fileDownloadUri(fileDownloadUri).fileType(file.getContentType()).fileSize(file.getSize()).build();
			Member member = mRepository.findById(id).orElseThrow(()-> new NotFoundException());
			uploadFile.setMember(member);
			fRepository.save(uploadFile);
			status = HttpStatus.ACCEPTED;
		} catch (RuntimeException e) {
			log.error("uploadMemberFile error", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<UploadFile>(uploadFile, status);
	}
	
	@PostMapping("/upload/item/{id}")
	public ResponseEntity<UploadFile> uploadItemFile(@PathVariable long id, @RequestParam("file") MultipartFile file) {
		UploadFile uploadFile = null;
		HttpStatus status = null;
		try {
			String fileName = fileService.storeFile(file);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
								.path("/post")
								.path("/downloadFile/")
								.path(fileName)
								.toUriString();
			uploadFile = UploadFile.builder().fileName(fileName).fileDownloadUri(fileDownloadUri).fileType(file.getContentType()).fileSize(file.getSize()).build();
			Item item = iRepository.findById(id).orElseThrow(()-> new NotFoundException());
			uploadFile.setItem(item);
			fRepository.save(uploadFile);
			status = HttpStatus.ACCEPTED;
		} catch (RuntimeException e) {
			log.error("uploadMemberFile error", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<UploadFile>(uploadFile, status);
	}
	
	@PostMapping("/upload/review/{id}")
	public ResponseEntity<UploadFile> uploadReviewFile(@PathVariable long id, @RequestParam("file") MultipartFile file) {
		UploadFile uploadFile = null;
		HttpStatus status = null;
		try {
			String fileName = fileService.storeFile(file);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
								.path("/post")
								.path("/downloadFile/")
								.path(fileName)
								.toUriString();
			uploadFile = UploadFile.builder().fileName(fileName).fileDownloadUri(fileDownloadUri).fileType(file.getContentType()).fileSize(file.getSize()).build();
			Review review = rRepository.findById(id).orElseThrow(()-> new NotFoundException());
			uploadFile.setReview(review);
			fRepository.save(uploadFile);
			status = HttpStatus.ACCEPTED;
		} catch (RuntimeException e) {
			log.error("uploadMemberFile error", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<UploadFile>(uploadFile, status);
	}
	
	@PostMapping("/upload/store/{id}")
	public ResponseEntity<List<UploadFile>> uploadStoreFile(@PathVariable long id, @RequestParam("files") MultipartFile[] files) {
		UploadFile uploadFile = null;
		HttpStatus status = null;
		List<UploadFile> list = new ArrayList();
		String fileName = null;
		String fileDownloadUri = null;
		try {
			Store store = sRepository.findById(id).orElseThrow(()-> new NotFoundException());
			for (MultipartFile file : files) {
				fileName = fileService.storeFile(file);
				fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
								.path("/post")
								.path("/downloadFile/")
								.path(fileName)
								.toUriString();
				uploadFile = UploadFile.builder().fileName(fileName).fileDownloadUri(fileDownloadUri).fileType(file.getContentType()).fileSize(file.getSize()).build();
				uploadFile.setStore(store);
				fRepository.save(uploadFile);
				list.add(uploadFile);
			}
			status = HttpStatus.ACCEPTED;
		} catch (RuntimeException e) {
			log.error("uploadMemberFile error", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<List<UploadFile>>(list, status);
	}
}
