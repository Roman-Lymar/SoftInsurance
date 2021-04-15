package com.springboot.cassandraproject.security;

import com.springboot.cassandraproject.exceptions.AuthorizationHeaderNotExistsException;
import com.springboot.cassandraproject.exceptions.InvalidTokenException;
import com.springboot.cassandraproject.exceptions.TokenExpiredException;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.regex.Pattern;

@Component
public class JwtUtils {

    private static final Logger logger = LogManager.getLogger(JwtUtils.class.getSimpleName());

    private static String PUBLIC_KEY_PATH = "src/main/resources/public_x509.der";

    public boolean validateJwtToken(String jwtToken) {

        try {
            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(jwtToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("Invalid JWT signature.");
            logger.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token.");
            logger.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.info("Unsupported JWT token.");
            logger.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            logger.info("JWT token compact of handler are invalid.");
            logger.trace("JWT token compact of handler are invalid trace: {}", e);
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
                .setSigningKey(getPublicKey())
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
                .setSigningKey(getPublicKey())
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

    private PublicKey getPublicKey() {
        File publicKeyFile = new File(PUBLIC_KEY_PATH);
        byte[] publicKeyBytes = new byte[0];

        try {
            publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(publicKeySpec);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
