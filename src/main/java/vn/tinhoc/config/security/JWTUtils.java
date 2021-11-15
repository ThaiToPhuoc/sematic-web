package vn.tinhoc.config.security;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWTUtils {
	private static final Logger log = LoggerFactory.getLogger(JWTUtils.class);
	
	@Value("${spring.application.jwt-secret}")
	private String jwtSecret;

	@Value("${spring.application.jwt-expire}")
	private int jwtExpirationMs;
	
	private final String ROLE_CLAIM_NAME = "roles";

	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder()
				.claim(ROLE_CLAIM_NAME, userPrincipal.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList()))
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public List<String> getRoles(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get(ROLE_CLAIM_NAME, List.class);
	}

	public Pair<JWTError, Boolean> validateJwtToken(String authToken) {
		JWTError error = null;
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return Pair.of(error, true);
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
			error = JWTError.SIGNATURE;
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
			error = JWTError.MALFORMED;
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
			error = JWTError.EXPIRED;
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
			error = JWTError.UNSUPPORTED;
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
			error = JWTError.ILLEGAL;
		}

		return Pair.of(error, false);
	}
}
