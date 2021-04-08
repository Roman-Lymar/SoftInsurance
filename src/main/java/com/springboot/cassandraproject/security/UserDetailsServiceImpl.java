package com.springboot.cassandraproject.security;

import com.springboot.cassandraproject.dto.roleuserdto.EnumRole;
import com.springboot.cassandraproject.dto.roleuserdto.Role;
import com.springboot.cassandraproject.dto.roleuserdto.UsersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        String id = jwtUtils.getUserIdFromJwtToken(token);
        String role = jwtUtils.getUserRoleFromJwtToken(token);

        Role userRole = new Role(EnumRole.valueOf(role));

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(userRole);

        UsersDto user = new UsersDto(UUID.fromString(id));
        user.setRoles(roleSet);

        return UserDetailsImpl.build(user);
    }
}
