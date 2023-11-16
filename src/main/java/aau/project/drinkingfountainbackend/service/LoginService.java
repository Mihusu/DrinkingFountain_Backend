package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.UserDTO;
import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;
import aau.project.drinkingfountainbackend.persistence.repository.UserRepository;
import aau.project.drinkingfountainbackend.service.model.UserRoleInformation;
import aau.project.drinkingfountainbackend.util.InvalidPasswordException;
import aau.project.drinkingfountainbackend.util.InvalidUsernameException;
import aau.project.drinkingfountainbackend.util.UsernameAlreadyExistException;
import aau.project.drinkingfountainbackend.util.UsernameDoesNotExistException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class LoginService {

    private final UserRepository userRepository;

    private final JwtTokenService jwtTokenService;

    @Autowired
    public LoginService(UserRepository userRepository, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
    }

    protected Optional<UserEntity> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void registerUser(UserDTO userDTO) {
        if(userRepository.findFirstByName(userDTO.username()).isPresent()) {
            throw new UsernameAlreadyExistException();
        }

        String hashedPassword = BCrypt.hashpw(userDTO.password(), BCrypt.gensalt());

        UserEntity userEntity = UserEntity.builder()
                .name(userDTO.username())
                .password(hashedPassword)
                .createdAt(ZonedDateTime.now())
                .role(UserEntity.RoleType.valueOf(UserEntity.RoleType.USER.name()))
                .build();

        if (userDTO.username() == null || userDTO.username().length() < 2) {
            throw new InvalidUsernameException();
        }

        if (checkPasswordRequirements(userDTO.password())) {
            throw new InvalidPasswordException();
        }

        userRepository.save(userEntity);
    }

    public Optional<UserRoleInformation> login(UserDTO userDTO) {
        Optional<UserEntity> userEntity = userRepository.findFirstByName(userDTO.username());

        if (userEntity.isEmpty()) {
            return Optional.empty();
        }

        // Verify user exists and check if password match
        return userEntity.filter(entity -> checkPassword(userDTO.password(), entity.getPassword())).map(user -> new UserRoleInformation(user.getId(),user.getRole()));
    }

    public String getUsername(HttpServletRequest httpServletRequest) {
        int userId = jwtTokenService.getUserIdFromToken(httpServletRequest);
        Optional<UserEntity> userEntity = userRepository.findById(userId);

        if (userEntity.isEmpty()) {
            return "";
        }

        return userEntity.get().getName();
    }

    public void resetPassword(UserDTO userDTO) {

        Optional<UserEntity> userEntity = userRepository.findFirstByName(userDTO.username());

        if(userEntity.isEmpty()) {
            throw new UsernameDoesNotExistException();
        }

        if (checkPasswordRequirements(userDTO.password())) {
            throw new InvalidPasswordException();
        }

        userEntity.get().setPassword(userDTO.password());
    }

    private boolean checkPassword(String inputPassword, String dataBasePassword) {
        return BCrypt.checkpw(inputPassword, dataBasePassword);
    }

    private boolean checkPasswordRequirements(String password) {
        return password.length() >= 8;
    }
}
