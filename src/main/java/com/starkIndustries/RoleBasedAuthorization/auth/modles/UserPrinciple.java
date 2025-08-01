package com.starkIndustries.RoleBasedAuthorization.auth.modles;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrinciple implements UserDetails {

    private AuthUser authUser;

    public UserPrinciple(AuthUser authUser){
        this.authUser = authUser;
    }

    public UserPrinciple(){

    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authUser.getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }

    @Override
    public String getUsername() {
        return authUser.getUsername();
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
