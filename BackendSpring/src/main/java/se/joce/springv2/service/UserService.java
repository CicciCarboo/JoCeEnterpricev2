package se.joce.springv2.service;

import se.joce.springv2.model.Role;
import se.joce.springv2.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();
    User getUserByID(Integer id);
    Optional<User> getUserByEmail(String email);
    List<User> getAllAdmin();
    boolean canRegisterNewUser(User user);
    Optional<User> updateUser(Integer id, User user);
    String deleteUserById(Integer id);

    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);

}
