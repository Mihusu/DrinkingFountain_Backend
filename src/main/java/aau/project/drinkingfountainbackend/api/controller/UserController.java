package aau.project.drinkingfountainbackend.api.controller;

import aau.project.drinkingfountainbackend.api.dto.UserDTO;
import aau.project.drinkingfountainbackend.persistence.repository.UserRepository;
import aau.project.drinkingfountainbackend.service.JwtTokenService;
import aau.project.drinkingfountainbackend.service.LoginService;
import aau.project.drinkingfountainbackend.util.UsernameAlreadyExistException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
public class UserController {

    private final JwtTokenService jwtTokenService;
    private final LoginService loginService;

    @Autowired
    public UserController(JwtTokenService jwtTokenService, LoginService loginService, UserRepository userRepository) {
        this.jwtTokenService = jwtTokenService;
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            // Attempt to register the user
            loginService.registerUser(userDTO);

            // If successful, return a 200 OK response
            return ResponseEntity.status(HttpStatus.OK).body("User registered successfully");
        } catch (UsernameAlreadyExistException e) {
            // If registration fails, return a 400 BAD_REQUEST response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        return loginService.login(userDTO)
                .map(userRoleInformation -> {
                    String token = jwtTokenService.generateToken(userRoleInformation.userId(), userRoleInformation.roleType().name());
                    return new ResponseEntity<>("Bearer " + token, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @GetMapping("/info")
    public ResponseEntity<String> getUserInfo(HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(loginService.getUsername(httpServletRequest), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserDTO userDTO) {
        try {
            // Attempt to register the user
            loginService.resetPassword(userDTO);

            // If successful, return a 200 OK response
            return ResponseEntity.status(HttpStatus.OK).body("Reset password was successful");
        } catch (UsernameAlreadyExistException e) {
            // If registration fails, return a 400 BAD_REQUEST response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Reset password failed");
        }
    }
}
