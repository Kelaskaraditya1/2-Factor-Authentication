package com.starkIndustries.RoleBasedAuthorization.auth.modles;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrinciple implements UserDetails {

    private Employee employee;

    public UserPrinciple(Employee employee){
        this.employee=employee;
    }

    public UserPrinciple(){

    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return employee.getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
