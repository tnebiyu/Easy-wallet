//package com.nebiyu.Kelal.services.util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.crypto.spec.SecretKeySpec;
//import java.security.Key;
//import java.util.Date;
//
//@Configuration
//public class JwtUtil {
//    @Value("${jwt.secret}")
//    private String jwtSecret;
//
//    @Value("${jwt.expiration}")
//    private long jwtExpiration;
//
//    @Bean
//    public Key secretKey() {
//        return new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
//    }
//
//    public String generateToken(String subject) {
//        Date now = new Date();
//        Date expirationDate = new Date(now.getTime() + jwtExpiration);
//
//        return Jwts.builder()
//                .setSubject(subject)
//                .setIssuedAt(now)
//                .setExpiration(expirationDate)
//                .signWith(secretKey())
//                .compact();
//    }
//
//    public Claims getClaimsFromToken(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}
//
//
//
////package com.nebiyu.Kelal.services.util;
////
////import io.jsonwebtoken.Claims;
////import io.jsonwebtoken.Jwts;
////import io.jsonwebtoken.SignatureAlgorithm;
////import io.jsonwebtoken.security.Keys;
////import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.stereotype.Component;
////
////import java.security.Key;
////import java.util.Date;
////import java.util.HashMap;
////import java.util.Map;
////import java.util.function.Function;
////
////@Component
////public class JwtUtil {
////public static final String SECRETE= "kelal";
////
////
////public String ExtractUsername(String token){
////    return extractClaim(token, Claims::getSubject);
////
////}
////    public Date extractExpiration(String token){
////        return extractClaim(token, Claims::getExpiration);
////
////    }
////    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
////    final Claims claims = extractAllClaims(token);
////    return claimsResolver.apply(claims);
////    }
////    private Claims extractAllClaims(String token){
////    return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
////    }
////    private Boolean isTokenExpired(String token, UserDetails userDetails){
////    return extractExpiration(token).before(new Date());
////    }
////
////   public String generateToken(String username){
////       Map<String, Object> claims = new HashMap<>();
////         return createToken(claims, username);
////   }
////   private String createToken(Map<String, Object> claims, String userName){
////    return Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(new Date(System.currentTimeMillis()))
////            .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
////            .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
////   }
////   private Key getSignKey(){
////    byte[] keyBytes = SECRETE.getBytes();
////    return Keys.hmacShaKeyFor(keyBytes);
////   }
////
////}
//
//
