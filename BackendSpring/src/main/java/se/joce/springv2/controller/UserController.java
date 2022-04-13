package se.joce.springv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.joce.springv2.model.User;
import se.joce.springv2.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id){
        try {
            User user = userService.getUserByID(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/{email}")
    public ResponseEntity<User> getUserEmail(@PathVariable String email){
        Optional<User> user = userService.getUserByEmail(email);
        if(user.isEmpty()) return new ResponseEntity<User>(HttpStatus.NOT_FOUND);

        ResponseEntity.ok().body(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //TODO: 403 forbidden
    @PostMapping("/add/new")
        public String registerNewUser(@RequestBody User user) {
        userService.registerNewUser(user);
        return "New user has been created";
    }

    //TODO: 403 forbidden
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUserById(@RequestBody User user, @PathVariable Integer id) {
        try {
            userService.getUserByID(id);
            userService.updateUser(id, user);
            return new ResponseEntity<User>(HttpStatus.OK);
        }catch(NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    //TODO: 403 forbidden
    @DeleteMapping("/delete/{id}")
    public String deleteUserById(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return "User with id " + " is successfully deleted";
    }
}
