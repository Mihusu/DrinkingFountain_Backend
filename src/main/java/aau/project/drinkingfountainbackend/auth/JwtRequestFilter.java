package aau.project.drinkingfountainbackend.auth;

import aau.project.drinkingfountainbackend.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final String ADMIN_ROLE = "ADMIN";

    @Autowired
    public JwtRequestFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String token = extractJwtFromRequest(request);

            if (token != null && token.startsWith("Bearer ") ) {
                // Validate the JWT token
                String tokenWithoutBearer = token.substring(7);
                Authentication authentication = jwtTokenService.getAuthentication(tokenWithoutBearer);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e){
            //Maybe log the failed jwt check
        }
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(
                this,
                UsernamePasswordAuthenticationFilter.class
        );

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/fountain/map").permitAll()
                .requestMatchers("/fountain/info/{id}").permitAll()
                .requestMatchers("/fountain/nearest/list").permitAll()
                .requestMatchers("/auth/reset-password").permitAll()
                .requestMatchers("/fountain/unapproved").hasRole(ADMIN_ROLE)
                .requestMatchers("/approve/{id}").hasRole(ADMIN_ROLE)
                .requestMatchers("/unapprove/{id}").hasRole(ADMIN_ROLE)
                .anyRequest().authenticated());

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
