package com.pmart5a.cloudstorage.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pmart5a.cloudstorage.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
public class TokenUtil {

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Value("${jwt.token.lifetime-in-minutes}")
    private long lifetime;

    public String generateToken(User user) {
        final String subject = String.valueOf(user.getId());
        Instant now = Instant.now();
        Instant exp = now.plus(lifetime, ChronoUnit.MINUTES);
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(getAlgorithm());
    }

    public boolean checkToken(String token) {
        try {
            JWT.require(getAlgorithm()).build().verify(token);
        } catch (JWTVerificationException ex) {
            log.info("The access token is invalid.");
            return false;
        }
        return true;
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secretKey);
    }
}
