package aau.project.drinkingfountainbackend.util;

public class UsernameAlreadyExistException extends RuntimeException {

    public UsernameAlreadyExistException() {
        super("An user has already registered with this username. Please provide a different username.");

    }
}
