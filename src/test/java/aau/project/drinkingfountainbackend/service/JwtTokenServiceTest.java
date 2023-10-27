package aau.project.drinkingfountainbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class JwtTokenServiceTest {

    @InjectMocks
    private JwtTokenService jwtTokenService;

    @Test
    void getUserIdFromToken() {
        ReflectionTestUtils.setField(jwtTokenService, "JWT_TOKEN_VALIDITY", 100000);
        ReflectionTestUtils.setField(jwtTokenService, "secret", "abcdef");

        int id = 1;
        String token = jwtTokenService.generateToken(id, "ADMIN");

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        // Call the method and assert the result
        int userId = jwtTokenService.getUserIdFromToken(mockRequest);
        Assertions.assertEquals(id, userId);
    }

    @Test
    void getAuthenticationTest() {
        ReflectionTestUtils.setField(jwtTokenService, "JWT_TOKEN_VALIDITY", 100000);
        ReflectionTestUtils.setField(jwtTokenService, "secret", "abcdef");

        int id = 1;
        String role = "ADMIN";
        String token = jwtTokenService.generateToken(id, role);

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        // Call the method and assert the result
        int userId = jwtTokenService.getUserIdFromToken(mockRequest);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        Authentication authentication = jwtTokenService.getAuthentication(token);

        Assertions.assertEquals(id, userId);
        Assertions.assertEquals(1, authorities.size());
        Assertions.assertTrue(authentication.isAuthenticated());
        Assertions.assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());

    }
}
