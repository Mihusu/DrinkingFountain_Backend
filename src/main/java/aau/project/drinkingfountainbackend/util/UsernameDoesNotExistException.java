package aau.project.drinkingfountainbackend.util;

public class UsernameDoesNotExistException extends RuntimeException {

    public UsernameDoesNotExistException() {
        super("The provided username does not exist");
    }
}
