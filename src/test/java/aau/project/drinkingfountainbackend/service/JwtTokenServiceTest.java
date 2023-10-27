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
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JwtTokenServiceTest {

    @Mock
    private Environment environment;

    @InjectMocks
    private JwtTokenService jwtTokenService;

    @Test
    void getUserIdFromToken() {
        ReflectionTestUtils.setField(jwtTokenService, "JWT_TOKEN_VALIDITY", 100000);
        ReflectionTestUtils.setField(jwtTokenService, "secret", "abc");

        int id = 1;
        String token = jwtTokenService.generateToken(id, "ADMIN");

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockRequest.getHeader("Authorization")).thenReturn(token);

        // Call the method and assert the result
        int userId = jwtTokenService.getUserIdFromToken(mockRequest);
        Assertions.assertEquals(id, userId);

    }
}
