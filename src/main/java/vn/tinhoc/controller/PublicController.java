package vn.tinhoc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.tinhoc.PublicService;
import vn.tinhoc.domain.CauHoi;
import vn.tinhoc.repository.CauHoiRepository;

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
		
		return new ResponseEntity<>(cauHoiRepository.find(), HttpStatus.OK);
	}
	
	@PostMapping("/cau-hoi")
	public ResponseEntity<?> save(@RequestBody CauHoi cauHoi) {
		if(publicService.create(cauHoi) == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
