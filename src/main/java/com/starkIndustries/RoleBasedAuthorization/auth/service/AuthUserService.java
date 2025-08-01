package com.starkIndustries.RoleBasedAuthorization.auth.service;

import com.starkIndustries.RoleBasedAuthorization.auth.dto.request.LoginRequestDto;
import com.starkIndustries.RoleBasedAuthorization.auth.dto.request.VerificationRequest;
import com.starkIndustries.RoleBasedAuthorization.auth.dto.response.LoginResponse;
import com.starkIndustries.RoleBasedAuthorization.auth.dto.response.SignupResponse;
import com.starkIndustries.RoleBasedAuthorization.auth.modles.AuthUser;
import com.starkIndustries.RoleBasedAuthorization.auth.repository.AuthUserRepository;
import com.starkIndustries.RoleBasedAuthorization.auth.security.twoFactorAuthentication.TwoFactorAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AuthUserService {

    @Autowired
    public AuthUserRepository authUserRepository;

    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    public JwtService jwtService;

    @Autowired
    public TwoFactorAuthenticationService twoFactorAuthenticationService;

    public List<AuthUser> getEmployees(){
        return this.authUserRepository.findAll();
    }

    public AuthUser getEmployeeById(String empId){
        if(this.authUserRepository.existsById(empId)){
            AuthUser authUser = this.authUserRepository.findById(empId).get();
            log.error("Employee:{}", authUser);
            return authUser;
        }
        return null;
    }

    public SignupResponse addEmployee(AuthUser authUser){

        String empId = UUID.randomUUID().toString();
        if(!this.authUserRepository.existsById(empId)){

            SignupResponse signupResponse = new SignupResponse();
            authUser.setEmpId(empId);
            authUser.setPassword(this.bCryptPasswordEncoder.encode(authUser.getPassword()));
            if(authUser.isMfaEnabled()){
                authUser.setSecret(this.twoFactorAuthenticationService.generateNewSecret());
                signupResponse.setQrCodeImageUri(this.twoFactorAuthenticationService.generateQrCodeImageUri(authUser.getSecret()));
            }
            signupResponse.setAuthUser(authUser);
            signupResponse.setJwtToken(this.jwtService.getJwtToken(authUser));
            signupResponse.setRefreshToken(this.jwtService.getRefreshToken(authUser));
            signupResponse.setMfaEnabled(authUser.isMfaEnabled());
            this.authUserRepository.save(authUser);
            return signupResponse;
        }
        return null;
    }

    public boolean deleteEmployee(String empID){
        if(this.authUserRepository.existsById(empID)){
            this.authUserRepository.deleteById(empID);
            return true;
        }
        return false;
    }

    public AuthUser updateEmployee(String empId, AuthUser authUser){

        if(this.authUserRepository.existsById(empId)){
            AuthUser authUser1 = this.authUserRepository.findById(empId).get();
            authUser1.setEmail(authUser.getEmail());
            authUser1.setName(authUser.getName());
            authUser1.setUsername(authUser.getUsername());
            return this.authUserRepository.save(authUser1);
        }
        return null;
    }

    public LoginResponse login(LoginRequestDto loginRequestDto){

        AuthUser authUser = this.authUserRepository.findByUsername(loginRequestDto.getUsername());

        if(authUser !=null){

            Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getUsername(),
                    loginRequestDto.getPassword()));

            if(authentication.isAuthenticated()){
                LoginResponse loginResponse = new LoginResponse();
                if(authUser.isMfaEnabled()){
                    loginResponse.setAuthUser(authUser);
                    loginResponse.setJwtToken(this.jwtService.getJwtToken(authUser));
                    loginResponse.setRefreshToken(this.jwtService.getRefreshToken(authUser));
                    loginResponse.setMfaEnabled(true);
                    return loginResponse;
                }
                loginResponse.setAuthUser(authUser);
                loginResponse.setJwtToken(this.jwtService.getJwtToken(authUser));
                loginResponse.setRefreshToken(this.jwtService.getRefreshToken(authUser));
                loginResponse.setMfaEnabled(false);
                return loginResponse;
            }else
                return null;
        }
        return null;
    }

    public LoginResponse verifyTwoFactor(VerificationRequest verificationRequest){

        AuthUser user = this.authUserRepository.findByUsername(verificationRequest.getUsername());
        log.error("user:{}",user);
        log.error("otp:{}",verificationRequest.getCode());

        if(!this.twoFactorAuthenticationService.isOtpValid(user.getSecret(),verificationRequest.getCode())){
            log.error("login response is null");
            return null;
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAuthUser(user);
        loginResponse.setJwtToken(this.jwtService.getJwtToken(user));
        loginResponse.setRefreshToken(this.jwtService.getRefreshToken(user));
        loginResponse.setMfaEnabled(user.isMfaEnabled());
        return loginResponse;
    }
}
