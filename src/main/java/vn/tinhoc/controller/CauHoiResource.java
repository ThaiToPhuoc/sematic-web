package vn.tinhoc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.tinhoc.domain.CauHoi;
import vn.tinhoc.repository.CauHoiRepository;

@RestController
@RequestMapping("/api")
public class CauHoiResource {
	
	@Autowired
	private CauHoiRepository cauHoiRepository;
	
	@GetMapping("/cau-hoi")
	public ResponseEntity<List<CauHoi>> getCauHoi() {
		return ResponseEntity.ok(cauHoiRepository.find());
	}
}
