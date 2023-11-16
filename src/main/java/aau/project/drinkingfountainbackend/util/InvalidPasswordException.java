package aau.project.drinkingfountainbackend.util;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("Invalid password. Password must contain 8 characters or above");
    }
}
