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

import static com.pmart5a.cloudstorage.generator.GeneratorId.getGeneratorId;

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
            final var errorId = getGeneratorId().getId();
            log.error("ErrorId: [{}]. TokenUtil. Token authentication. The access token is invalid.", errorId);
            return false;
        }
        return true;
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secretKey);
    }

//    public boolean checkToken(String token) {
//        Algorithm algorithm = Algorithm.HMAC256(secretKey);
//        JWTVerifier verifier = JWT.require(algorithm).build();
//
//        DecodedJWT jwt = JWT.require(algorithm)
//                .build()
//                .verify("a.b.c");
//
//        try {
//            DecodedJWT decodedJWT = verifier.verify(token);
//            if (!decodedJWT.getIssuer().equals("auth-service")) {
//                log.error("Issuer is incorrect");
//                return false;
//            }
//
//            if (!decodedJWT.getAudience().contains("bookstore")) {
//                log.error("Audience is incorrect");
//                return false;
//            }
//        } catch (JWTVerificationException e) {
//            log.error("Token is invalid: " + e.getMessage());
//            return false;
//        }
//
//        return true;
//    }

}
