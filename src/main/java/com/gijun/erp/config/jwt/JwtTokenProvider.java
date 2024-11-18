package com.gijun.erp.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private final JwtProperties jwtProperties;
    private SecretKey key;

    @PostConstruct
    public void init() {
        try {
            String secret = jwtProperties.getSecretKey();
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            this.key = Keys.hmacShaKeyFor(keyBytes);

            // 키 초기화 검증
            String testToken = createTestToken();
            validateToken(testToken);

            log.info("JWT key successfully initialized and validated");
        } catch (Exception e) {
            log.error("Failed to initialize JWT key", e);
            throw new IllegalStateException("Failed to initialize JWT key", e);
        }
    }

    private String createTestToken() {
        return Jwts.builder()
                .subject("test")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000))
                .signWith(key)
                .compact();
    }

    public String createToken(Authentication authentication, boolean isRefreshToken) {
        try {
            String authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            long now = System.currentTimeMillis();
            Date validity = new Date(now + (isRefreshToken ?
                    jwtProperties.getRefreshTokenValidityMs() :
                    jwtProperties.getExpirationMs()));

            return Jwts.builder()
                    .subject(authentication.getName())
                    .claim(AUTHORITIES_KEY, authorities)
                    .issuedAt(new Date(now))
                    .expiration(validity)
                    .signWith(key)  // 새로운 방식
                    .compact();

        } catch (Exception e) {
            log.error("Token creation failed", e);
            throw new RuntimeException("Token creation failed", e);
        }
    }

    public Authentication getAuthentication(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)  // 새로운 방식
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            User principal = new User(claims.getSubject(), "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, token, authorities);
        } catch (Exception e) {
            log.error("Authentication extraction failed", e);
            throw new RuntimeException("Authentication extraction failed", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)  // 새로운 방식
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());
        }
        return false;
    }

    // 디버그용 메서드
    public void logTokenInfo(String token) {
        try {
            String[] chunks = token.split("\\.");

            if (chunks.length == 3) {
                String header = new String(Base64.getDecoder().decode(chunks[0]));
                String payload = new String(Base64.getDecoder().decode(chunks[1]));

                log.debug("Token Header: {}", header);
                log.debug("Token Payload: {}", payload);
            }

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            log.debug("Token Subject: {}", claims.getSubject());
            log.debug("Token Issued At: {}", claims.getIssuedAt());
            log.debug("Token Expiration: {}", claims.getExpiration());
            log.debug("Token Authorities: {}", claims.get(AUTHORITIES_KEY));
        } catch (Exception e) {
            log.error("Failed to log token info", e);
        }
    }
}