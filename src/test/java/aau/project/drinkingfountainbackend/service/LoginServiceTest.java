package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.UserDTO;
import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;
import aau.project.drinkingfountainbackend.persistence.repository.UserRepository;
import aau.project.drinkingfountainbackend.util.InvalidPasswordException;
import aau.project.drinkingfountainbackend.util.InvalidUsernameException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService;

    @Test
    void registerUserTest() {
        String name = "Mihusu";
        String password = "password";

        UserDTO userDTO = new UserDTO(name, password);
        loginService.registerUser(userDTO);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
    }

    @Test
    void registerUserWithAnInvalidUsernameTest() {
        String name = "";
        String password = "password";

        UserDTO userDTO = new UserDTO(name, password);
        Assertions.assertThrows(InvalidUsernameException.class, () -> {
            loginService.registerUser(userDTO);
        });

        // Verify that the userRepository.save method is not called
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any(UserEntity.class));
    }

    @Test
    void registerUserWithAnInvalidPasswordTest() {
        String name = "Mihusu";
        String password = "passwor";

        UserDTO userDTO = new UserDTO(name, password);

        Assertions.assertThrows(InvalidPasswordException.class, () -> {
            loginService.registerUser(userDTO);
        });

        // Verify that the userRepository.save method is not called
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any(UserEntity.class));
    }

    @Test
    void loginTest() {
        int id = 0;
        String name = "Mihusu";
        String password = "Jeff112233";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        ZonedDateTime specificCreatedAt = ZonedDateTime.parse("2023-01-01T00:00:00.000000+01:00[Europe/Copenhagen]");

        UserEntity user = UserEntity.builder()
                .id(id)
                .name(name)
                .password(hashedPassword)
                .role(UserEntity.RoleType.USER)
                .createdAt(specificCreatedAt)
                .build();

        Mockito.when(userRepository.findFirstByName(name)).thenReturn(Optional.of(user));

        UserDTO userDTO = new UserDTO(name, password);
        Optional<Integer> userId = loginService.login(userDTO);

        Assertions.assertTrue(userId.isPresent());
        Assertions.assertEquals(id, userId.get());
    }

    @Test
    void loginTestWithoutUser() {
        String name = "Mihusu";
        String password = "Jeff112233";

        UserDTO userDTO = new UserDTO(name, password);
        Optional<Integer> userId = loginService.login(userDTO);

        Assertions.assertTrue(userId.isEmpty());
    }
}
