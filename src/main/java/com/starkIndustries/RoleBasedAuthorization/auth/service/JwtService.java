package com.starkIndustries.RoleBasedAuthorization.auth.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starkIndustries.RoleBasedAuthorization.auth.dto.response.LoginResponse;
import com.starkIndustries.RoleBasedAuthorization.auth.modles.Employee;
import com.starkIndustries.RoleBasedAuthorization.auth.modles.UserPrinciple;
import com.starkIndustries.RoleBasedAuthorization.auth.repository.EmployeeRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Request;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static org.springframework.cache.interceptor.SimpleKeyGenerator.generateKey;

@Slf4j
@Service
public class JwtService {

    @Autowired
    public EmployeeRepository employeeRepository;

    @Autowired
    public MyUserDetailService myUserDetailService;

    private String secretKey = "";

    @Value("${application.security.jwt.expiration}")
    public Long jwtTokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    public Long refreshTokenExpiration;

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
        return buildToken(employee, (long) (1000*60));
    }

    public String getRefreshToken(Employee employee){
        return buildToken(employee,refreshTokenExpiration);
    }


    public String buildToken(Employee employee,Long expiration){

        Map<String, Objects> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(employee.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
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

    public LoginResponse getRefreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader==null || !authHeader.startsWith("Bearer "))
            return null;

        String refreshToken = authHeader.substring(7);

        log.error("refreshToken:{}",refreshToken);

        String username = extractUserName(refreshToken);

        log.error("username:{}",username);

        LoginResponse loginResponse = new LoginResponse();

        if(username!=null){

            Employee employee = this.employeeRepository.findByUsername(username);
            UserDetails userDetails = this.myUserDetailService.loadUserByUsername(username);
            if(isTokenValid(refreshToken,userDetails)){
                String accessToken = getJwtToken(employee);
                loginResponse.setEmployee(employee);
                loginResponse.setJwtToken(accessToken);
                loginResponse.setRefreshToken(refreshToken);

                log.error("Login Response: {}",loginResponse);

                ObjectMapper objectMapper = new ObjectMapper();
                httpServletResponse.setContentType("application/json");
                objectMapper.writeValue(httpServletResponse.getOutputStream(),loginResponse);

            }
        }

        return loginResponse;
    }


}
