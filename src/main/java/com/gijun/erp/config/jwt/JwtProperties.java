package com.gijun.erp.config.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtProperties {
    private String secretKey;
    private long expirationMs;
    private long refreshTokenValidityMs;

    public long getAccessTokenValidityInSeconds() {
        return expirationMs / 1000;
    }

    public long getRefreshTokenValidityInSeconds() {
        return refreshTokenValidityMs / 1000;
    }
}