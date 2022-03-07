package recipes.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.entities.User;
import recipes.services.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class RegistrationController {

    final UserService userService;
    final PasswordEncoder encoder;

    public RegistrationController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public void addUser(@RequestBody @Valid  User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        boolean status = userService.save(user);
        if (!status) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get_users")
    public List<User> getUsers() {
        return userService.findAll();
    }
}
