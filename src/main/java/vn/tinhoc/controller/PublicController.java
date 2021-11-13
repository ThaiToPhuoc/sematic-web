package vn.tinhoc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.tinhoc.domain.BaiGiang;
import vn.tinhoc.domain.CauHoi;
import vn.tinhoc.domain.Chuong;
import vn.tinhoc.domain.dto.CauHoiDTO;
import vn.tinhoc.repository.CauHoiRepository;
import vn.tinhoc.service.PublicService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/public")
public class PublicController {
	@Autowired
	PublicService publicService;
	
	@Autowired
	CauHoiRepository cauHoiRepository;
	
	@GetMapping("/cau-hoi")
	public ResponseEntity<?> listCauHoi() {
		
		return new ResponseEntity<>(publicService.listCauHoi(), HttpStatus.OK);
	}
	
	@PostMapping("/cau-hoi")
	public ResponseEntity<?> save(@RequestBody CauHoiDTO cauHoiDTO) {
		
		return new ResponseEntity<>(publicService.create(cauHoiDTO), HttpStatus.OK);
	}
	
	@GetMapping("/chuong/{id}")
	public ResponseEntity<?> findChuongById(@PathVariable String id) {
		Chuong chuong = publicService.findChuongById(id);
		return chuong != null
				? new ResponseEntity<>(chuong, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/bai-giang/{id}")
	public ResponseEntity<?> findBaiGiangById(@PathVariable String id) {
		BaiGiang baiGiang = publicService.findBaiGiangById(id);
		return baiGiang != null
				? new ResponseEntity<>(baiGiang, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/bai-giang")
	public ResponseEntity<?> listBaiGiang() {
		
		return new ResponseEntity<>(publicService.listBaiGiang(), HttpStatus.OK);
	}
}
