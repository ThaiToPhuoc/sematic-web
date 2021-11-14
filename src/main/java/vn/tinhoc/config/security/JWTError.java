package vn.tinhoc.config.security;

public enum JWTError {
	SIGNATURE("Invalid JWT signature"),
	MALFORMED("Invalid JWT token"),
	EXPIRED("JWT token is expired"),
	UNSUPPORTED("JWT token is unsupported"),
	ILLEGAL("JWT claims string is empty");
	
	private String message;
	private JWTError(String message) {
		this.message = message;
	}
	
	public String message() {
		return this.message;
	}
}
