package com.springboot.cassandraproject.security;

import com.springboot.cassandraproject.exceptions.AuthorizationHeaderNotExistsException;
import com.springboot.cassandraproject.exceptions.InvalidTokenException;
import com.springboot.cassandraproject.exceptions.TokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Pattern;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    public boolean validateJwtToken(String jwtToken) {

        try {
            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(getEncodedSecretKey())
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

    public String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.isBlank(header)) {
            throw new AuthorizationHeaderNotExistsException();
        }

        if (Pattern.matches("^Bearer .*", header)) {
            header = header.replaceAll("^Bearer( )*", "");
            return header;
        }

        return null;
    }

    public String getUserIdFromJwtToken(String jwtToken) {

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(getEncodedSecretKey())
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

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(getEncodedSecretKey())
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

    private String getSecretKey() {
        return secret;
    }

    private SecretKey getEncodedSecretKey() {
        return Keys.hmacShaKeyFor(getSecretKey().getBytes(StandardCharsets.UTF_8));
    }
}
