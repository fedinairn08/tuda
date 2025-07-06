package com.tuda.config;

import com.tuda.exception.BadRequestException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {

    @Value("${jwt.jwtAccessSecret}")
    private String jwtAccessSecret;

    @Value("${jwt.jwtRefreshSecret}")
    private String jwtRefreshSecret;

    @Value("${jwt.accessLifetime}")
    private Duration accessLifetime;

    @Value("${jwt.refreshLifetime}")
    private Duration refreshLifetime;

    public String generateAccessToken(UserDetails userDetails, Long userId) {
        return generateToken(userDetails, userId, getAccessSigningKey(), accessLifetime);
    }

    public String generateRefreshToken(UserDetails userDetails, Long userId) {
        return generateToken(userDetails, userId, getRefreshSigningKey(), refreshLifetime);
    }

    private String generateToken(UserDetails userDetails, Long userId, SecretKey key, Duration lifetime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + lifetime.toMillis());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(key)
                .compact();
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, getRefreshSigningKey());
    }

    private boolean validateToken(String token, SecretKey key) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new BadRequestException("Токен просрочен");
        } catch (UnsupportedJwtException e) {
            throw new BadRequestException("Неподдерживаемый токен");
        } catch (MalformedJwtException e) {
            throw new BadRequestException("Некорректный формат токена");
        } catch (SignatureException e) {
            throw new BadRequestException("Неверная подпись");
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Пустой или null токен");
        }
    }

    public String getUsername(String token) {
        return parseAccessToken(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        return (List<String>) parseAccessToken(token).get("roles");
    }

    public Claims parseAccessToken(String token) {
        return parseToken(token, getAccessSigningKey());
    }

    public Claims parseRefreshToken(String token) {
        return parseToken(token, getRefreshSigningKey());
    }

    private Claims parseToken(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getAccessSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtAccessSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getRefreshSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtRefreshSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}