package com.starkIndustries.RoleBasedAuthorization.auth.repository;

import com.starkIndustries.RoleBasedAuthorization.auth.modles.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser,String> {

    public AuthUser findByUsername(String username);

}
