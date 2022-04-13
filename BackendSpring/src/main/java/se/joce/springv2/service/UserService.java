package se.joce.springv2.service;

import se.joce.springv2.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();
    User getUserByID(Integer id);
    Optional<User> getUserByEmail(String email);
    User registerNewUser(User user);
    User updateUser(User user);
    void deleteUserById(Integer id);

}
