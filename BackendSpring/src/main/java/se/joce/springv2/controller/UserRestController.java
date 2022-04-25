package se.joce.springv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import se.joce.springv2.model.User;
import se.joce.springv2.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRestController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        try {
            User user = userService.getUserByID(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/{email}")
    public ResponseEntity<User> getUserEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isEmpty()) return new ResponseEntity<User>(HttpStatus.NOT_FOUND);

//        TODO: return user to frontend via body?/C
        ResponseEntity.ok().body(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/add/new")
    public String registerNewUser(@RequestBody User user) {

        if(!userService.canRegisterNewUser(user)){
            return "E-mail address already in use, choose another e-mail address.";
        }
        return "New user has been created";
    }

    //TODO: add body
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUserById(@RequestBody User user, @PathVariable Integer id) {
        String message;

        HttpHeaders httpHeaders = new HttpHeaders();

        try {
            userService.getUserByID(id);
            userService.updateUser(id, user);
            message = "User with id " + id + " has been successfully updated";
            httpHeaders.add("description", message);
            return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUserById(@PathVariable Integer id) {
        String message = userService.deleteUserById(id);
        return message;
    }

    @GetMapping("/allAdmin")
    public List<User> getAdminUsers() {
        return userService.getAllAdmin();
    }

    //TODO: Try adding the logic part in service instead
    @PostMapping("/register")
    public boolean registerUser(@RequestParam("name") String name, @RequestParam("password") String password,
                                @RequestParam("email") String email) {
        User user = new User();
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        return userService.canRegisterNewUser(user);
    }
}


