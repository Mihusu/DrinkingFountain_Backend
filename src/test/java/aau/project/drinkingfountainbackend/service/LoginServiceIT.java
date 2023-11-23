package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.EnableTestcontainers;
import aau.project.drinkingfountainbackend.api.dto.UserDTO;
import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;
import aau.project.drinkingfountainbackend.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;

@SpringBootTest
@ActiveProfiles("test")
@EnableTestcontainers
public class LoginServiceIT {

    @Autowired
    LoginService loginService;

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Test
    void simpleIT(){
        userRepository.save(UserEntity.builder().name("Jerf").password("$2a$10$8ClFAL7G017PT22Wdaf1O.JmmAxYw50fsC3/Abjqxkz4RSKrrCDbq").role(UserEntity.RoleType.USER).createdAt(ZonedDateTime.now()).build());
        Assertions.assertTrue(loginService.login(new UserDTO("Jerf", "jerf")).isPresent());
    }
}
