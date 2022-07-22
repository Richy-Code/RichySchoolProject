package com.example.demo.webconfig_security;

import com.example.demo.entities.Users;
import com.example.demo.services.service_interface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailServiceImp implements UserDetailsService {

    @Autowired
    UserServiceInterface userServiceInterface;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userServiceInterface.getUser(username);
        if(users == null){
            throw new UsernameNotFoundException("User does not exist");
        }
        return new MyUserDetails(users);
    }
}