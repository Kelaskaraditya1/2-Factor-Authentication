package com.starkIndustries.RoleBasedAuthorization.api.repository;

import com.starkIndustries.RoleBasedAuthorization.api.modles.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    public User findByUsernameAndEnabledTrue(String username);
}
