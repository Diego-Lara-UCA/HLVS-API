package com.dev.hlvsbackend.utils;

import com.dev.hlvsbackend.domain.entities.User;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTools {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.exptime}")
    private Integer exp;
    //  From a user, it will generate a JWT token containing the username, signed with a Secret key.
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        ArrayList<String> roles = new ArrayList<>();

        claims.put("id", user.getId().toString());
        claims.put("name", user.getNombre());
        claims.put("email", user.getCorreo());
        claims.put("rol", user.getUserType());
        claims.put("document_type", user.getTipo_documento());
        claims.put("document_number", user.getNumero_documento());
        if (user.getCasa() != null)
            claims.put("house_number", user.getCasa().getNumber());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + exp))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public Boolean verifyToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build();

            parser.parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public String getUserIdFromToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build();

            return parser.parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}