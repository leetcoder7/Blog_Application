package com.blog.security;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

@Component
public class JwtTokenHelper {

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	private String secret = "jwtTokenKey";
	
	//retrieve username and jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	
	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	
	//for retrieving any information from token we will need the secret key
	@SuppressWarnings("deprecation")
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
	}
	
	
	//check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	
	//generate token for user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGeneratetoken(claims, userDetails.getUsername());
	}
	
	
	/*while creating the token -
	 * 1.Define claims of the token, like Issuer, Expiration, Subject, and the Id
	 * 2.sign the JWT using the HS512 algorithm and secret key.
	 * 3.According to JWS Compact Serialization (https://tools.ietf.org/html/draft-ieth-jose-)
	 * compaction of the JWT to a URL-safe String
	 */
	 @SuppressWarnings("deprecation")
	private String doGeneratetoken(Map<String,Object> claims, String subject) {
		 
		 return Jwts.builder().setClaims(claims).setSubject(subject) 
		         .setIssuedAt(new Date(System.currentTimeMillis())) 
		         .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)) 
		         .signWith(SignatureAlgorithm.HS512, secret).compact();
	 }
	 
	 //validate token
	 public boolean validatetoken(String token, UserDetails userDetails) {
		 
		 final String username = getUsernameFromToken(token);
		 return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	 }
}
