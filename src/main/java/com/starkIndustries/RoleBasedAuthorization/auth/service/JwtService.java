package com.starkIndustries.RoleBasedAuthorization.auth.service;

import com.starkIndustries.RoleBasedAuthorization.auth.modles.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

import static org.springframework.cache.interceptor.SimpleKeyGenerator.generateKey;

@Slf4j
@Service
public class JwtService {

    private String secretKey = "";

    public JwtService(){

        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance(com.starkIndustries.RoleBasedAuthorization.keys.Keys.HMAC_SHA_256);
            SecretKey sk = keyGenerator.generateKey();
            secretKey= Base64.getEncoder().encodeToString(sk.getEncoded());
        }catch (Exception e){
            log.error("Key generation exception: {}",e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public String getJwtToken(Employee employee){

        Map<String, Objects> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(employee.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+(1000*60*60*24)))
                .and()
                .signWith(generateKey())
                .compact();

    }

    public SecretKey generateKey(){
        byte[] data = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(data);
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);

    }

    private <T> T extractClaims(String token, Function<Claims,T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        final boolean isExpired = isTokenExpired(token);

        log.info("Validating token for user...");
        log.info("→ Extracted username from token : {}", userName);
        log.info("→ UserDetails username         : {}", userDetails.getUsername());
        log.info("→ Token expired?                : {}", isExpired);

        boolean isValid = userName.equals(userDetails.getUsername()) && !isExpired;
        log.info("→ Final token validity result   : {}", isValid);

        return isValid;
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }


}
