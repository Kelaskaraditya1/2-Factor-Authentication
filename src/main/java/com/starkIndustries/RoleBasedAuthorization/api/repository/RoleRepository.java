package com.starkIndustries.RoleBasedAuthorization.api.repository;

import com.starkIndustries.RoleBasedAuthorization.api.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {

    public Role findByName(String name);
}
