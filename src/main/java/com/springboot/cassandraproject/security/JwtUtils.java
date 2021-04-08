package com.springboot.cassandraproject.security;

import com.springboot.cassandraproject.exceptions.InvalidTokenException;
import com.springboot.cassandraproject.exceptions.TokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Autowired
    private JwtConfig jwtConfig;

    public boolean validateJwtToken(String jwtToken) {
        try {
            //        Key hmacKey  = new SecretKeySpec(Base64.getDecoder().decode(jwtConfig.getSecret()),
//                    SignatureAlgorithm.HS256.getJcaName());
            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.getSecret())
                    .build()
                    .parseClaimsJws(jwtToken);
            return true;
        } catch (SignatureException e) {
            //logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            //logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            //logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            //logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            //logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String getUserIdFromJwtToken(String jwtToken) {

//        Key hmacKey  = new SecretKeySpec(Base64.getDecoder().decode(jwtConfig.getSecret()),
//                        SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSecret())
                .build()
                .parseClaimsJws(jwtToken);

        Claims claims;
        if (jwsClaims.getBody() != null) {
            claims = jwsClaims.getBody();
            if (claims.getExpiration() == null) {
                throw new InvalidTokenException();
            }
            long exp = claims.getExpiration().getTime();
            if (exp < new Date().getTime()) {
                throw new TokenExpiredException();
            }
        } else {
            throw new InvalidTokenException();
        }

        return claims.getSubject();
    }

    public String getUserRoleFromJwtToken(String jwtToken) {

//        Key hmacKey  = new SecretKeySpec(Base64.getDecoder().decode(jwtConfig.getSecret()),
//                        SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSecret())
                .build()
                .parseClaimsJws(jwtToken);

        Claims claims;
        if (jwsClaims.getBody() != null) {
            claims = jwsClaims.getBody();
            if (claims.getExpiration() == null) {
                throw new InvalidTokenException();
            }
            long exp = claims.getExpiration().getTime();
            if (exp < new Date().getTime()) {
                throw new TokenExpiredException();
            }
        } else {
            throw new InvalidTokenException();
        }

        return (String) claims.get("role");
    }
}
