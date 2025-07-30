package com.starkIndustries.RoleBasedAuthorization.auth.controller;

import com.starkIndustries.RoleBasedAuthorization.auth.dto.request.LoginRequestDto;
import com.starkIndustries.RoleBasedAuthorization.auth.dto.response.LoginResponse;
import com.starkIndustries.RoleBasedAuthorization.auth.dto.response.SignupResponse;
import com.starkIndustries.RoleBasedAuthorization.auth.modles.Employee;
import com.starkIndustries.RoleBasedAuthorization.auth.service.EmployeeService;
import com.starkIndustries.RoleBasedAuthorization.auth.service.JwtService;
import com.starkIndustries.RoleBasedAuthorization.keys.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    public EmployeeService employeeService;

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

        List<Employee> employeeList = this.employeeService.getEmployees();
        if(!employeeList.isEmpty()){
            response.put(Keys.TIME_STAMP, Instant.now());
            response.put(Keys.STATUS, HttpStatus.OK.value());
            response.put(Keys.MESSAGE,"Employees found successfully!!");
            response.put(Keys.BODY,employeeList);
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

        Employee employee = this.employeeService.getEmployeeById(empId);

        Map<String,Object> response = new HashMap<>();

        if(employee!=null){
            response.put(Keys.TIME_STAMP,Instant.now());
            response.put(Keys.STATUS,HttpStatus.OK.value());
            response.put(Keys.MESSAGE,"Employee found successfully!!");
            response.put(Keys.BODY,employee);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else {
            response.put(Keys.TIME_STAMP, Instant.now());
            response.put(Keys.STATUS, HttpStatus.NOT_FOUND.value());
            response.put(Keys.MESSAGE,"Employee not found!!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found!!");
        }
    }

    @PostMapping("/add-employee")
    public ResponseEntity<?> addEmployee(@RequestBody Employee employee){

        SignupResponse signupResponse = this.employeeService.addEmployee(employee);

        Map<String,Object> response = new HashMap<>();

        if(signupResponse!=null){
            response.put(Keys.TIME_STAMP,Instant.now());
            response.put(Keys.STATUS,HttpStatus.OK.value());
            response.put(Keys.MESSAGE,"Employees added successfully!!");
            response.put(Keys.BODY,signupResponse);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else {
            response.put(Keys.TIME_STAMP, Instant.now());
            response.put(Keys.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put(Keys.MESSAGE,"Failed to add Employee!!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add Employee!!");
        }
    }

    @DeleteMapping("/delete-employee/{empId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("empId") String empId){

        Map<String,Object> response = new HashMap<>();

        if(this.employeeService.deleteEmployee(empId)){
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
    public ResponseEntity<?> updateEmployee(@PathVariable("empId") String empId,@RequestBody Employee employee){

        Employee employee1 = this.employeeService.updateEmployee(empId,employee);
        Map<String,Object> response = new HashMap<>();

        if(employee1!=null){
            response.put(Keys.TIME_STAMP,Instant.now());
            response.put(Keys.STATUS,HttpStatus.OK.value());
            response.put(Keys.BODY,employee1);
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

        LoginResponse loginResponse = this.employeeService.login(loginRequestDto);
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
}