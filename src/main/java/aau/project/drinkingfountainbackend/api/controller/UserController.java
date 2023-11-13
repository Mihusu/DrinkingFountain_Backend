package aau.project.drinkingfountainbackend.api.controller;

import aau.project.drinkingfountainbackend.api.dto.UserDTO;
import aau.project.drinkingfountainbackend.service.JwtTokenService;
import aau.project.drinkingfountainbackend.service.LoginService;
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
    public UserController(JwtTokenService jwtTokenService, LoginService loginService) {
        this.jwtTokenService = jwtTokenService;
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody UserDTO userDTO) {
        loginService.registerUser(userDTO);
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
}
