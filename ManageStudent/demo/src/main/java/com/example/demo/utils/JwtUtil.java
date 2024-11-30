package com.example.demo.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil {

  @Value("${jwt.secret_key}")
  private String SECRET_KEY;

  @Value("${jwt.time_expiration}")
  private Long TIME_EXPIRATION;

  public String extractUsername(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
  }

  public Date extractExpiration(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
  }

  public Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claim = new HashMap<>();
    claim.put("username", userDetails.getUsername());
    return Jwts.builder().setSubject(userDetails.getUsername())
        .setClaims(claim)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + TIME_EXPIRATION * 10))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
  }

}
