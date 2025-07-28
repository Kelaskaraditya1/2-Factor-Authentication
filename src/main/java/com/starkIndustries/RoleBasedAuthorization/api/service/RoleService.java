package com.starkIndustries.RoleBasedAuthorization.api.service;

import com.starkIndustries.RoleBasedAuthorization.api.dao.RoleDao;
import com.starkIndustries.RoleBasedAuthorization.api.repository.RoleRepository;
import com.starkIndustries.RoleBasedAuthorization.api.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoleService implements RoleDao {

    @Autowired
    public RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {

        Role role = this.roleRepository.findByName(name);
        if(role!=null){
            log.error("role: {}",role);
            return role;
        }
        return null;
    }
}
