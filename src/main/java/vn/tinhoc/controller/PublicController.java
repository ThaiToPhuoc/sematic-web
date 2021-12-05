package vn.tinhoc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.tinhoc.domain.Chuong;
import vn.tinhoc.domain.Tiet;
import vn.tinhoc.domain.dto.BaiGiangDTO;
import vn.tinhoc.domain.dto.CauHoiDTO;
import vn.tinhoc.domain.dto.NopBaiDTO;
import vn.tinhoc.repository.CauHoiRepository;
import vn.tinhoc.service.PublicService;

import static vn.tinhoc.utils.DataUtils.escapeMetaCharacters;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/public")
public class PublicController {
	@Autowired
	PublicService publicService;
	
	@Autowired
	CauHoiRepository cauHoiRepository;
	
	@GetMapping("/cau-hoi/{id}")
	public ResponseEntity<?> listCauHoi(@PathVariable String id) {
		
		List<CauHoiDTO> cauhoi = publicService.listCauHoi(id);
		return cauhoi != null
				? new ResponseEntity<>(cauhoi, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
	
	@GetMapping("/tiet/{id}")
	public ResponseEntity<?> findTietById(@PathVariable String id) {
		Tiet tiet = publicService.findTietById(id);
		return tiet != null
				? new ResponseEntity<>(tiet, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/bai-giang/{id}")
	public ResponseEntity<?> findBaiGiangById(@PathVariable String id) {
		BaiGiangDTO baiGiangdto = publicService.findBaiGiangById(id);
		return baiGiangdto != null
				? new ResponseEntity<>(baiGiangdto, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/bai-giang")
	public ResponseEntity<?> listBaiGiang() {
		
		return new ResponseEntity<>(publicService.listBaiGiang(), HttpStatus.OK);
	}

	@PostMapping("/nop-bai")
	public ResponseEntity<?> nopBai(@RequestBody List<NopBaiDTO> nopBaiDTOs,
									@RequestParam String user,
									@RequestParam String ktra){
		return ResponseEntity.ok(publicService.nopBai(nopBaiDTOs, user, ktra));
	}

	@GetMapping("/ket-qua/{username}")
	public ResponseEntity<?> xemKetQua(@PathVariable String username) {
		return ResponseEntity.ok(publicService.findKQByUsername(username));
	}

	@PostMapping("/search/basic")
	public ResponseEntity<?> searchBasic(@RequestBody Map<String, String> map) {
		return ResponseEntity.ok(publicService.basicSearch(
				escapeMetaCharacters(map.get("text")))
		);
	}
}
