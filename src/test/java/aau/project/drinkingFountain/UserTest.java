package aau.project.drinkingFountain;

import aau.project.drinkingfountainbackend.api.dto.UserDTO;
import org.junit.jupiter.api.Test;


public class UserTest {

    @Test
    public void demoUser () {
        UserDTO user = new UserDTO("hello", "A12345678");
    }
}
