package com.starkIndustries.RoleBasedAuthorization.auth.service;

import com.starkIndustries.RoleBasedAuthorization.auth.modles.Employee;
import com.starkIndustries.RoleBasedAuthorization.auth.modles.UserPrinciple;
import com.starkIndustries.RoleBasedAuthorization.auth.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    public EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Employee employee = this.employeeRepository.findByUsername(username);

        if(employee!=null)
            return new UserPrinciple(employee);

        return null;
    }
}
