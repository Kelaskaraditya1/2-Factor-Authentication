package com.starkIndustries.RoleBasedAuthorization.auth.service;

import com.starkIndustries.RoleBasedAuthorization.auth.dto.request.LoginRequestDto;
import com.starkIndustries.RoleBasedAuthorization.auth.dto.response.LoginResponse;
import com.starkIndustries.RoleBasedAuthorization.auth.dto.response.SignupResponse;
import com.starkIndustries.RoleBasedAuthorization.auth.modles.Employee;
import com.starkIndustries.RoleBasedAuthorization.auth.repository.EmployeeRepository;
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
public class EmployeeService {

    @Autowired
    public EmployeeRepository employeeRepository;

    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    public JwtService jwtService;

    public List<Employee> getEmployees(){
        return this.employeeRepository.findAll();
    }

    public Employee getEmployeeById(String empId){
        if(this.employeeRepository.existsById(empId)){
            Employee employee = this.employeeRepository.findById(empId).get();
            log.error("Employee:{}",employee);
            return employee;
        }
        return null;
    }

    public SignupResponse addEmployee(Employee employee){

        String empId = UUID.randomUUID().toString();
        if(!this.employeeRepository.existsById(empId)){

            SignupResponse signupResponse = new SignupResponse();
            employee.setEmpId(empId);
            employee.setPassword(this.bCryptPasswordEncoder.encode(employee.getPassword()));
            signupResponse.setEmployee(employee);
            signupResponse.setJwtToken(this.jwtService.getJwtToken(employee));
            signupResponse.setRefreshToken(this.jwtService.getRefreshToken(employee));
            this.employeeRepository.save(employee);
            return signupResponse;
        }
        return null;
    }

    public boolean deleteEmployee(String empID){
        if(this.employeeRepository.existsById(empID)){
            this.employeeRepository.deleteById(empID);
            return true;
        }
        return false;
    }

    public Employee updateEmployee(String empId,Employee employee){

        if(this.employeeRepository.existsById(empId)){
            Employee employee1= this.employeeRepository.findById(empId).get();
            employee1.setEmail(employee.getEmail());
            employee1.setName(employee.getName());
            employee1.setUsername(employee.getUsername());
            return this.employeeRepository.save(employee1);
        }
        return null;
    }

    public LoginResponse login(LoginRequestDto loginRequestDto){

        Employee employee = this.employeeRepository.findByUsername(loginRequestDto.getUsername());

        if(employee!=null){

            Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getUsername(),
                    loginRequestDto.getPassword()));

            if(authentication.isAuthenticated()){
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setEmployee(employee);
                loginResponse.setJwtToken(this.jwtService.getJwtToken(employee));
                loginResponse.setRefreshToken(this.jwtService.getRefreshToken(employee));
                return loginResponse;
            }else
                return null;
        }
        return null;
    }
}
