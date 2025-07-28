package com.starkIndustries.RoleBasedAuthorization.api.service;

import com.starkIndustries.RoleBasedAuthorization.api.modles.User;
import com.starkIndustries.RoleBasedAuthorization.api.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
@Component
public class UserPrincipleService implements UserDetailsService {

    @Autowired
    public UserService userService;

    @Autowired
    public RoleService roleService;

    public Collection<? extends GrantedAuthority> mapRoleToAuthority(Collection<Role> roles){
        return roles.stream().map(role->
                new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.userService.findByUsername(username);

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                mapRoleToAuthority(user.getRoles()));
    }
}
