package se.joce.springv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.joce.springv2.model.User;
import se.joce.springv2.service.UserServiceImpl;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserServiceImpl userServiceImpl;

    @GetMapping
    public List<User> getUsers(){
        return userServiceImpl.getAllUsers();
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id){

        try {
            User user = userServiceImpl.getUserByID(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }


}
