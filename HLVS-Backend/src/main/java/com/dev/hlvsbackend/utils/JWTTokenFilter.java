package com.dev.hlvsbackend.utils;

import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.Console;
import java.io.IOException;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {

    @Lazy
    @Autowired
    JWTTools jwtTools;

    @Lazy
    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        String userId = null;
        String token = null;

        if(tokenHeader != null && tokenHeader.startsWith("Bearer ") && tokenHeader.length() > 7) {
            token = tokenHeader.substring(7);
            try {
                userId = jwtTools.getUserIdFromToken(token);
                System.out.println(userId);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT TOKEN has expired");
            } catch (MalformedJwtException e) {
                System.out.println("JWT Malformado");
            }
        } else {
            System.out.println("Bearer string not found");
        }

        if(userId != null && token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.getUserById(userId);
            System.out.println(user.getUserType().toString());
            if(user != null) {
                Boolean tokenValidity = userService.isTokenValid(user, token);
                if(tokenValidity) {
                    if (
                            user.getUserType().toString().equals("USER") ||
                            user.getUserType().toString().equals("ADMIN") ||
                            user.getUserType().toString().equals("SUPERVISOR") ||
                            user.getUserType().toString().equals("GUEST") ||
                            user.getUserType().toString().equals("GUARD")
                    ){
                        UsernamePasswordAuthenticationToken authToken
                                = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(authToken);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}