package com.springboot.cassandraproject.security;

import com.springboot.cassandraproject.controllers.catalog.ProductController;
import com.springboot.cassandraproject.exceptions.AuthorizationHeaderNotExistsException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(ProductController.class.getSimpleName());

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
        logger.info("Inside AuthTokenFilter.doFilterInternal");

        try {
            String jwtToken = parseJwt(request);
            logger.info(jwtToken.toString());

            if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
                String id = jwtUtils.getUserIdFromJwtToken(jwtToken);
                String role = jwtUtils.getUserRoleFromJwtToken(jwtToken);

                System.out.println(id);
                System.out.println(role);
                //logger.info(id + role);

                UserDetails userDetails = userDetailsService.loadUserByUsername(jwtToken);
                System.out.println("UserDetails" + userDetails.toString());
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
        String header = request.getHeader("Authorization");
        logger.info("Inside parseJWT()");
        if (StringUtils.isBlank(header)) {
            throw new AuthorizationHeaderNotExistsException();
        }

        if (Pattern.matches("^Bearer .*", header)) {
            header = header.replaceAll("^Bearer( )*", "");
            return header;
        }

        return null;
    }
}
