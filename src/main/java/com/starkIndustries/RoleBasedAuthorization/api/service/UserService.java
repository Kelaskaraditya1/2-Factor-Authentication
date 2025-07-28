package com.starkIndustries.RoleBasedAuthorization.api.service;

import com.starkIndustries.RoleBasedAuthorization.api.dao.UserDao;
import com.starkIndustries.RoleBasedAuthorization.api.modles.User;
import com.starkIndustries.RoleBasedAuthorization.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService implements UserDao {

    @Autowired
    public UserRepository userRepository;


    @Override
    public User findByUsername(String username) {

        User user = this.userRepository.findByUsernameAndEnabledTrue(username);
        if(user!=null){
            log.error("user: {}",user);
            return user;
        }
        return null;
    }
}
