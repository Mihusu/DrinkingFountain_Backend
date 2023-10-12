package aau.project.drinkingFountain;

import aau.project.drinkingfountainbackend.api.dto.UserDTO;
import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

public class UserTest {

    @Test
    public void demoUser () {
        UserDTO user = new UserDTO("hello", "A12345678");


    }
}
