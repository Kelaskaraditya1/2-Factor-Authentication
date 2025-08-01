package com.starkIndustries.RoleBasedAuthorization.auth.controller;

import com.google.zxing.WriterException;
import com.starkIndustries.RoleBasedAuthorization.auth.dto.request.LoginRequestDto;
import com.starkIndustries.RoleBasedAuthorization.auth.dto.request.VerificationRequest;
import com.starkIndustries.RoleBasedAuthorization.auth.dto.response.LoginResponse;
import com.starkIndustries.RoleBasedAuthorization.auth.dto.response.SignupResponse;
import com.starkIndustries.RoleBasedAuthorization.auth.modles.AuthUser;
import com.starkIndustries.RoleBasedAuthorization.auth.service.AuthUserService;
import com.starkIndustries.RoleBasedAuthorization.auth.service.JwtService;
import com.starkIndustries.RoleBasedAuthorization.keys.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/employee")
public class AuthUserController {

    @Autowired
    public AuthUserService authUserService;

    @Autowired
    public JwtService jwtService;

    @GetMapping("/greetings")
    public ResponseEntity<?> greetings(){

        HashMap<String,Object> response = new HashMap<>();

        response.put(Keys.TIME_STAMP, Instant.now());
        response.put(Keys.STATUS, HttpStatus.OK.value());
        response.put(Keys.MESSAGE,"Greetings,I am Optimus Prime!!");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/get-employees")
    public ResponseEntity<?> getAllEmployees(){

        Map<String, Object> response = new HashMap<>();

        List<AuthUser> authUserList = this.authUserService.getEmployees();
        if(!authUserList.isEmpty()){
            response.put(Keys.TIME_STAMP, Instant.now());
            response.put(Keys.STATUS, HttpStatus.OK.value());
            response.put(Keys.MESSAGE,"Employees found successfully!!");
            response.put(Keys.BODY, authUserList);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else {
            response.put(Keys.TIME_STAMP, Instant.now());
            response.put(Keys.STATUS, HttpStatus.NOT_FOUND.value());
            response.put(Keys.MESSAGE,"Enter Employees first!!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Enter Employees first!!");
        }
    }

    @GetMapping("/get-employee/{empId}")
    public ResponseEntity<?> getEmployeeById(@PathVariable("empId")  String empId){

        AuthUser authUser = this.authUserService.getEmployeeById(empId);

        Map<String,Object> response = new HashMap<>();

        if(authUser !=null){
            response.put(Keys.TIME_STAMP,Instant.now());
            response.put(Keys.STATUS,HttpStatus.OK.value());
            response.put(Keys.MESSAGE,"Employee found successfully!!");
            response.put(Keys.BODY, authUser);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else {
            response.put(Keys.TIME_STAMP, Instant.now());
            response.put(Keys.STATUS, HttpStatus.NOT_FOUND.value());
            response.put(Keys.MESSAGE,"Employee not found!!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found!!");
        }
    }

    @PostMapping("/add-employee")
    public ResponseEntity<?> addEmployee(@RequestBody AuthUser authUser,HttpServletRequest httpServletRequest){
        SignupResponse signupResponse = this.authUserService.addEmployee(authUser);

        Map<String,Object> response = new HashMap<>();

        if(signupResponse != null){

            if(authUser.isMfaEnabled()){
                String username = signupResponse.getAuthUser().getUsername();
                String secret = signupResponse.getAuthUser().getSecret(); // not stored
                String uri = signupResponse.getQrCodeImageUri(); // not stored

                String baseUrl = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort();
                String qrPageUrl = baseUrl + "/signup-2fa?username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                        + "&secret=" + URLEncoder.encode(secret, StandardCharsets.UTF_8)
                        + "&uri=" + URLEncoder.encode(uri, StandardCharsets.UTF_8);
                response.put(Keys.QR_CODE_URL, qrPageUrl);  // 👈 Add this
            }
            response.put(Keys.TIME_STAMP, Instant.now());
            response.put(Keys.STATUS, HttpStatus.OK.value());
            response.put(Keys.MESSAGE, "Employee added successfully!!");
            response.put(Keys.BODY, signupResponse);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put(Keys.TIME_STAMP, Instant.now());
            response.put(Keys.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put(Keys.MESSAGE, "Failed to add Employee!!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete-employee/{empId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("empId") String empId){

        Map<String,Object> response = new HashMap<>();

        if(this.authUserService.deleteEmployee(empId)){
            response.put(Keys.TIME_STAMP,Instant.now());
            response.put(Keys.STATUS,HttpStatus.OK.value());
            response.put(Keys.MESSAGE,"Employees deleted successfully!!");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            response.put(Keys.TIME_STAMP, Instant.now());
            response.put(Keys.STATUS, HttpStatus.NOT_FOUND.value());
            response.put(Keys.MESSAGE,"Failed to deleted Employee!!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to deleted Employee!!");
        }
    }

    @PutMapping("/update-employee/{empId}")
    public ResponseEntity<?> updateEmployee(@PathVariable("empId") String empId,@RequestBody AuthUser authUser){

        AuthUser authUser1 = this.authUserService.updateEmployee(empId, authUser);
        Map<String,Object> response = new HashMap<>();

        if(authUser1 !=null){
            response.put(Keys.TIME_STAMP,Instant.now());
            response.put(Keys.STATUS,HttpStatus.OK.value());
            response.put(Keys.BODY, authUser1);
            response.put(Keys.MESSAGE,"Employees updates successfully!!");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            response.put(Keys.TIME_STAMP, Instant.now());
            response.put(Keys.STATUS, HttpStatus.NOT_FOUND.value());
            response.put(Keys.MESSAGE,"Failed to update Employee!!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update Employee!!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto){

        Map<String,Object> response = new HashMap<>();

        LoginResponse loginResponse = this.authUserService.login(loginRequestDto);
        if(loginResponse!=null){
            response.put(Keys.TIME_STAMP,Instant.now());
            response.put(Keys.STATUS,HttpStatus.OK.value());
            response.put(Keys.MESSAGE,"Login Successful!!");
            response.put(Keys.BODY,loginResponse);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            response.put(Keys.TIME_STAMP, Instant.now());
            response.put(Keys.STATUS, HttpStatus.UNAUTHORIZED.value());
            response.put(Keys.MESSAGE,"Login Failed!!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Failed!!");
        }

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws IOException {
        LoginResponse loginResponse = this.jwtService.getRefreshToken(httpServletRequest,httpServletResponse);

        Map<String,Object> response = new HashMap<>();

        if(loginResponse!=null){
            response.put(Keys.TIME_STAMP,Instant.now());
            response.put(Keys.STATUS,HttpStatus.OK);
            response.put(Keys.MESSAGE,"Token refreshed successfully!!");
            response.put(Keys.BODY,loginResponse);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            response.put(Keys.TIME_STAMP,Instant.now());
            response.put(Keys.STATUS,HttpStatus.INTERNAL_SERVER_ERROR);
            response.put(Keys.MESSAGE,"Failed to refresh!!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest verificationRequest){

        log.error("otp:{}",verificationRequest.getCode());
        log.error("username:{}",verificationRequest.getUsername());

        Map<String,Object> response = new HashMap<>();
        response.put(Keys.TIME_STAMP,Instant.now());

        LoginResponse loginResponse = this.authUserService.verifyTwoFactor(verificationRequest);
        if(loginResponse!=null){
            response.put(Keys.STATUS,HttpStatus.OK);
            response.put(Keys.MESSAGE,"2 Factor Authentication Success!!");
            response.put(Keys.BODY,loginResponse);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            response.put(Keys.STATUS,HttpStatus.INTERNAL_SERVER_ERROR);
            response.put(Keys.MESSAGE,"Enter valid code!!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
}