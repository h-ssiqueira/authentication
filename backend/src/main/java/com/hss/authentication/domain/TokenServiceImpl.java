package com.hss.authentication.domain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hss.authentication.commons.exception.AuthException;
import com.hss.authentication.persistence.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.hss.authentication.commons.utils.DateAndTimeUtils.generateExpirationInstant;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(String username) throws AuthException {
        try {
            var algo = Algorithm.HMAC512(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(username)
                    .withExpiresAt(generateExpirationInstant())
                    .sign(algo);
        } catch (Exception ex) {
            throw new AuthException("Error generating token","ERR");
        }
    }

    public String validateToken(String token) {
        try {
            var algo = Algorithm.HMAC512(secret);
            return JWT.require(algo)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            return "";
        }
    }
}