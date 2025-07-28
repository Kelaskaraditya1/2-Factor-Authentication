package com.starkIndustries.RoleBasedAuthorization.api.dao;

import com.starkIndustries.RoleBasedAuthorization.api.modles.User;

public interface UserDao {

    public User findByUsername(String username);
}
