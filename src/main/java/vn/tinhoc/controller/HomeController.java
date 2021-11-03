package vn.tinhoc.controller;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@GetMapping("/api")
	public ResponseEntity<?> home() {
		return ResponseEntity.ok(Instant.now().toString());
	}
}
