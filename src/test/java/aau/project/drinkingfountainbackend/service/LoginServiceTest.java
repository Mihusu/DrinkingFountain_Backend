package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.UserDTO;
import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;
import aau.project.drinkingfountainbackend.persistence.repository.UserRepository;
import aau.project.drinkingfountainbackend.service.model.UserRoleInformation;
import aau.project.drinkingfountainbackend.util.InvalidPasswordException;
import aau.project.drinkingfountainbackend.util.InvalidUsernameException;
import aau.project.drinkingfountainbackend.util.UsernameDoesNotExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.ZonedDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenService jwtTokenService;

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
        Optional<UserRoleInformation> userId = loginService.login(userDTO);

        Assertions.assertTrue(userId.isPresent());
        Assertions.assertEquals(id, userId.get().userId());
        Assertions.assertEquals(UserEntity.RoleType.USER, userId.get().roleType());
    }

    @Test
    void loginTestWithoutUser() {
        String name = "Mihusu";
        String password = "Jeff112233";

        UserDTO userDTO = new UserDTO(name, password);
        Optional<UserRoleInformation> userId = loginService.login(userDTO);

        Assertions.assertTrue(userId.isEmpty());
    }

    @Test
    void getUsernameTest() {
        // Set up the test data
        ZonedDateTime specificCreatedAt = ZonedDateTime.parse("2023-01-01T00:00:00.000000+01:00[Europe/Copenhagen]");
        int id = 1;

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        Mockito.when(jwtTokenService.getUserIdFromToken(mockHttpServletRequest)).thenReturn(id);

        // Mock the behavior of userRepository.findById()
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(new UserEntity(
                1, "John", "Hej12345678", UserEntity.RoleType.ADMIN, specificCreatedAt)));

        // Call the method you want to test
        String username = loginService.getUsername(mockHttpServletRequest);

        // Verify that the method behaves as expected
        Assertions.assertEquals("John", username);
    }

    @Test
    void getEmptyUsernameTest() {
        int id = 1;

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        Mockito.when(jwtTokenService.getUserIdFromToken(mockHttpServletRequest)).thenReturn(id);

        // Mock the behavior of userRepository.findById()
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Call the method you want to test
        String username = loginService.getUsername(mockHttpServletRequest);

        // Verify that the method behaves as expected
        Assertions.assertEquals("", username);
    }

    @Test
    void resetPasswordTest() {
        int id = 0;
        String name = "Mihusu";
        String password = "Jeff112233";
        String changedPassword = "Hej123456";
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

        UserDTO userDTO = new UserDTO(user.getName(), changedPassword);
        loginService.resetPassword(userDTO);

        Assertions.assertEquals(user.getPassword(), changedPassword);
    }

    @Test
    void resetPasswordWithAnNotExistingUserTest() {
        int id = 0;
        String name = "Mihusu";
        String password = "Jeff112233";
        String changedPassword = "Hej123456";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        ZonedDateTime specificCreatedAt = ZonedDateTime.parse("2023-01-01T00:00:00.000000+01:00[Europe/Copenhagen]");

        UserEntity user = UserEntity.builder()
                .id(id)
                .name(name)
                .password(hashedPassword)
                .role(UserEntity.RoleType.USER)
                .createdAt(specificCreatedAt)
                .build();

        Mockito.when(userRepository.findFirstByName(name)).thenReturn(Optional.empty());
        UserDTO userDTO = new UserDTO(user.getName(), changedPassword);

        Assertions.assertThrows(UsernameDoesNotExistException.class, () -> {
            loginService.resetPassword(userDTO);
        });

        // Verify that the userRepository.save method is not called
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any(UserEntity.class));
    }

    @Test
    void resetPasswordWithAnInvalidPasswordTest() {
        int id = 0;
        String name = "Mihusu";
        String password = "Jeff112233";
        String changedPassword = "Hej1234";
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
        UserDTO userDTO = new UserDTO(user.getName(), changedPassword);

        Assertions.assertThrows(InvalidPasswordException.class, () -> {
            loginService.resetPassword(userDTO);
        });

        // Verify that the userRepository.save method is not called
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any(UserEntity.class));
    }
}
