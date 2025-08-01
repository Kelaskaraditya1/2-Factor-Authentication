package com.starkIndustries.RoleBasedAuthorization.auth.service;

import com.starkIndustries.RoleBasedAuthorization.auth.modles.AuthUser;
import com.starkIndustries.RoleBasedAuthorization.auth.modles.UserPrinciple;
import com.starkIndustries.RoleBasedAuthorization.auth.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    public AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AuthUser authUser = this.authUserRepository.findByUsername(username);

        if(authUser !=null)
            return new UserPrinciple(authUser);

        return null;
    }
}
