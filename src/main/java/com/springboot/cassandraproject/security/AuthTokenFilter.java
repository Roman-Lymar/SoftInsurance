package com.springboot.cassandraproject.security;

import com.springboot.cassandraproject.exceptions.AuthorizationHeaderNotExistsException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        try {
            String jwtToken = parseJwt(request);

            if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
                String id = jwtUtils.getUserIdFromJwtToken(jwtToken);
                String role = jwtUtils.getUserRoleFromJwtToken(jwtToken);

                UserDetails userDetails = userDetailsService.loadUserByUsername(jwtToken);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String header = request.getHeader(jwtConfig.getHeader());

        if (StringUtils.isBlank(header)) {
            throw new AuthorizationHeaderNotExistsException();
        }

        if (Pattern.matches(jwtConfig.getPrefix(), header)) {
            header = header.replaceAll(jwtConfig.getPrefix(), "");
            return header;
        }

        return null;
    }
}
