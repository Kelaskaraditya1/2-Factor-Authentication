package com.starkIndustries.RoleBasedAuthorization.api.dao;

import com.starkIndustries.RoleBasedAuthorization.api.role.Role;

public interface RoleDao {

    public Role findByName(String name);
}
