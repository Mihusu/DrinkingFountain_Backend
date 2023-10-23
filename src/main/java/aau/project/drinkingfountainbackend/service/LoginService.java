package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.UserDTO;
import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;
import aau.project.drinkingfountainbackend.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class LoginService {

    private final UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected Optional<UserEntity> getUserById(int id){
        return userRepository.findById(id);
    }

    @Transactional
    public void registerUser(UserDTO userDTO) {
        String hashedPassword = BCrypt.hashpw(userDTO.password(), BCrypt.gensalt());

        UserEntity userEntity = UserEntity.builder()
                .name(userDTO.username())
                .password(hashedPassword)
                .createdAt(ZonedDateTime.now())
                .role(UserEntity.RoleType.valueOf(UserEntity.RoleType.USER.name()))
                .build();

        userRepository.save(userEntity);
    }

    public Optional<Integer> login(UserDTO userDTO) {
        Optional<UserEntity> userEntity = userRepository.findFirstByName(userDTO.username());

        if (userEntity.isEmpty()) {
            return Optional.empty();
        }

        // Verify user exists and check if password match
        return userEntity.filter(entity -> checkPassword(userDTO.password(), entity.getPassword())).map(UserEntity::getId);
    }

    private boolean checkPassword(String inputPassword, String dataBasePassword){
        return BCrypt.checkpw(inputPassword, dataBasePassword);
    }
}