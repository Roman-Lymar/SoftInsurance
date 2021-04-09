package com.springboot.cassandraproject.security;

import com.springboot.cassandraproject.dto.roleuserdto.UsersDto;
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
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        String id = jwtUtils.getUserIdFromJwtToken(token);
        String role = jwtUtils.getUserRoleFromJwtToken(token);

        UsersDto user = new UsersDto(UUID.fromString(id), role);
        System.out.println(user.toString());
        return UserDetailsImpl.build(user);
    }
}
