package vn.tinhoc.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.lanhoang.ontology.configuration.OntologyVariables;
import vn.lanhoang.ontology.model.QueryParam;
import vn.tinhoc.config.security.JWTUtils;
import vn.tinhoc.config.security.UserDetailsImpl;
import vn.tinhoc.domain.dto.LoginRequest;
import vn.tinhoc.domain.dto.LoginResponse;
import vn.tinhoc.domain.dto.Role;
import vn.tinhoc.domain.dto.User;
import vn.tinhoc.repository.RoleRepository;
import vn.tinhoc.repository.UserRepository;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	JWTUtils jwtUtils;
	
	@Autowired
	private OntologyVariables vars;
	
	@PostMapping("/login")
	@PermitAll
	public ResponseEntity<?> login(@RequestBody LoginRequest form) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		return ResponseEntity.ok(new LoginResponse(userDetails.getUsername(), token, roles));
	}
	
	@PostMapping("/register")
	@PermitAll
	public ResponseEntity<?> register(@RequestBody User user) {
		Optional<User> uOp = userRepository.findByPropertyValue("TenTaiKhoan", user.getTenTaiKhoan());
		if (uOp.isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			Optional<Role> op = roleRepository.findByUriTag("ROLE_STUDENT");
			if (op.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			List<Role> roles = List.of(op.get());
			user.setGomQuyen(roles);
			user.setId(user.getTenTaiKhoan());

			return ResponseEntity.ok(userRepository.save(user));
		}
	}
}
