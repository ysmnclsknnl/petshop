package com.example.petshop.security;

import com.example.petshop.collection.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        User user = new User();
        user.setEmail(jwt.getSubject());
        String role = jwt.getClaim("role");
        user.setRole(role);
        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
        return new UsernamePasswordAuthenticationToken(user, jwt, authorities);
    }
}