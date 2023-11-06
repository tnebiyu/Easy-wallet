package com.nebiyu.Kelal.configuration;

import com.nebiyu.Kelal.admin.model.Admin;
import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.super_admin.model.SuperAdminModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
@Value("${jwtSecret}")
    private  String SECRET_KEY;


    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);

    }
    public String generateToken(Admin admin) {
        return generateToken(new HashMap<>(), admin);

    }
    public String generateToken(SuperAdminModel superAdmin){
        return generateToken(new HashMap<>(), superAdmin);

    }
//

    public String generateToken(Map<String, Object> extractClaims, User user
    ) {
        extractClaims.put("email", user.getEmail());
        extractClaims.put("id", user.getId());
        return Jwts.builder().setClaims(extractClaims).setSubject(user.getEmail()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 * 24 * 60)).signWith(
                getSignInKey(), SignatureAlgorithm.HS256
        ).compact();
    }
    public String generateToken(Map<String, Object> extractClaims, Admin admin
    ) {
        extractClaims.put("email", admin.getEmail());
        extractClaims.put("id", admin.getId());
        return Jwts.builder().setClaims(extractClaims).setSubject(admin.getEmail()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 * 24 * 60)).signWith(
                getSignInKey(), SignatureAlgorithm.HS256
        ).compact();
    }
    public String generateToken(Map<String, Object> extractClaims, SuperAdminModel superAdmin
    ) {
        extractClaims.put("email", superAdmin.getEmail());
        extractClaims.put("id", superAdmin.getId());
        return Jwts.builder().setClaims(extractClaims).setSubject(superAdmin.getEmail()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 * 24 * 60)).signWith(
                getSignInKey(), SignatureAlgorithm.HS256
        ).compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().
                setSigningKey(getSignInKey())
                .build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims verify(String authentication) throws IOException {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authentication).getBody();
          //  System.out.println(claims.get("email") + " " + claims.get("password"));
return claims;
        } catch (Exception e) {
            throw new AccessDeniedException("Access denied");
        }
    }
    public Claims verify2(String authentication) throws ExpiredJwtException, AccessDeniedException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(authentication)
                    .getBody();


            Date expirationDate = claims.getExpiration();
            Date now = new Date();

            if (expirationDate != null && expirationDate.before(now)) {
                throw new ExpiredJwtException(null, claims, "Token has expired");
            }

            return claims;
        } catch (ExpiredJwtException e) {
            throw e; // Rethrow the exception to handle token expiration separately
        } catch (Exception e) {
            throw new AccessDeniedException("Access denied");
        }
    }


}

