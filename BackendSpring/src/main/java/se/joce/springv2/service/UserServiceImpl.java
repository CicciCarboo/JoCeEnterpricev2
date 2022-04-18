package se.joce.springv2.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.joce.springv2.model.User;
import se.joce.springv2.repository.UserRepository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByID(Integer id) {
        return userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid user id: " + id));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean registerNewUser(User user) {
//        Check that e-mail is unique
        Optional<User> userWithProposedEmail = getUserByEmail(user.getEmail());

        if(userWithProposedEmail.isPresent()){ return false; }

        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public Optional<User> updateUser(Integer id, User user) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) return userOptional;

        userRepository.save(user);
        return userOptional;
    }

    @Override
    public String deleteUserById(Integer id) {
        try{
            userRepository.deleteById(id);
            return "Successfully deleted user with id " + id;
        }catch (IllegalArgumentException e) {
            System.out.println("User with id not valid " + e);
            return "User ID not valid " + e;
        }
    }
}
