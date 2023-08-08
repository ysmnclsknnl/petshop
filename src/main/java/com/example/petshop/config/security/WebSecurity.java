package com.example.petshop.config.security;

import com.example.petshop.config.security.token.JwtToUserConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurity {
    final
    JwtToUserConverter jwtToUserConverter;

    public WebSecurity(JwtToUserConverter jwtToUserConverter) {
        this.jwtToUserConverter = jwtToUserConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/pets/add").hasRole("ADMIN")
                        .requestMatchers("/pets/{id}").hasRole("CUSTOMER")
                        .requestMatchers("/pets").hasAnyRole("CUSTOMER","ADMIN")
                        .anyRequest().authenticated())
                .logout(logout -> logout.permitAll())

                .formLogin(login ->
                        login.loginPage("/login"))
                .oauth2ResourceServer((oauth2) ->
                        oauth2.jwt((jwt) -> jwt.jwtAuthenticationConverter(jwtToUserConverter))
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                );
        return http.build();
    }
}
