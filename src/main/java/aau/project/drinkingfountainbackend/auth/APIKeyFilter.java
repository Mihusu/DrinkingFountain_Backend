package aau.project.drinkingfountainbackend.auth;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class APIKeyFilter extends OncePerRequestFilter {

    @Value("${auth.api-key.value}")
    private String apiKeyValue;

    @Value("${auth.api-key.key}")
    private String apiKeyHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader(this.apiKeyHeader);

        if (apiKeyHeader != null && apiKey.equals(apiKeyValue)) {
            // API key is valid
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    @Bean
    public FilterRegistrationBean<APIKeyFilter> apiKeyFilterFilterRegistration(){
        FilterRegistrationBean<APIKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(this);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}