package aau.project.drinkingfountainbackend.util;

public class InvalidUsernameException extends RuntimeException {

    public InvalidUsernameException() {
        super("Invalid username. Please provide a valid username that is not empty and contain more than 1 letter.");
    }
}
