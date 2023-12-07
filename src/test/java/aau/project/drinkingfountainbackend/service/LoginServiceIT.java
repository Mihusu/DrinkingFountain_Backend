package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.EnableTestcontainers;
import aau.project.drinkingfountainbackend.api.dto.UserDTO;
import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;
import aau.project.drinkingfountainbackend.persistence.repository.UserRepository;
import aau.project.drinkingfountainbackend.service.model.UserRoleInformation;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@EnableTestcontainers
public class LoginServiceIT {

    @Autowired
    LoginService loginService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenService jwtTokenService;

    @Transactional
    @Test
    void simpleGetUsernameIT() {
        UserEntity user = UserEntity.builder()
                .name("Jerf")
                .password("jerf123456")
                .role(UserEntity.RoleType.USER)
                .createdAt(ZonedDateTime.now())
                .build();

        userRepository.save(user);

        String token = jwtTokenService.generateToken(user.getId(), "ADMIN");

        // Create a real HttpServletRequest
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("Authorization", "Bearer " + token);

        String getUsername = loginService.getUsername(httpServletRequest);

        Assertions.assertEquals(user.getName(), getUsername);
    }

    @Transactional
    @Test
    void simpleLoginIT(){
        UserEntity user = UserEntity.builder()
                .name("Jerf2")
                .password("$2a$10$8ClFAL7G017PT22Wdaf1O.JmmAxYw50fsC3/Abjqxkz4RSKrrCDbq")
                .role(UserEntity.RoleType.USER)
                .createdAt(ZonedDateTime.now())
                .build();

        userRepository.save(user);

        UserDTO userDTO = new UserDTO(user.getName(), "jerf");

        Optional<UserRoleInformation> userRoleInformation = loginService.login(userDTO);

        Assertions.assertTrue(userRoleInformation.isPresent());
        Assertions.assertEquals(userRoleInformation.get().userId(), user.getId());
        Assertions.assertEquals(userRoleInformation.get().roleType(), user.getRole());
    }

    @Transactional
    @Test
    void simpleRegisterIT() {
        UserDTO userDTO = new UserDTO("Jerf3", "jerfferson");

        loginService.registerUser(userDTO);

        Optional<UserEntity> userEntity = userRepository.findFirstByName("Jerf3");

        Assertions.assertTrue(userEntity.isPresent());
    }

    @Transactional
    @Test
    void simpleResetPasswordIT() {
        String changedPassword = "hellojerf";
        UserEntity resetPasswordUser = UserEntity.builder()
                .name("Jerf4")
                .password("jerf123456")
                .role(UserEntity.RoleType.USER)
                .createdAt(ZonedDateTime.now())
                .build();

        userRepository.save(resetPasswordUser);
        UserDTO userDTO = new UserDTO(resetPasswordUser.getName(), changedPassword);
        loginService.resetPassword(userDTO);

        Assertions.assertEquals(resetPasswordUser.getPassword(), changedPassword);
    }
}
