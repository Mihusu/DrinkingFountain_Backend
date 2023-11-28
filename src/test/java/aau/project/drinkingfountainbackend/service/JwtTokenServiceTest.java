package aau.project.drinkingfountainbackend.service;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

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

        Authentication authentication = jwtTokenService.getAuthentication(token);

        // Assert the results
        Assertions.assertTrue(authentication.isAuthenticated());
    }

    @Test
    void getAuthenticationWithAnInvalidTokenTest() {
        ReflectionTestUtils.setField(jwtTokenService, "JWT_TOKEN_VALIDITY", 100000);
        ReflectionTestUtils.setField(jwtTokenService, "secret", "abcdef");

        String token = "Invalid token";

        // Assert with a throw because of invalid token
        Assertions.assertThrows(JwtException.class, () -> {
            jwtTokenService.getAuthentication(token);
        });
    }
}
