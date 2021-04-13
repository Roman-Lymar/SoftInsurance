package com.springboot.cassandraproject.security;

import com.springboot.cassandraproject.dto.UsersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String jwtToken) throws UsernameNotFoundException {

        String id = jwtUtils.getUserIdFromJwtToken(jwtToken);
        String role = jwtUtils.getUserRoleFromJwtToken(jwtToken);
        UsersDto user = new UsersDto(UUID.fromString(id), role);

        System.out.println("User info: " + user);

        return UserDetailsImpl.build(user);
    }
}
