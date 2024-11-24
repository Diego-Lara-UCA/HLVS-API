package com.dev.hlvsbackend.config;

import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.services.UserService;
import com.dev.hlvsbackend.utils.JWTTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTTokenFilter filter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(auth -> auth
                //INDEX
                .requestMatchers("/").permitAll()

                //AUTH ENDPOINT
                .requestMatchers("/api/auth/**").permitAll()

                //USER ENDPOINTS
                .requestMatchers("/api/users/register").permitAll()
                .requestMatchers("/api/users/all").hasAnyAuthority("ADMIN", "SUPERVISOR")
                .requestMatchers("/api/users/get-user").hasAnyAuthority("ADMIN", "SUPERVISOR")
                .requestMatchers("/api/users/register-guard").hasAnyAuthority("ADMIN", "SUPERVISOR")
                .requestMatchers("/api/users/all-guards").hasAnyAuthority("ADMIN", "SUPERVISOR")

                //ENTRANCE KEY ENDPOINTS
                .requestMatchers("/api/entrance/key/**").hasAnyAuthority("USER", "GUEST", "ADMIN")

                //GRACE TIME ENDPOINTS
                .requestMatchers("/api/grace-time/**").hasAnyAuthority("ADMIN", "SUPERVISOR")

                //HOUSE ENDPOINTS
                .requestMatchers("/api/residential/house/**").hasAnyAuthority("USER", "SUPERVISOR", "ADMIN")

                //PERMISSIONS ENDPOINTS
                .requestMatchers("/api/residential/permission/**").hasAnyAuthority("GUEST", "SUPERVISOR", "ADMIN")

                //ENTRANCES ENDPOINTS
                .requestMatchers("/api/residential/entrance/**").hasAnyAuthority("GUARD", "ADMIN")

                .anyRequest().authenticated());

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(handling -> handling.authenticationEntryPoint((req, res, ex) -> {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Auth fail!");
        }));
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return  new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        //allowedOrigins("https://securityhlvs.com")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");

            }
        };
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder managerBuilder
                = http.getSharedObject(AuthenticationManagerBuilder.class);

        managerBuilder
                .userDetailsService(identifier -> {
                    System.out.println("identifier: " + identifier);
                    User user = userService.getUserById(identifier);

                    if(user == null)
                        throw new UsernameNotFoundException("User: " + identifier + ", not found!");

                    return user;
                })
                .passwordEncoder(passwordEncoder());

        return managerBuilder.build();
    }
}
