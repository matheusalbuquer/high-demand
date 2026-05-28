package com.triade.planodeaula.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.triade.planodeaula.entites.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final Algorithm algorithm =
            Algorithm.HMAC256("my-secret");

    public String generateToken(User user){

        try{

            return JWT.create()
                    .withIssuer("triade")
                    .withSubject(user.getEmail())
                    .withClaim("userId", user.getId())
                    .withExpiresAt(generateDateExpiration())
                    .sign(algorithm);

        } catch (JWTCreationException e){

            throw new RuntimeException(
                    "Erro ao gerar token"
            );
        }
    }

    public String validateToken(String token){

        try{

            return JWT.require(algorithm)
                    .withIssuer("triade")
                    .acceptLeeway(30)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException e){

            return null;
        }
    }

    public Instant generateDateExpiration(){

        return LocalDateTime.now()
                .plusHours(8)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}